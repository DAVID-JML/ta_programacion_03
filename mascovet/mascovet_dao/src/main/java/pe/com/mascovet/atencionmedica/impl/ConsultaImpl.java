package pe.com.mascovet.atencionmedica.impl;

import pe.com.mascovet.atencionmedica.dao.ConsultaDAO;
import pe.com.mascovet.atencionmedica.model.Consulta;
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

public class ConsultaImpl implements ConsultaDAO {

    @Override
    public int insertar(Consulta consulta) {
        String sqlAtencion = "INSERT INTO ATENCION_MEDICA (observaciones, hora_inicio, hora_fin, id_mascota, id_cita, peso_actual, alergias) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sql = "INSERT INTO CONSULTA (id_atencion, motivoConsulta, diagnostico, tratamiento) VALUES (?, ?, ?, ?)";
        try {
            Connection con = TransactionContext.getConnection();
            try (PreparedStatement psAtencion = con.prepareStatement(sqlAtencion, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement ps = con.prepareStatement(sql)) {
                psAtencion.setString(1, consulta.getObservaciones());
                if (consulta.getHoraInicio() == null) {
                    psAtencion.setNull(2, Types.TIME);
                } else {
                    psAtencion.setTime(2, Time.valueOf(consulta.getHoraInicio()));
                }
                if (consulta.getHoraFin() == null) {
                    psAtencion.setNull(3, Types.TIME);
                } else {
                    psAtencion.setTime(3, Time.valueOf(consulta.getHoraFin()));
                }
                psAtencion.setInt(4, consulta.getMascota().getIdMascota());
                psAtencion.setInt(5, consulta.getCita().getIdCita());
                psAtencion.setDouble(6, consulta.getPesoActual());
                psAtencion.setString(7, consulta.getAlergias());
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
                consulta.setIdAtencion(idAtencion);

                ps.setInt(1, idAtencion);
                ps.setString(2, consulta.getMotivoConsulta());
                ps.setString(3, consulta.getDiagnostico());
                ps.setString(4, consulta.getTratamiento());
                ps.executeUpdate();
                return consulta.getIdAtencion();
            } catch (Exception ex) {
                System.out.println("ERROR AL INSERTAR CONSULTA: " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR CONSULTA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Consulta consulta) {
        String sqlAtencion = "UPDATE ATENCION_MEDICA SET observaciones=?, hora_inicio=?, hora_fin=?, id_mascota=?, id_cita=?, peso_actual=?, alergias=? " +
                "WHERE id_atencion=?";
        String sql = "UPDATE CONSULTA SET motivoConsulta=?, diagnostico=?, tratamiento=? WHERE id_atencion=?";
        try {
            Connection con = TransactionContext.getConnection();
            try (PreparedStatement psAtencion = con.prepareStatement(sqlAtencion);
                 PreparedStatement ps = con.prepareStatement(sql)) {
                psAtencion.setString(1, consulta.getObservaciones());
                if (consulta.getHoraInicio() == null) {
                    psAtencion.setNull(2, Types.TIME);
                } else {
                    psAtencion.setTime(2, Time.valueOf(consulta.getHoraInicio()));
                }
                if (consulta.getHoraFin() == null) {
                    psAtencion.setNull(3, Types.TIME);
                } else {
                    psAtencion.setTime(3, Time.valueOf(consulta.getHoraFin()));
                }
                psAtencion.setInt(4, consulta.getMascota().getIdMascota());
                psAtencion.setInt(5, consulta.getCita().getIdCita());
                psAtencion.setDouble(6, consulta.getPesoActual());
                psAtencion.setString(7, consulta.getAlergias());
                psAtencion.setInt(8, consulta.getIdAtencion());
                int filasBase = psAtencion.executeUpdate();

                ps.setString(1, consulta.getMotivoConsulta());
                ps.setString(2, consulta.getDiagnostico());
                ps.setString(3, consulta.getTratamiento());
                ps.setInt(4, consulta.getIdAtencion());
                int filasHija = ps.executeUpdate();
                return (filasBase > 0 && filasHija > 0) ? 1 : 0;
            } catch (Exception ex) {
                System.out.println("ERROR AL MODIFICAR CONSULTA: " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR CONSULTA: " + ex.getMessage());
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
            System.out.println("ERROR AL ELIMINAR CONSULTA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Consulta obtenerPorId(int idConsulta) {
        Consulta consulta = null;
        String sql = "SELECT a.id_atencion, a.observaciones, a.hora_inicio, a.hora_fin, a.id_mascota, a.id_cita, a.peso_actual, a.alergias, " +
                "c.motivoConsulta, c.diagnostico, c.tratamiento, r.id_receta " +
                "FROM ATENCION_MEDICA a INNER JOIN CONSULTA c ON a.id_atencion=c.id_atencion " +
                "LEFT JOIN RECETA r ON a.id_atencion=r.id_atencion WHERE a.id_atencion=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idConsulta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    consulta = new Consulta();
                    consulta.setIdAtencion(rs.getInt("id_atencion"));
                    consulta.setObservaciones(rs.getString("observaciones"));
                    Time horaInicio = rs.getTime("hora_inicio");
                    if (horaInicio != null) consulta.setHoraInicio(horaInicio.toLocalTime());
                    Time horaFin = rs.getTime("hora_fin");
                    if (horaFin != null) consulta.setHoraFin(horaFin.toLocalTime());
                    Mascota mascota = new Mascota();
                    mascota.setIdMascota(rs.getInt("id_mascota"));
                    consulta.setMascota(mascota);
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
        String sql = "SELECT a.id_atencion, a.observaciones, a.hora_inicio, a.hora_fin, a.id_mascota, a.id_cita, a.peso_actual, a.alergias, " +
                "c.motivoConsulta, c.diagnostico, c.tratamiento, r.id_receta " +
                "FROM ATENCION_MEDICA a INNER JOIN CONSULTA c ON a.id_atencion=c.id_atencion " +
                "LEFT JOIN RECETA r ON a.id_atencion=r.id_atencion";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Consulta consulta = new Consulta();
                consulta.setIdAtencion(rs.getInt("id_atencion"));
                consulta.setObservaciones(rs.getString("observaciones"));
                Time horaInicio = rs.getTime("hora_inicio");
                if (horaInicio != null) consulta.setHoraInicio(horaInicio.toLocalTime());
                Time horaFin = rs.getTime("hora_fin");
                if (horaFin != null) consulta.setHoraFin(horaFin.toLocalTime());
                Mascota mascota = new Mascota();
                mascota.setIdMascota(rs.getInt("id_mascota"));
                consulta.setMascota(mascota);
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
