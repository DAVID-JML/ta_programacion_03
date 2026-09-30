package pe.com.mascovet.atencionmedica.impl;

import pe.com.mascovet.atencionmedica.dao.CirugiaDAO;
import pe.com.mascovet.atencionmedica.model.Cirugia;
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

public class CirugiaImpl implements CirugiaDAO {

    @Override
    public int insertar(Cirugia cirugia) {
        String sqlAtencion = "INSERT INTO ATENCION_MEDICA (observaciones, hora_inicio, hora_fin, id_mascota, id_cita, peso_actual, alergias) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sql = "INSERT INTO CIRUGIA (id_atencion, procedimiento, indicaciones_post_operatorias) VALUES (?, ?, ?)";
        try {
            Connection con = TransactionContext.getConnection();
            try (PreparedStatement psAtencion = con.prepareStatement(sqlAtencion, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement ps = con.prepareStatement(sql)) {
                psAtencion.setString(1, cirugia.getObservaciones());
                if (cirugia.getHoraInicio() == null) {
                    psAtencion.setNull(2, Types.TIME);
                } else {
                    psAtencion.setTime(2, Time.valueOf(cirugia.getHoraInicio()));
                }
                if (cirugia.getHoraFin() == null) {
                    psAtencion.setNull(3, Types.TIME);
                } else {
                    psAtencion.setTime(3, Time.valueOf(cirugia.getHoraFin()));
                }
                psAtencion.setInt(4, cirugia.getMascota().getIdMascota());
                psAtencion.setInt(5, cirugia.getCita().getIdCita());
                psAtencion.setDouble(6, cirugia.getPesoActual());
                psAtencion.setString(7, cirugia.getAlergias());
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
                cirugia.setIdAtencion(idAtencion);

                ps.setInt(1, idAtencion);
                ps.setString(2, cirugia.getProcedimiento());
                ps.setString(3, cirugia.getIndicacionesPostOperatorias());
                ps.executeUpdate();
                return cirugia.getIdAtencion();
            } catch (Exception ex) {
                System.out.println("ERROR AL INSERTAR CIRUGIA: " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR CIRUGIA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Cirugia cirugia) {
        String sqlAtencion = "UPDATE ATENCION_MEDICA SET observaciones=?, hora_inicio=?, hora_fin=?, id_mascota=?, id_cita=?, peso_actual=?, alergias=? " +
                "WHERE id_atencion=?";
        String sql = "UPDATE CIRUGIA SET procedimiento=?, indicaciones_post_operatorias=? WHERE id_atencion=?";
        try {
            Connection con = TransactionContext.getConnection();
            try (PreparedStatement psAtencion = con.prepareStatement(sqlAtencion);
                 PreparedStatement ps = con.prepareStatement(sql)) {
                psAtencion.setString(1, cirugia.getObservaciones());
                if (cirugia.getHoraInicio() == null) {
                    psAtencion.setNull(2, Types.TIME);
                } else {
                    psAtencion.setTime(2, Time.valueOf(cirugia.getHoraInicio()));
                }
                if (cirugia.getHoraFin() == null) {
                    psAtencion.setNull(3, Types.TIME);
                } else {
                    psAtencion.setTime(3, Time.valueOf(cirugia.getHoraFin()));
                }
                psAtencion.setInt(4, cirugia.getMascota().getIdMascota());
                psAtencion.setInt(5, cirugia.getCita().getIdCita());
                psAtencion.setDouble(6, cirugia.getPesoActual());
                psAtencion.setString(7, cirugia.getAlergias());
                psAtencion.setInt(8, cirugia.getIdAtencion());
                int filasBase = psAtencion.executeUpdate();

                ps.setString(1, cirugia.getProcedimiento());
                ps.setString(2, cirugia.getIndicacionesPostOperatorias());
                ps.setInt(3, cirugia.getIdAtencion());
                int filasHija = ps.executeUpdate();
                return (filasBase > 0 && filasHija > 0) ? 1 : 0;
            } catch (Exception ex) {
                System.out.println("ERROR AL MODIFICAR CIRUGIA: " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR CIRUGIA: " + ex.getMessage());
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
            System.out.println("ERROR AL ELIMINAR CIRUGIA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Cirugia obtenerPorId(int idCirugia) {
        Cirugia cirugia = null;
        String sql = "SELECT a.id_atencion, a.observaciones, a.hora_inicio, a.hora_fin, a.id_mascota, a.id_cita, a.peso_actual, a.alergias, " +
                "c.procedimiento, c.indicaciones_post_operatorias, r.id_receta " +
                "FROM ATENCION_MEDICA a INNER JOIN CIRUGIA c ON a.id_atencion=c.id_atencion " +
                "LEFT JOIN RECETA r ON a.id_atencion=r.id_atencion WHERE a.id_atencion=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCirugia);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cirugia = new Cirugia();
                    cirugia.setIdAtencion(rs.getInt("id_atencion"));
                    cirugia.setObservaciones(rs.getString("observaciones"));
                    Time horaInicio = rs.getTime("hora_inicio");
                    if (horaInicio != null) cirugia.setHoraInicio(horaInicio.toLocalTime());
                    Time horaFin = rs.getTime("hora_fin");
                    if (horaFin != null) cirugia.setHoraFin(horaFin.toLocalTime());
                    Mascota mascota = new Mascota();
                    mascota.setIdMascota(rs.getInt("id_mascota"));
                    cirugia.setMascota(mascota);
                    Cita cita = new Cita();
                    cita.setIdCita(rs.getInt("id_cita"));
                    cirugia.setCita(cita);
                    cirugia.setPesoActual(rs.getDouble("peso_actual"));
                    cirugia.setAlergias(rs.getString("alergias"));
                    cirugia.setProcedimiento(rs.getString("procedimiento"));
                    cirugia.setIndicacionesPostOperatorias(rs.getString("indicaciones_post_operatorias"));
                    int idReceta = rs.getInt("id_receta");
                    if (!rs.wasNull()) {
                        Receta receta = new Receta();
                        receta.setIdReceta(idReceta);
                        cirugia.setReceta(receta);
                    }
                }
            }
            return cirugia;
        } catch (Exception ex) {
            System.out.println("ERROR AL OBTENER CIRUGIA POR ID: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Cirugia> listarTodos() {
        List<Cirugia> cirugias = new ArrayList<>();
        String sql = "SELECT a.id_atencion, a.observaciones, a.hora_inicio, a.hora_fin, a.id_mascota, a.id_cita, a.peso_actual, a.alergias, " +
                "c.procedimiento, c.indicaciones_post_operatorias, r.id_receta " +
                "FROM ATENCION_MEDICA a INNER JOIN CIRUGIA c ON a.id_atencion=c.id_atencion " +
                "LEFT JOIN RECETA r ON a.id_atencion=r.id_atencion";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cirugia cirugia = new Cirugia();
                cirugia.setIdAtencion(rs.getInt("id_atencion"));
                cirugia.setObservaciones(rs.getString("observaciones"));
                Time horaInicio = rs.getTime("hora_inicio");
                if (horaInicio != null) cirugia.setHoraInicio(horaInicio.toLocalTime());
                Time horaFin = rs.getTime("hora_fin");
                if (horaFin != null) cirugia.setHoraFin(horaFin.toLocalTime());
                Mascota mascota = new Mascota();
                mascota.setIdMascota(rs.getInt("id_mascota"));
                cirugia.setMascota(mascota);
                Cita cita = new Cita();
                cita.setIdCita(rs.getInt("id_cita"));
                cirugia.setCita(cita);
                cirugia.setPesoActual(rs.getDouble("peso_actual"));
                cirugia.setAlergias(rs.getString("alergias"));
                cirugia.setProcedimiento(rs.getString("procedimiento"));
                cirugia.setIndicacionesPostOperatorias(rs.getString("indicaciones_post_operatorias"));
                int idReceta = rs.getInt("id_receta");
                if (!rs.wasNull()) {
                    Receta receta = new Receta();
                    receta.setIdReceta(idReceta);
                    cirugia.setReceta(receta);
                }
                cirugias.add(cirugia);
            }
            return cirugias;
        } catch (Exception ex) {
            System.out.println("ERROR AL LISTAR CIRUGIAS: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
