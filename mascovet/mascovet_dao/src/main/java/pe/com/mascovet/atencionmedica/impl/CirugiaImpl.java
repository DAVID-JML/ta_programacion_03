package pe.com.mascovet.atencionmedica.impl;

import pe.com.mascovet.atencionmedica.dao.CirugiaDAO;
import pe.com.mascovet.atencionmedica.model.Cirugia;
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

public class CirugiaImpl implements CirugiaDAO {

    @Override
    public int insertar(Cirugia cirugia) {
        String sql = "{call INSERTAR_CIRUGIA(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_id_atencion", Types.INTEGER);
                cs.setString("_observaciones", cirugia.getObservaciones());
                if (cirugia.getHoraInicio() == null) {
                    cs.setNull("_hora_inicio", Types.TIME);
                } else {
                    cs.setTime("_hora_inicio", Time.valueOf(cirugia.getHoraInicio()));
                }
                if (cirugia.getHoraFin() == null) {
                    cs.setNull("_hora_fin", Types.TIME);
                } else {
                    cs.setTime("_hora_fin", Time.valueOf(cirugia.getHoraFin()));
                }
                cs.setInt("_id_cita", cirugia.getCita().getIdCita());
                cs.setDouble("_peso_actual", cirugia.getPesoActual());
                cs.setString("_alergias", cirugia.getAlergias());
                cs.setString("_procedimiento", cirugia.getProcedimiento());
                cs.setString("_indicaciones_post_operatorias", cirugia.getIndicacionesPostOperatorias());
                cs.executeUpdate();
                int idGenerado = cs.getInt("_id_atencion");
                cirugia.setIdAtencion(idGenerado);
                return idGenerado;
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR CIRUGIA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Cirugia cirugia) {
        String sql = "{call MODIFICAR_CIRUGIA(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_resultado", Types.INTEGER);
                cs.setString("_observaciones", cirugia.getObservaciones());
                if (cirugia.getHoraInicio() == null) {
                    cs.setNull("_hora_inicio", Types.TIME);
                } else {
                    cs.setTime("_hora_inicio", Time.valueOf(cirugia.getHoraInicio()));
                }
                if (cirugia.getHoraFin() == null) {
                    cs.setNull("_hora_fin", Types.TIME);
                } else {
                    cs.setTime("_hora_fin", Time.valueOf(cirugia.getHoraFin()));
                }
                cs.setInt("_id_cita", cirugia.getCita().getIdCita());
                cs.setDouble("_peso_actual", cirugia.getPesoActual());
                cs.setString("_alergias", cirugia.getAlergias());
                cs.setInt("_id_atencion", cirugia.getIdAtencion());
                cs.setString("_procedimiento", cirugia.getProcedimiento());
                cs.setString("_indicaciones_post_operatorias", cirugia.getIndicacionesPostOperatorias());
                cs.executeUpdate();
                return cs.getInt("_resultado");
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR CIRUGIA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idCirugia) {
        String sql = "{call ELIMINAR_CIRUGIA(?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_resultado", Types.INTEGER);
                cs.setInt("_id_atencion", idCirugia);
                cs.executeUpdate();
                return cs.getInt("_resultado");
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL ELIMINAR CIRUGIA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Cirugia obtenerPorId(int idCirugia) {
        Cirugia cirugia = null;
        String sql = "{call LISTAR_CIRUGIA_X_ID(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt("_id_atencion", idCirugia);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    cirugia = new Cirugia();
                    cirugia.setIdAtencion(rs.getInt("id_atencion"));
                    cirugia.setObservaciones(rs.getString("observaciones"));
                    Time horaInicio = rs.getTime("hora_inicio");
                    if (horaInicio != null) cirugia.setHoraInicio(horaInicio.toLocalTime());
                    Time horaFin = rs.getTime("hora_fin");
                    if (horaFin != null) cirugia.setHoraFin(horaFin.toLocalTime());

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
        String sql = "{call LISTAR_CIRUGIA_TODOS()}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                Cirugia cirugia = new Cirugia();
                cirugia.setIdAtencion(rs.getInt("id_atencion"));
                cirugia.setObservaciones(rs.getString("observaciones"));
                Time horaInicio = rs.getTime("hora_inicio");
                if (horaInicio != null) cirugia.setHoraInicio(horaInicio.toLocalTime());
                Time horaFin = rs.getTime("hora_fin");
                if (horaFin != null) cirugia.setHoraFin(horaFin.toLocalTime());

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
