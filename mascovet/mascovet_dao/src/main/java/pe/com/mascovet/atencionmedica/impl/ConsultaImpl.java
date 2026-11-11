package pe.com.mascovet.atencionmedica.impl;

import pe.com.mascovet.atencionmedica.dao.ConsultaDAO;
import pe.com.mascovet.atencionmedica.model.Consulta;
import pe.com.mascovet.cita.model.Cita;
import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.mascota.model.Mascota;
import pe.com.mascovet.receta.model.Receta;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class ConsultaImpl implements ConsultaDAO {

    @Override
    public int insertar(Consulta consulta) {
        String sql = "{call INSERTAR_CONSULTA(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_id_atencion", Types.INTEGER);
                cs.setString("_observaciones", consulta.getObservaciones());
                if (consulta.getHoraInicio() == null) {
                    cs.setNull("_hora_inicio", Types.TIME);
                } else {
                    cs.setTime("_hora_inicio", Time.valueOf(consulta.getHoraInicio()));
                }
                if (consulta.getHoraFin() == null) {
                    cs.setNull("_hora_fin", Types.TIME);
                } else {
                    cs.setTime("_hora_fin", Time.valueOf(consulta.getHoraFin()));
                }
                cs.setInt("_id_cita", consulta.getCita().getIdCita());
                cs.setDouble("_peso_actual", consulta.getPesoActual());
                cs.setString("_alergias", consulta.getAlergias());
                cs.setString("_motivoconsulta", consulta.getMotivoConsulta());
                cs.setString("_diagnostico", consulta.getDiagnostico());
                cs.setString("_tratamiento", consulta.getTratamiento());
                cs.executeUpdate();
                int idGenerado = cs.getInt("_id_atencion");
                consulta.setIdAtencion(idGenerado);
                return idGenerado;
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR CONSULTA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Consulta consulta) {
        String sql = "{call MODIFICAR_CONSULTA(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_resultado", Types.INTEGER);
                cs.setString("_observaciones", consulta.getObservaciones());
                if (consulta.getHoraInicio() == null) {
                    cs.setNull("_hora_inicio", Types.TIME);
                } else {
                    cs.setTime("_hora_inicio", Time.valueOf(consulta.getHoraInicio()));
                }
                if (consulta.getHoraFin() == null) {
                    cs.setNull("_hora_fin", Types.TIME);
                } else {
                    cs.setTime("_hora_fin", Time.valueOf(consulta.getHoraFin()));
                }
                cs.setInt("_id_cita", consulta.getCita().getIdCita());
                cs.setDouble("_peso_actual", consulta.getPesoActual());
                cs.setString("_alergias", consulta.getAlergias());
                cs.setInt("_id_atencion", consulta.getIdAtencion());
                cs.setString("_motivoconsulta", consulta.getMotivoConsulta());
                cs.setString("_diagnostico", consulta.getDiagnostico());
                cs.setString("_tratamiento", consulta.getTratamiento());
                cs.executeUpdate();
                return cs.getInt("_resultado");
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR CONSULTA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idConsulta) {
        String sql = "{call ELIMINAR_CONSULTA(?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_resultado", Types.INTEGER);
                cs.setInt("_id_atencion", idConsulta);
                cs.executeUpdate();
                return cs.getInt("_resultado");
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL ELIMINAR CONSULTA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Consulta obtenerPorId(int idConsulta) {
        Consulta consulta = null;
        String sql = "{call LISTAR_CONSULTA_X_ID(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt("_id_atencion", idConsulta);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    consulta = new Consulta();
                    consulta.setIdAtencion(rs.getInt("id_atencion"));
                    consulta.setObservaciones(rs.getString("observaciones"));
                    Time horaInicio = rs.getTime("hora_inicio");
                    if (horaInicio != null) consulta.setHoraInicio(horaInicio.toLocalTime());
                    Time horaFin = rs.getTime("hora_fin");
                    if (horaFin != null) consulta.setHoraFin(horaFin.toLocalTime());

                    Cita cita = new Cita();
                    cita.setIdCita(rs.getInt("id_cita"));
                    consulta.setCita(cita);
                    consulta.setPesoActual(rs.getDouble("peso_actual"));
                    consulta.setAlergias(rs.getString("alergias"));
                    consulta.setMotivoConsulta(rs.getString("motivoConsulta"));
                    consulta.setDiagnostico(rs.getString("diagnostico"));
                    consulta.setTratamiento(rs.getString("tratamiento"));
                    int idReceta = rs.getInt("id_receta");
                    if (!rs.wasNull()) {
                        Receta receta = new Receta();
                        receta.setIdReceta(idReceta);
                        consulta.setReceta(receta);
                    }
                }
            }
            return consulta;
        } catch (Exception ex) {
            System.out.println("ERROR AL OBTENER CONSULTA POR ID: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Consulta> listarTodos() {
        List<Consulta> consultas = new ArrayList<>();
        String sql = "{call LISTAR_CONSULTA_TODOS()}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                Consulta consulta = new Consulta();
                consulta.setIdAtencion(rs.getInt("id_atencion"));
                consulta.setObservaciones(rs.getString("observaciones"));
                Time horaInicio = rs.getTime("hora_inicio");
                if (horaInicio != null) consulta.setHoraInicio(horaInicio.toLocalTime());
                Time horaFin = rs.getTime("hora_fin");
                if (horaFin != null) consulta.setHoraFin(horaFin.toLocalTime());

                Cita cita = new Cita();
                cita.setIdCita(rs.getInt("id_cita"));
                consulta.setCita(cita);
                consulta.setPesoActual(rs.getDouble("peso_actual"));
                consulta.setAlergias(rs.getString("alergias"));
                consulta.setMotivoConsulta(rs.getString("motivoConsulta"));
                consulta.setDiagnostico(rs.getString("diagnostico"));
                consulta.setTratamiento(rs.getString("tratamiento"));
                int idReceta = rs.getInt("id_receta");
                if (!rs.wasNull()) {
                    Receta receta = new Receta();
                    receta.setIdReceta(idReceta);
                    consulta.setReceta(receta);
                }
                consultas.add(consulta);
            }
            return consultas;
        } catch (Exception ex) {
            System.out.println("ERROR AL LISTAR CONSULTAS: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
