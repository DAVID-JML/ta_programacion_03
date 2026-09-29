package pe.com.mascovet.atencionmedica.impl;

import pe.com.mascovet.atencionmedica.dao.CirugiaDAO;
import pe.com.mascovet.atencionmedica.model.Cirugia;
import pe.com.mascovet.config.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CirugiaImpl implements CirugiaDAO {
    private final AtencionMedicaImpl atencionBase = new AtencionMedicaImpl();
    @Override
    public int insertar(Cirugia cirugia) {
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            con.setAutoCommit(false);

            int idAtencion = atencionBase.insertarAtencionBase(cirugia, con);
            cirugia.setIdAtencion(idAtencion);

            String sql = "INSERT INTO CIRUGIA (id_atencion, procedimiento, indicaciones_post_operatorias) VALUES (?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idAtencion);
                ps.setString(2, cirugia.getProcedimiento());
                ps.setString(3, cirugia.getIndicacionesPostOperatorias());
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
    public int modificar(Cirugia cirugia) {
        String sql = "UPDATE CIRUGIA SET procedimiento=?, indicaciones_post_operatorias=? WHERE id_atencion=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cirugia.getProcedimiento());
            ps.setString(2, cirugia.getIndicacionesPostOperatorias());
            ps.setInt(3, cirugia.getIdAtencion());
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idCirugia) {
        String sql = "DELETE FROM ATENCION_MEDICA WHERE id_atencion=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCirugia);
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Cirugia obtenerPorId(int idCirugia) {
        String sql = "SELECT a.id_atencion, a.observaciones, c.procedimiento, c.indicaciones_post_operatorias " +
                "FROM ATENCION_MEDICA a INNER JOIN CIRUGIA c ON a.id_atencion = c.id_atencion WHERE a.id_atencion=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCirugia);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cirugia c = new Cirugia();
                    c.setIdAtencion(rs.getInt("id_atencion"));
                    c.setObservaciones(rs.getString("observaciones"));
                    c.setProcedimiento(rs.getString("procedimiento"));
                    c.setIndicacionesPostOperatorias(rs.getString("indicaciones_post_operatorias"));
                    return c;
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    @Override
    public List<Cirugia> listarTodos() {
        List<Cirugia> lista = new ArrayList<>();
        String sql = "SELECT a.id_atencion, a.observaciones, c.procedimiento, c.indicaciones_post_operatorias " +
                "FROM ATENCION_MEDICA a INNER JOIN CIRUGIA c ON a.id_atencion = c.id_atencion";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cirugia c = new Cirugia();
                c.setIdAtencion(rs.getInt("id_atencion"));
                c.setObservaciones(rs.getString("observaciones"));
                c.setProcedimiento(rs.getString("procedimiento"));
                c.setIndicacionesPostOperatorias(rs.getString("indicaciones_post_operatorias"));
                lista.add(c);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return lista;
    }
}
