package pe.com.mascovet.cita.impl;

import pe.com.mascovet.cita.dao.HorarioAtencionDAO;
import pe.com.mascovet.cita.model.HorarioAtencion;
import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.config.TransactionContext;
import pe.com.mascovet.enums.model.Dia;
import pe.com.mascovet.usuario.model.Veterinario;

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
        try {
            Connection con = TransactionContext.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, horario.getVeterinario().getIdUsuario());
                ps.setString(2, horario.getDia().name());
                ps.setTime(3, java.sql.Time.valueOf(horario.getHoraInicio()));
                ps.setTime(4, java.sql.Time.valueOf(horario.getHoraFin()));
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        horario.setIdHorario(rs.getInt(1));
                        return horario.getIdHorario();
                    }
                }
                return 0;
            } catch (Exception ex) {
                System.out.println("ERROR AL INSERTAR HORARIO DE ATENCION: " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR HORARIO DE ATENCION: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(HorarioAtencion horario) {
        String sql = "UPDATE HORARIO_ATENCION SET id_veterinario=?, dia=?, hora_inicio=?, hora_fin=? WHERE id_horario=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, horario.getVeterinario().getIdUsuario());
            ps.setString(2, horario.getDia().name());
            ps.setTime(3, java.sql.Time.valueOf(horario.getHoraInicio()));
            ps.setTime(4, java.sql.Time.valueOf(horario.getHoraFin()));
            ps.setInt(5, horario.getIdHorario());
            return ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR HORARIO DE ATENCION: " + ex.getMessage());
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
            System.out.println("ERROR AL ELIMINAR HORARIO DE ATENCION: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public HorarioAtencion obtenerPorId(int idHorario) {
        HorarioAtencion horario = null;
        String sql = "SELECT id_horario, id_veterinario, dia, hora_inicio, hora_fin FROM HORARIO_ATENCION WHERE id_horario=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idHorario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    horario = new HorarioAtencion();
                    horario.setIdHorario(rs.getInt("id_horario"));
                    Veterinario veterinario = new Veterinario();
                    veterinario.setIdUsuario(rs.getInt("id_veterinario"));
                    horario.setVeterinario(veterinario);
                    horario.setDia(Dia.valueOf(rs.getString("dia")));
                    horario.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                    horario.setHoraFin(rs.getTime("hora_fin").toLocalTime());
                }
            }
            return horario;
        } catch (Exception ex) {
            System.out.println("ERROR AL OBTENER HORARIO DE ATENCION POR ID: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<HorarioAtencion> listarTodos() {
        List<HorarioAtencion> horarios = new ArrayList<>();
        String sql = "SELECT id_horario, id_veterinario, dia, hora_inicio, hora_fin FROM HORARIO_ATENCION";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                HorarioAtencion horario = new HorarioAtencion();
                horario.setIdHorario(rs.getInt("id_horario"));
                Veterinario veterinario = new Veterinario();
                veterinario.setIdUsuario(rs.getInt("id_veterinario"));
                horario.setVeterinario(veterinario);
                horario.setDia(Dia.valueOf(rs.getString("dia")));
                horario.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                horario.setHoraFin(rs.getTime("hora_fin").toLocalTime());
                horarios.add(horario);
            }
            return horarios;
        } catch (Exception ex) {
            System.out.println("ERROR AL LISTAR HORARIOS DE ATENCION: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
