package pe.com.mascovet.atencionmedica.impl;

import pe.com.mascovet.atencionmedica.dao.ConsultaDAO;
import pe.com.mascovet.atencionmedica.model.Consulta;
import pe.com.mascovet.config.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConsultaImpl implements ConsultaDAO {
    private final AtencionMedicaImpl atencionBase = new AtencionMedicaImpl();
    @Override
    public int insertar(Consulta consulta) {
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            con.setAutoCommit(false);

            int idAtencion = atencionBase.insertarAtencionBase(consulta, con);
            consulta.setIdAtencion(idAtencion);

            String sql = "INSERT INTO CONSULTA (id_atencion, motivoConsulta, diagnostico, tratamiento) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idAtencion);
                ps.setString(2, consulta.getMotivoConsulta());
                ps.setString(3, consulta.getDiagnostico());
                ps.setString(4, consulta.getTratamiento());
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
    public int modificar(Consulta consulta) {
        String sql = "UPDATE CONSULTA SET motivoConsulta=?, diagnostico=?, tratamiento=? WHERE id_atencion=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, consulta.getMotivoConsulta());
            ps.setString(2, consulta.getDiagnostico());
            ps.setString(3, consulta.getTratamiento());
            ps.setInt(4, consulta.getIdAtencion());
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idConsulta) {
        String sql = "DELETE FROM ATENCION_MEDICA WHERE id_atencion=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idConsulta);
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Consulta obtenerPorId(int idConsulta) {
        String sql = "SELECT a.id_atencion, a.observaciones, a.hora_inicio, a.hora_fin, c.motivoConsulta, c.diagnostico, c.tratamiento " +
                "FROM ATENCION_MEDICA a INNER JOIN CONSULTA c ON a.id_atencion = c.id_atencion WHERE a.id_atencion=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idConsulta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Consulta c = new Consulta();
                    c.setIdAtencion(rs.getInt("id_atencion"));
                    c.setObservaciones(rs.getString("observaciones"));
                    c.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                    c.setHoraFin(rs.getTime("hora_fin").toLocalTime());
                    c.setMotivoConsulta(rs.getString("motivoConsulta"));
                    c.setDiagnostico(rs.getString("diagnostico"));
                    c.setTratamiento(rs.getString("tratamiento"));
                    return c;
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    @Override
    public List<Consulta> listarTodos() {
        List<Consulta> lista = new ArrayList<>();
        String sql = "SELECT a.id_atencion, a.observaciones, a.hora_inicio, a.hora_fin, c.motivoConsulta, c.diagnostico, c.tratamiento " +
                "FROM ATENCION_MEDICA a INNER JOIN CONSULTA c ON a.id_atencion = c.id_atencion";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Consulta c = new Consulta();
                c.setIdAtencion(rs.getInt("id_atencion"));
                c.setObservaciones(rs.getString("observaciones"));
                c.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                c.setHoraFin(rs.getTime("hora_fin").toLocalTime());
                c.setMotivoConsulta(rs.getString("motivoConsulta"));
                c.setDiagnostico(rs.getString("diagnostico"));
                c.setTratamiento(rs.getString("tratamiento"));
                lista.add(c);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return lista;
    }
}
