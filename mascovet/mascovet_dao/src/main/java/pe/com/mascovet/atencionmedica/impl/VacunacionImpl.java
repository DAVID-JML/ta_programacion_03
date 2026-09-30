package pe.com.mascovet.atencionmedica.impl;

import pe.com.mascovet.atencionmedica.dao.VacunacionDAO;
import pe.com.mascovet.atencionmedica.model.Vacunacion;
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

public class VacunacionImpl implements VacunacionDAO {

    @Override
    public int insertar(Vacunacion vacunacion) {
        String sqlAtencion = "INSERT INTO ATENCION_MEDICA (observaciones, hora_inicio, hora_fin, id_mascota, id_cita, peso_actual, alergias) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sql = "INSERT INTO VACUNACION (id_atencion, fechaAplicacion, fechaProximaDosis, dosis) VALUES (?, ?, ?, ?)";
        try {
            Connection con = TransactionContext.getConnection();
            try (PreparedStatement psAtencion = con.prepareStatement(sqlAtencion, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement ps = con.prepareStatement(sql)) {
                psAtencion.setString(1, vacunacion.getObservaciones());
                if (vacunacion.getHoraInicio() == null) {
                    psAtencion.setNull(2, Types.TIME);
                } else {
                    psAtencion.setTime(2, Time.valueOf(vacunacion.getHoraInicio()));
                }
                if (vacunacion.getHoraFin() == null) {
                    psAtencion.setNull(3, Types.TIME);
                } else {
                    psAtencion.setTime(3, Time.valueOf(vacunacion.getHoraFin()));
                }
                psAtencion.setInt(4, vacunacion.getMascota().getIdMascota());
                psAtencion.setInt(5, vacunacion.getCita().getIdCita());
                psAtencion.setDouble(6, vacunacion.getPesoActual());
                psAtencion.setString(7, vacunacion.getAlergias());
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
                vacunacion.setIdAtencion(idAtencion);

                ps.setInt(1, idAtencion);
                ps.setDate(2, new java.sql.Date(vacunacion.getFechaAplicacion().getTime()));
                ps.setDate(3, new java.sql.Date(vacunacion.getFechaProximaDosis().getTime()));
                ps.setString(4, vacunacion.getDosis());
                ps.executeUpdate();
                return vacunacion.getIdAtencion();
            } catch (Exception ex) {
                System.out.println("ERROR AL INSERTAR VACUNACION: " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR VACUNACION: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Vacunacion vacunacion) {
        String sqlAtencion = "UPDATE ATENCION_MEDICA SET observaciones=?, hora_inicio=?, hora_fin=?, id_mascota=?, id_cita=?, peso_actual=?, alergias=? " +
                "WHERE id_atencion=?";
        String sql = "UPDATE VACUNACION SET fechaAplicacion=?, fechaProximaDosis=?, dosis=? WHERE id_atencion=?";
        try {
            Connection con = TransactionContext.getConnection();
            try (PreparedStatement psAtencion = con.prepareStatement(sqlAtencion);
                 PreparedStatement ps = con.prepareStatement(sql)) {
                psAtencion.setString(1, vacunacion.getObservaciones());
                if (vacunacion.getHoraInicio() == null) {
                    psAtencion.setNull(2, Types.TIME);
                } else {
                    psAtencion.setTime(2, Time.valueOf(vacunacion.getHoraInicio()));
                }
                if (vacunacion.getHoraFin() == null) {
                    psAtencion.setNull(3, Types.TIME);
                } else {
                    psAtencion.setTime(3, Time.valueOf(vacunacion.getHoraFin()));
                }
                psAtencion.setInt(4, vacunacion.getMascota().getIdMascota());
                psAtencion.setInt(5, vacunacion.getCita().getIdCita());
                psAtencion.setDouble(6, vacunacion.getPesoActual());
                psAtencion.setString(7, vacunacion.getAlergias());
                psAtencion.setInt(8, vacunacion.getIdAtencion());
                int filasBase = psAtencion.executeUpdate();

                ps.setDate(1, new java.sql.Date(vacunacion.getFechaAplicacion().getTime()));
                ps.setDate(2, new java.sql.Date(vacunacion.getFechaProximaDosis().getTime()));
                ps.setString(3, vacunacion.getDosis());
                ps.setInt(4, vacunacion.getIdAtencion());
                int filasHija = ps.executeUpdate();
                return (filasBase > 0 && filasHija > 0) ? 1 : 0;
            } catch (Exception ex) {
                System.out.println("ERROR AL MODIFICAR VACUNACION: " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR VACUNACION: " + ex.getMessage());
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
            System.out.println("ERROR AL ELIMINAR VACUNACION: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Vacunacion obtenerPorId(int idVacunacion) {
        Vacunacion vacunacion = null;
        String sql = "SELECT a.id_atencion, a.observaciones, a.hora_inicio, a.hora_fin, a.id_mascota, a.id_cita, a.peso_actual, a.alergias, " +
                "v.fechaAplicacion, v.fechaProximaDosis, v.dosis, r.id_receta " +
                "FROM ATENCION_MEDICA a INNER JOIN VACUNACION v ON a.id_atencion=v.id_atencion " +
                "LEFT JOIN RECETA r ON a.id_atencion=r.id_atencion WHERE a.id_atencion=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVacunacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    vacunacion = new Vacunacion();
                    vacunacion.setIdAtencion(rs.getInt("id_atencion"));
                    vacunacion.setObservaciones(rs.getString("observaciones"));
                    Time horaInicio = rs.getTime("hora_inicio");
                    if (horaInicio != null) vacunacion.setHoraInicio(horaInicio.toLocalTime());
                    Time horaFin = rs.getTime("hora_fin");
                    if (horaFin != null) vacunacion.setHoraFin(horaFin.toLocalTime());
                    Mascota mascota = new Mascota();
                    mascota.setIdMascota(rs.getInt("id_mascota"));
                    vacunacion.setMascota(mascota);
                    Cita cita = new Cita();
                    cita.setIdCita(rs.getInt("id_cita"));
                    vacunacion.setCita(cita);
                    vacunacion.setPesoActual(rs.getDouble("peso_actual"));
                    vacunacion.setAlergias(rs.getString("alergias"));
                    vacunacion.setFechaAplicacion(rs.getDate("fechaAplicacion"));
                    vacunacion.setFechaProximaDosis(rs.getDate("fechaProximaDosis"));
                    vacunacion.setDosis(rs.getString("dosis"));
                    int idReceta = rs.getInt("id_receta");
                    if (!rs.wasNull()) {
                        Receta receta = new Receta();
                        receta.setIdReceta(idReceta);
                        vacunacion.setReceta(receta);
                    }
                }
            }
            return vacunacion;
        } catch (Exception ex) {
            System.out.println("ERROR AL OBTENER VACUNACION POR ID: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Vacunacion> listarTodos() {
        List<Vacunacion> vacunaciones = new ArrayList<>();
        String sql = "SELECT a.id_atencion, a.observaciones, a.hora_inicio, a.hora_fin, a.id_mascota, a.id_cita, a.peso_actual, a.alergias, " +
                "v.fechaAplicacion, v.fechaProximaDosis, v.dosis, r.id_receta " +
                "FROM ATENCION_MEDICA a INNER JOIN VACUNACION v ON a.id_atencion=v.id_atencion " +
                "LEFT JOIN RECETA r ON a.id_atencion=r.id_atencion";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Vacunacion vacunacion = new Vacunacion();
                vacunacion.setIdAtencion(rs.getInt("id_atencion"));
                vacunacion.setObservaciones(rs.getString("observaciones"));
                Time horaInicio = rs.getTime("hora_inicio");
                if (horaInicio != null) vacunacion.setHoraInicio(horaInicio.toLocalTime());
                Time horaFin = rs.getTime("hora_fin");
                if (horaFin != null) vacunacion.setHoraFin(horaFin.toLocalTime());
                Mascota mascota = new Mascota();
                mascota.setIdMascota(rs.getInt("id_mascota"));
                vacunacion.setMascota(mascota);
                Cita cita = new Cita();
                cita.setIdCita(rs.getInt("id_cita"));
                vacunacion.setCita(cita);
                vacunacion.setPesoActual(rs.getDouble("peso_actual"));
                vacunacion.setAlergias(rs.getString("alergias"));
                vacunacion.setFechaAplicacion(rs.getDate("fechaAplicacion"));
                vacunacion.setFechaProximaDosis(rs.getDate("fechaProximaDosis"));
                vacunacion.setDosis(rs.getString("dosis"));
                int idReceta = rs.getInt("id_receta");
                if (!rs.wasNull()) {
                    Receta receta = new Receta();
                    receta.setIdReceta(idReceta);
                    vacunacion.setReceta(receta);
                }
                vacunaciones.add(vacunacion);
            }
            return vacunaciones;
        } catch (Exception ex) {
            System.out.println("ERROR AL LISTAR VACUNACIONES: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
