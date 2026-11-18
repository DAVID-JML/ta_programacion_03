package pe.com.mascovet.cita.impl;

import pe.com.mascovet.cita.dao.HorarioAtencionDAO;
import pe.com.mascovet.cita.model.HorarioAtencion;
import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.config.TransactionContext;
import pe.com.mascovet.enums.model.Dia;
import pe.com.mascovet.usuario.model.Veterinario;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.Types;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HorarioAtencionImpl implements HorarioAtencionDAO {

    @Override
    public int insertar(HorarioAtencion horario) {
        String sql = "{call INSERTAR_HORARIOATENCION(?, ?, ?, ?, ?)}";
        Connection conexion = TransactionContext.getCurrentConnection();
        if (conexion == null)
            conexion = DBManager.getInstance().getConnection();
        try (Connection con = conexion) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_id_horario_atencion", Types.INTEGER);
                cs.setInt("_id_veterinario", horario.getVeterinario().getIdUsuario());
                cs.setString("_dia", horario.getDia().name());
                cs.setTime("_hora_inicio", java.sql.Time.valueOf(horario.getHoraInicio()));
                cs.setTime("_hora_fin", java.sql.Time.valueOf(horario.getHoraFin()));
                cs.executeUpdate();

                if (cs.getInt("_id_horario_atencion") > 0) {
                        horario.setIdHorario(cs.getInt("_id_horario_atencion"));
                        return horario.getIdHorario();
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
        String sql = "{call MODIFICAR_HORARIOATENCION(?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter("_resultado", Types.INTEGER);
            cs.setInt("_id_veterinario", horario.getVeterinario().getIdUsuario());
            cs.setString("_dia", horario.getDia().name());
            cs.setTime("_hora_inicio", java.sql.Time.valueOf(horario.getHoraInicio()));
            cs.setTime("_hora_fin", java.sql.Time.valueOf(horario.getHoraFin()));
            cs.setInt("_id_horario", horario.getIdHorario());
            cs.executeUpdate();
            return cs.getInt("_resultado");
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR HORARIO DE ATENCION: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idHorario) {
        String sql = "{call ELIMINAR_HORARIOATENCION(?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter("_resultado", Types.INTEGER);
            cs.setInt("_id_horario", idHorario);
            cs.executeUpdate();
            return cs.getInt("_resultado");
        } catch (Exception ex) {
            System.out.println("ERROR AL ELIMINAR HORARIO DE ATENCION: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public HorarioAtencion obtenerPorId(int idHorario) {
        HorarioAtencion horario = null;
        String sql = "{call LISTAR_HORARIOATENCION_X_ID(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt("_id_horario", idHorario);
            try (ResultSet rs = cs.executeQuery()) {
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
        String sql = "{call LISTAR_HORARIOATENCION_TODOS()}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
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
