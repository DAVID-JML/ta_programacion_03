package pe.com.mascovet.atencionmedica.impl;

import pe.com.mascovet.atencionmedica.dao.ControlDAO;
import pe.com.mascovet.atencionmedica.model.Control;
import pe.com.mascovet.config.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ControlImpl implements ControlDAO {
    private final AtencionMedicaImpl atencionBase = new AtencionMedicaImpl();
    @Override
    public int insertar(Control control) {
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            con.setAutoCommit(false);

            int idAtencion = atencionBase.insertarAtencionBase(control, con);
            control.setIdAtencion(idAtencion);

            String sql = "INSERT INTO CONTROL (id_atencion, evolucion, indicaciones) VALUES (?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idAtencion);
                ps.setString(2, control.getEvolucion());
                ps.setString(3, control.getIndicaciones());
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
    public int modificar(Control control) {
        String sql = "UPDATE CONTROL SET evolucion=?, indicaciones=? WHERE id_atencion=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, control.getEvolucion());
            ps.setString(2, control.getIndicaciones());
            ps.setInt(3, control.getIdAtencion());
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idControl) {
        String sql = "DELETE FROM ATENCION_MEDICA WHERE id_atencion=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idControl);
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Control obtenerPorId(int idControl) {
        String sql = "SELECT a.id_atencion, a.observaciones, c.evolucion, c.indicaciones " +
                "FROM ATENCION_MEDICA a INNER JOIN CONTROL c ON a.id_atencion = c.id_atencion WHERE a.id_atencion=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idControl);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Control ctrl = new Control();
                    ctrl.setIdAtencion(rs.getInt("id_atencion"));
                    ctrl.setObservaciones(rs.getString("observaciones"));
                    ctrl.setEvolucion(rs.getString("evolucion"));
                    ctrl.setIndicaciones(rs.getString("indicaciones"));
                    return ctrl;
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    @Override
    public List<Control> listarTodos() {
        List<Control> lista = new ArrayList<>();
        String sql = "SELECT a.id_atencion, a.observaciones, c.evolucion, c.indicaciones " +
                "FROM ATENCION_MEDICA a INNER JOIN CONTROL c ON a.id_atencion = c.id_atencion";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Control ctrl = new Control();
                ctrl.setIdAtencion(rs.getInt("id_atencion"));
                ctrl.setObservaciones(rs.getString("observaciones"));
                ctrl.setEvolucion(rs.getString("evolucion"));
                ctrl.setIndicaciones(rs.getString("indicaciones"));
                lista.add(ctrl);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return lista;
    }
}
