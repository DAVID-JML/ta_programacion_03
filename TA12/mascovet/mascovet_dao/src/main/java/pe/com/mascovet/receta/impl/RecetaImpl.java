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
import java.sql.CallableStatement;
import java.sql.Types;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RecetaImpl implements RecetaDAO {

    @Override
    public int insertar(Receta receta) {
        String sql = "{call INSERTAR_RECETA(?, ?, ?, ?)}";
        Connection conexion = TransactionContext.getCurrentConnection();
        if (conexion == null)
            conexion = DBManager.getInstance().getConnection();
        try (Connection con = conexion) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_id_receta", Types.INTEGER);
                cs.setInt("_id_atencion", receta.getAtencionMedica().getIdAtencion());
                cs.setDate("_fecha", new java.sql.Date(receta.getFecha().getTime()));
                cs.setString("_indicaciones", receta.getIndicaciones());
                cs.executeUpdate();

                if (cs.getInt("_id_receta") > 0) {
                        receta.setIdReceta(cs.getInt("_id_receta"));
                        return receta.getIdReceta();
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
        String sql = "{call MODIFICAR_RECETA(?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter("_resultado", Types.INTEGER);
            cs.setInt("_id_atencion", receta.getAtencionMedica().getIdAtencion());
            cs.setDate("_fecha", new java.sql.Date(receta.getFecha().getTime()));
            cs.setString("_indicaciones", receta.getIndicaciones());
            cs.setInt("_id_receta", receta.getIdReceta());
            cs.executeUpdate();
            return cs.getInt("_resultado");
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR RECETA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idReceta) {
        String sql = "{call ELIMINAR_RECETA(?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter("_resultado", Types.INTEGER);
            cs.setInt("_id_receta", idReceta);
            cs.executeUpdate();
            return cs.getInt("_resultado");
        } catch (Exception ex) {
            System.out.println("ERROR AL ELIMINAR RECETA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Receta obtenerPorId(int idReceta) {
        Receta receta = null;
        String sql = "{call LISTAR_RECETA_X_ID(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt("_id_receta", idReceta);
            try (ResultSet rs = cs.executeQuery()) {
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
        String sql = "{call LISTAR_RECETA_TODOS()}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
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
