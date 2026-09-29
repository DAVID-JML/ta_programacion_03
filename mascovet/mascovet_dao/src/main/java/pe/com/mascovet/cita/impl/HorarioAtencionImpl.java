package pe.com.mascovet.cita.impl;

import pe.com.mascovet.cita.dao.HorarioAtencionDAO;
import pe.com.mascovet.cita.model.HorarioAtencion;
import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.enums.model.Dia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HorarioAtencionImpl implements HorarioAtencionDAO {

    @Override
    public int insertar(HorarioAtencion horario) {
        String sql = "INSERT INTO HORARIO_ATENCION (id_veterinario, dia, hora_inicio, hora_fin) VALUES (?, ?, ?, ?)";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, horario.getVeterinario().getIdUsuario());
            ps.setString(2, horario.getDia().name());
            ps.setObject(3, horario.getHoraInicio());
            ps.setObject(4, horario.getHoraFin());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return 0;
    }

    @Override
    public int modificar(HorarioAtencion horario) {
        String sql = "UPDATE HORARIO_ATENCION SET dia=?, hora_inicio=?, hora_fin=? WHERE id_horario=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, horario.getDia().name());
            ps.setObject(2, horario.getHoraInicio());
            ps.setObject(3, horario.getHoraFin());
            ps.setInt(4, horario.getIdHorario());
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idHorario) {
        String sql = "DELETE FROM HORARIO_ATENCION WHERE id_horario=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idHorario);
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public HorarioAtencion obtenerPorId(int idHorario) {
        String sql = "SELECT id_horario, id_veterinario, dia, hora_inicio, hora_fin FROM HORARIO_ATENCION WHERE id_horario=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idHorario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    HorarioAtencion h = new HorarioAtencion();
                    h.setIdHorario(rs.getInt("id_horario"));
                    h.setDia(Dia.valueOf(rs.getString("dia")));
                    h.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                    h.setHoraFin(rs.getTime("hora_fin").toLocalTime());
                    return h;
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    @Override
    public List<HorarioAtencion> listarTodos() {
        List<HorarioAtencion> lista = new ArrayList<>();
        String sql = "SELECT id_horario, id_veterinario, dia, hora_inicio, hora_fin FROM HORARIO_ATENCION";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                HorarioAtencion h = new HorarioAtencion();
                h.setIdHorario(rs.getInt("id_horario"));
                h.setDia(Dia.valueOf(rs.getString("dia")));
                h.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                h.setHoraFin(rs.getTime("hora_fin").toLocalTime());
                lista.add(h);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return lista;
    }
}
