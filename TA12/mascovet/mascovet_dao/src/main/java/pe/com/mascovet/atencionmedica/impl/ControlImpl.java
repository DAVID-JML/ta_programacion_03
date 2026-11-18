package pe.com.mascovet.atencionmedica.impl;

import pe.com.mascovet.atencionmedica.dao.ControlDAO;
import pe.com.mascovet.atencionmedica.model.Control;
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

public class ControlImpl implements ControlDAO {

    @Override
    public int insertar(Control control) {
        String sql = "{call INSERTAR_CONTROL(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_id_atencion", Types.INTEGER);
                cs.setString("_observaciones", control.getObservaciones());
                if (control.getHoraInicio() == null) {
                    cs.setNull("_hora_inicio", Types.TIME);
                } else {
                    cs.setTime("_hora_inicio", Time.valueOf(control.getHoraInicio()));
                }
                if (control.getHoraFin() == null) {
                    cs.setNull("_hora_fin", Types.TIME);
                } else {
                    cs.setTime("_hora_fin", Time.valueOf(control.getHoraFin()));
                }
                cs.setInt("_id_cita", control.getCita().getIdCita());
                cs.setDouble("_peso_actual", control.getPesoActual());
                cs.setString("_alergias", control.getAlergias());
                cs.setString("_evolucion", control.getEvolucion());
                cs.setString("_indicaciones", control.getIndicaciones());
                cs.executeUpdate();
                int idGenerado = cs.getInt("_id_atencion");
                control.setIdAtencion(idGenerado);
                return idGenerado;
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR CONTROL: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Control control) {
        String sql = "{call MODIFICAR_CONTROL(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_resultado", Types.INTEGER);
                cs.setString("_observaciones", control.getObservaciones());
                if (control.getHoraInicio() == null) {
                    cs.setNull("_hora_inicio", Types.TIME);
                } else {
                    cs.setTime("_hora_inicio", Time.valueOf(control.getHoraInicio()));
                }
                if (control.getHoraFin() == null) {
                    cs.setNull("_hora_fin", Types.TIME);
                } else {
                    cs.setTime("_hora_fin", Time.valueOf(control.getHoraFin()));
                }
                cs.setInt("_id_cita", control.getCita().getIdCita());
                cs.setDouble("_peso_actual", control.getPesoActual());
                cs.setString("_alergias", control.getAlergias());
                cs.setInt("_id_atencion", control.getIdAtencion());
                cs.setString("_evolucion", control.getEvolucion());
                cs.setString("_indicaciones", control.getIndicaciones());
                cs.executeUpdate();
                return cs.getInt("_resultado");
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR CONTROL: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idControl) {
        String sql = "{call ELIMINAR_CONTROL(?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_resultado", Types.INTEGER);
                cs.setInt("_id_atencion", idControl);
                cs.executeUpdate();
                return cs.getInt("_resultado");
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL ELIMINAR CONTROL: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Control obtenerPorId(int idControl) {
        Control control = null;
        String sql = "{call LISTAR_CONTROL_X_ID(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt("_id_atencion", idControl);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    control = new Control();
                    control.setIdAtencion(rs.getInt("id_atencion"));
                    control.setObservaciones(rs.getString("observaciones"));
                    Time horaInicio = rs.getTime("hora_inicio");
                    if (horaInicio != null) control.setHoraInicio(horaInicio.toLocalTime());
                    Time horaFin = rs.getTime("hora_fin");
                    if (horaFin != null) control.setHoraFin(horaFin.toLocalTime());

                    Cita cita = new Cita();
                    cita.setIdCita(rs.getInt("id_cita"));
                    control.setCita(cita);
                    control.setPesoActual(rs.getDouble("peso_actual"));
                    control.setAlergias(rs.getString("alergias"));
                    control.setEvolucion(rs.getString("evolucion"));
                    control.setIndicaciones(rs.getString("indicaciones"));
                    int idReceta = rs.getInt("id_receta");
                    if (!rs.wasNull()) {
                        Receta receta = new Receta();
                        receta.setIdReceta(idReceta);
                        control.setReceta(receta);
                    }
                }
            }
            return control;
        } catch (Exception ex) {
            System.out.println("ERROR AL OBTENER CONTROL POR ID: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Control> listarTodos() {
        List<Control> controles = new ArrayList<>();
        String sql = "{call LISTAR_CONTROL_TODOS()}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                Control control = new Control();
                control.setIdAtencion(rs.getInt("id_atencion"));
                control.setObservaciones(rs.getString("observaciones"));
                Time horaInicio = rs.getTime("hora_inicio");
                if (horaInicio != null) control.setHoraInicio(horaInicio.toLocalTime());
                Time horaFin = rs.getTime("hora_fin");
                if (horaFin != null) control.setHoraFin(horaFin.toLocalTime());

                Cita cita = new Cita();
                cita.setIdCita(rs.getInt("id_cita"));
                control.setCita(cita);
                control.setPesoActual(rs.getDouble("peso_actual"));
                control.setAlergias(rs.getString("alergias"));
                control.setEvolucion(rs.getString("evolucion"));
                control.setIndicaciones(rs.getString("indicaciones"));
                int idReceta = rs.getInt("id_receta");
                if (!rs.wasNull()) {
                    Receta receta = new Receta();
                    receta.setIdReceta(idReceta);
                    control.setReceta(receta);
                }
                controles.add(control);
            }
            return controles;
        } catch (Exception ex) {
            System.out.println("ERROR AL LISTAR CONTROLES: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
