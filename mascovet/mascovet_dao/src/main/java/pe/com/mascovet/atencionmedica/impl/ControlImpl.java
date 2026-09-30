package pe.com.mascovet.atencionmedica.impl;

import pe.com.mascovet.atencionmedica.dao.ControlDAO;
import pe.com.mascovet.atencionmedica.model.Control;
import pe.com.mascovet.cita.model.Cita;
import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.config.TransactionContext;
import pe.com.mascovet.mascota.model.Mascota;
import pe.com.mascovet.receta.model.Receta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class ControlImpl implements ControlDAO {

    @Override
    public int insertar(Control control) {
        String sqlAtencion = "INSERT INTO ATENCION_MEDICA (observaciones, hora_inicio, hora_fin, id_mascota, id_cita, peso_actual, alergias) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sql = "INSERT INTO CONTROL (id_atencion, evolucion, indicaciones) VALUES (?, ?, ?)";
        try {
            Connection con = TransactionContext.getConnection();
            try (PreparedStatement psAtencion = con.prepareStatement(sqlAtencion, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement ps = con.prepareStatement(sql)) {
                psAtencion.setString(1, control.getObservaciones());
                if (control.getHoraInicio() == null) {
                    psAtencion.setNull(2, Types.TIME);
                } else {
                    psAtencion.setTime(2, Time.valueOf(control.getHoraInicio()));
                }
                if (control.getHoraFin() == null) {
                    psAtencion.setNull(3, Types.TIME);
                } else {
                    psAtencion.setTime(3, Time.valueOf(control.getHoraFin()));
                }
                psAtencion.setInt(4, control.getMascota().getIdMascota());
                psAtencion.setInt(5, control.getCita().getIdCita());
                psAtencion.setDouble(6, control.getPesoActual());
                psAtencion.setString(7, control.getAlergias());
                psAtencion.executeUpdate();

                int idAtencion = 0;
                try (ResultSet rs = psAtencion.getGeneratedKeys()) {
                    if (rs.next()) {
                        idAtencion = rs.getInt(1);
                    }
                }
                if (idAtencion <= 0) {
                    throw new RuntimeException("No se pudo generar el identificador de la atencion medica");
                }
                control.setIdAtencion(idAtencion);

                ps.setInt(1, idAtencion);
                ps.setString(2, control.getEvolucion());
                ps.setString(3, control.getIndicaciones());
                ps.executeUpdate();
                return control.getIdAtencion();
            } catch (Exception ex) {
                System.out.println("ERROR AL INSERTAR CONTROL: " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR CONTROL: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Control control) {
        String sqlAtencion = "UPDATE ATENCION_MEDICA SET observaciones=?, hora_inicio=?, hora_fin=?, id_mascota=?, id_cita=?, peso_actual=?, alergias=? " +
                "WHERE id_atencion=?";
        String sql = "UPDATE CONTROL SET evolucion=?, indicaciones=? WHERE id_atencion=?";
        try {
            Connection con = TransactionContext.getConnection();
            try (PreparedStatement psAtencion = con.prepareStatement(sqlAtencion);
                 PreparedStatement ps = con.prepareStatement(sql)) {
                psAtencion.setString(1, control.getObservaciones());
                if (control.getHoraInicio() == null) {
                    psAtencion.setNull(2, Types.TIME);
                } else {
                    psAtencion.setTime(2, Time.valueOf(control.getHoraInicio()));
                }
                if (control.getHoraFin() == null) {
                    psAtencion.setNull(3, Types.TIME);
                } else {
                    psAtencion.setTime(3, Time.valueOf(control.getHoraFin()));
                }
                psAtencion.setInt(4, control.getMascota().getIdMascota());
                psAtencion.setInt(5, control.getCita().getIdCita());
                psAtencion.setDouble(6, control.getPesoActual());
                psAtencion.setString(7, control.getAlergias());
                psAtencion.setInt(8, control.getIdAtencion());
                int filasBase = psAtencion.executeUpdate();

                ps.setString(1, control.getEvolucion());
                ps.setString(2, control.getIndicaciones());
                ps.setInt(3, control.getIdAtencion());
                int filasHija = ps.executeUpdate();
                return (filasBase > 0 && filasHija > 0) ? 1 : 0;
            } catch (Exception ex) {
                System.out.println("ERROR AL MODIFICAR CONTROL: " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR CONTROL: " + ex.getMessage());
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
            System.out.println("ERROR AL ELIMINAR CONTROL: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Control obtenerPorId(int idControl) {
        Control control = null;
        String sql = "SELECT a.id_atencion, a.observaciones, a.hora_inicio, a.hora_fin, a.id_mascota, a.id_cita, a.peso_actual, a.alergias, " +
                "c.evolucion, c.indicaciones, r.id_receta " +
                "FROM ATENCION_MEDICA a INNER JOIN CONTROL c ON a.id_atencion=c.id_atencion " +
                "LEFT JOIN RECETA r ON a.id_atencion=r.id_atencion WHERE a.id_atencion=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idControl);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    control = new Control();
                    control.setIdAtencion(rs.getInt("id_atencion"));
                    control.setObservaciones(rs.getString("observaciones"));
                    Time horaInicio = rs.getTime("hora_inicio");
                    if (horaInicio != null) control.setHoraInicio(horaInicio.toLocalTime());
                    Time horaFin = rs.getTime("hora_fin");
                    if (horaFin != null) control.setHoraFin(horaFin.toLocalTime());
                    Mascota mascota = new Mascota();
                    mascota.setIdMascota(rs.getInt("id_mascota"));
                    control.setMascota(mascota);
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
        String sql = "SELECT a.id_atencion, a.observaciones, a.hora_inicio, a.hora_fin, a.id_mascota, a.id_cita, a.peso_actual, a.alergias, " +
                "c.evolucion, c.indicaciones, r.id_receta " +
                "FROM ATENCION_MEDICA a INNER JOIN CONTROL c ON a.id_atencion=c.id_atencion " +
                "LEFT JOIN RECETA r ON a.id_atencion=r.id_atencion";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Control control = new Control();
                control.setIdAtencion(rs.getInt("id_atencion"));
                control.setObservaciones(rs.getString("observaciones"));
                Time horaInicio = rs.getTime("hora_inicio");
                if (horaInicio != null) control.setHoraInicio(horaInicio.toLocalTime());
                Time horaFin = rs.getTime("hora_fin");
                if (horaFin != null) control.setHoraFin(horaFin.toLocalTime());
                Mascota mascota = new Mascota();
                mascota.setIdMascota(rs.getInt("id_mascota"));
                control.setMascota(mascota);
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
