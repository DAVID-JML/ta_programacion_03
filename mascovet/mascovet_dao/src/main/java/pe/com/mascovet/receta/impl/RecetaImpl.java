package pe.com.mascovet.receta.impl;

import pe.com.mascovet.atencionmedica.model.AtencionMedica;
import pe.com.mascovet.atencionmedica.model.Cirugia;
import pe.com.mascovet.atencionmedica.model.Consulta;
import pe.com.mascovet.atencionmedica.model.Control;
import pe.com.mascovet.atencionmedica.model.Vacunacion;
import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.config.TransactionContext;
import pe.com.mascovet.receta.dao.RecetaDAO;
import pe.com.mascovet.receta.model.Receta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RecetaImpl implements RecetaDAO {

    @Override
    public int insertar(Receta receta) {
        String sql = "INSERT INTO RECETA (id_atencion, fecha, indicaciones) VALUES (?, ?, ?)";
        try {
            Connection con = TransactionContext.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, receta.getAtencionMedica().getIdAtencion());
                ps.setDate(2, new java.sql.Date(receta.getFecha().getTime()));
                ps.setString(3, receta.getIndicaciones());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        receta.setIdReceta(rs.getInt(1));
                        return receta.getIdReceta();
                    }
                }
                return 0;
            } catch (Exception ex) {
                System.out.println("ERROR AL INSERTAR RECETA: " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR RECETA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Receta receta) {
        String sql = "UPDATE RECETA SET id_atencion=?, fecha=?, indicaciones=? WHERE id_receta=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, receta.getAtencionMedica().getIdAtencion());
            ps.setDate(2, new java.sql.Date(receta.getFecha().getTime()));
            ps.setString(3, receta.getIndicaciones());
            ps.setInt(4, receta.getIdReceta());
            return ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR RECETA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idReceta) {
        String sql = "DELETE FROM RECETA WHERE id_receta=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idReceta);
            return ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR AL ELIMINAR RECETA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Receta obtenerPorId(int idReceta) {
        Receta receta = null;
        String sql = "SELECT r.id_receta, r.id_atencion, r.fecha, r.indicaciones, " +
                "co.id_atencion AS id_consulta, ct.id_atencion AS id_control, ci.id_atencion AS id_cirugia, va.id_atencion AS id_vacunacion " +
                "FROM RECETA r " +
                "LEFT JOIN CONSULTA co ON r.id_atencion=co.id_atencion " +
                "LEFT JOIN CONTROL ct ON r.id_atencion=ct.id_atencion " +
                "LEFT JOIN CIRUGIA ci ON r.id_atencion=ci.id_atencion " +
                "LEFT JOIN VACUNACION va ON r.id_atencion=va.id_atencion " +
                "WHERE r.id_receta=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idReceta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    receta = new Receta();
                    receta.setIdReceta(rs.getInt("id_receta"));
                    receta.setFecha(rs.getDate("fecha"));
                    receta.setIndicaciones(rs.getString("indicaciones"));

                    int idAtencion = rs.getInt("id_atencion");
                    AtencionMedica atencionMedica = null;
                    if (rs.getObject("id_consulta") != null) {
                        atencionMedica = new Consulta();
                    } else if (rs.getObject("id_control") != null) {
                        atencionMedica = new Control();
                    } else if (rs.getObject("id_cirugia") != null) {
                        atencionMedica = new Cirugia();
                    } else if (rs.getObject("id_vacunacion") != null) {
                        atencionMedica = new Vacunacion();
                    }
                    if (atencionMedica != null) {
                        atencionMedica.setIdAtencion(idAtencion);
                        receta.setAtencionMedica(atencionMedica);
                    }
                }
            }
            return receta;
        } catch (Exception ex) {
            System.out.println("ERROR AL OBTENER RECETA POR ID: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Receta> listarTodos() {
        List<Receta> recetas = new ArrayList<>();
        String sql = "SELECT r.id_receta, r.id_atencion, r.fecha, r.indicaciones, " +
                "co.id_atencion AS id_consulta, ct.id_atencion AS id_control, ci.id_atencion AS id_cirugia, va.id_atencion AS id_vacunacion " +
                "FROM RECETA r " +
                "LEFT JOIN CONSULTA co ON r.id_atencion=co.id_atencion " +
                "LEFT JOIN CONTROL ct ON r.id_atencion=ct.id_atencion " +
                "LEFT JOIN CIRUGIA ci ON r.id_atencion=ci.id_atencion " +
                "LEFT JOIN VACUNACION va ON r.id_atencion=va.id_atencion";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Receta receta = new Receta();
                receta.setIdReceta(rs.getInt("id_receta"));
                receta.setFecha(rs.getDate("fecha"));
                receta.setIndicaciones(rs.getString("indicaciones"));

                int idAtencion = rs.getInt("id_atencion");
                AtencionMedica atencionMedica;
                if (rs.getObject("id_consulta") != null) {
                    atencionMedica = new Consulta();
                } else if (rs.getObject("id_control") != null) {
                    atencionMedica = new Control();
                } else if (rs.getObject("id_cirugia") != null) {
                    atencionMedica = new Cirugia();
                } else {
                    atencionMedica = new Vacunacion();
                }
                atencionMedica.setIdAtencion(idAtencion);
                receta.setAtencionMedica(atencionMedica);
                recetas.add(receta);
            }
            return recetas;
        } catch (Exception ex) {
            System.out.println("ERROR AL LISTAR RECETAS: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
