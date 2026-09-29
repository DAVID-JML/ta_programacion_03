package pe.com.mascovet.atencionmedica.impl;
import pe.com.mascovet.atencionmedica.dao.VacunacionDAO;
import pe.com.mascovet.atencionmedica.model.AtencionMedica;
import pe.com.mascovet.atencionmedica.model.Vacunacion;
import pe.com.mascovet.config.DBManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VacunacionImpl implements VacunacionDAO {
    private final AtencionMedicaImpl atencionBase = new AtencionMedicaImpl();

    @Override
    public int insertar(Vacunacion vacunacion) {
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            con.setAutoCommit(false);

            int idAtencion = atencionBase.insertarAtencionBase(vacunacion, con);
            vacunacion.setIdAtencion(idAtencion);

            String sql = "INSERT INTO VACUNACION (id_atencion, fechaAplicacion, fechaProximaDosis, dosis) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idAtencion);
                ps.setDate(2, new Date(vacunacion.getFechaAplicacion().getTime()));
                ps.setDate(3, new Date(vacunacion.getFechaProximaDosis().getTime()));
                ps.setString(4, vacunacion.getDosis());
                ps.executeUpdate();
            }
            con.commit();
            return idAtencion;
        } catch (Exception ex) {
            if (con != null) try { con.rollback(); } catch (SQLException e) {}
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Vacunacion vacunacion) {
        String sql = "UPDATE VACUNACION SET fechaAplicacion=?, fechaProximaDosis=?, dosis=? WHERE id_atencion=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, new Date(vacunacion.getFechaAplicacion().getTime()));
            ps.setDate(2, new Date(vacunacion.getFechaProximaDosis().getTime()));
            ps.setString(3, vacunacion.getDosis());
            ps.setInt(4, vacunacion.getIdAtencion());
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idVacunacion) {
        String sql = "DELETE FROM ATENCION_MEDICA WHERE id_atencion=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVacunacion);
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Vacunacion obtenerPorId(int idVacunacion) {
        String sql = "SELECT a.id_atencion, a.observaciones, v.fechaAplicacion, v.fechaProximaDosis, v.dosis " +
                "FROM ATENCION_MEDICA a INNER JOIN VACUNACION v ON a.id_atencion = v.id_atencion WHERE a.id_atencion=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVacunacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Vacunacion v = new Vacunacion();
                    v.setIdAtencion(rs.getInt("id_atencion"));
                    v.setObservaciones(rs.getString("observaciones"));
                    v.setFechaAplicacion(rs.getDate("fechaAplicacion"));
                    v.setFechaProximaDosis(rs.getDate("fechaProximaDosis"));
                    v.setDosis(rs.getString("dosis"));
                    return v;
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    @Override
    public List<Vacunacion> listarTodos() {
        List<Vacunacion> lista = new ArrayList<>();
        String sql = "SELECT a.id_atencion, a.observaciones, v.fechaAplicacion, v.fechaProximaDosis, v.dosis " +
                "FROM ATENCION_MEDICA a INNER JOIN VACUNACION v ON a.id_atencion = v.id_atencion";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Vacunacion v = new Vacunacion();
                v.setIdAtencion(rs.getInt("id_atencion"));
                v.setObservaciones(rs.getString("observaciones"));
                v.setFechaAplicacion(rs.getDate("fechaAplicacion"));
                v.setFechaProximaDosis(rs.getDate("fechaProximaDosis"));
                v.setDosis(rs.getString("dosis"));
                lista.add(v);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return lista;
    }
}
