package pe.com.mascovet.atencionmedica.impl;

import pe.com.mascovet.atencionmedica.dao.VacunacionDAO;
import pe.com.mascovet.atencionmedica.model.Vacunacion;
import pe.com.mascovet.cita.model.Cita;
import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.config.TransactionContext;
import pe.com.mascovet.mascota.model.Mascota;
import pe.com.mascovet.receta.model.Receta;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class VacunacionImpl implements VacunacionDAO {

    @Override
    public int insertar(Vacunacion vacunacion) {
        String sql = "{call INSERTAR_VACUNACION(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        Connection conexion = TransactionContext.getCurrentConnection();
        if (conexion == null)
            conexion = DBManager.getInstance().getConnection();
        try (Connection con = conexion) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_id_atencion", Types.INTEGER);
                cs.setString("_observaciones", vacunacion.getObservaciones());
                if (vacunacion.getHoraInicio() == null) {
                    cs.setNull("_hora_inicio", Types.TIME);
                } else {
                    cs.setTime("_hora_inicio", Time.valueOf(vacunacion.getHoraInicio()));
                }
                if (vacunacion.getHoraFin() == null) {
                    cs.setNull("_hora_fin", Types.TIME);
                } else {
                    cs.setTime("_hora_fin", Time.valueOf(vacunacion.getHoraFin()));
                }
                cs.setInt("_id_cita", vacunacion.getCita().getIdCita());
                cs.setDouble("_peso_actual", vacunacion.getPesoActual());
                cs.setString("_alergias", vacunacion.getAlergias());
                if (vacunacion.getFechaAplicacion() == null) {
                    cs.setNull("_fechaaplicacion", Types.DATE);
                } else {
                    cs.setDate("_fechaaplicacion", new java.sql.Date(vacunacion.getFechaAplicacion().getTime()));
                }
                if (vacunacion.getFechaProximaDosis() == null) {
                    cs.setNull("_fechaproximadosis", Types.DATE);
                } else {
                    cs.setDate("_fechaproximadosis", new java.sql.Date(vacunacion.getFechaProximaDosis().getTime()));
                }
                cs.setString("_dosis", vacunacion.getDosis());
                cs.executeUpdate();
                int idGenerado = cs.getInt("_id_atencion");
                vacunacion.setIdAtencion(idGenerado);

                for (pe.com.mascovet.atencionmedica.model.Vacuna vacuna : vacunacion.getVacunas()) {
                    if (vacuna.getIdVacuna() <= 0) {
                        throw new RuntimeException("La vacuna debe existir en el catálogo antes de asociarse");
                    }
                    try (CallableStatement csRelacion = con.prepareCall("{call ASOCIARVACUNA_VACUNACION(?, ?, ?)}")) {
                        csRelacion.registerOutParameter("_resultado", Types.INTEGER);
                        csRelacion.setInt("_id_vacunacion", idGenerado);
                        csRelacion.setInt("_id_vacuna", vacuna.getIdVacuna());
                        csRelacion.executeUpdate();
                    }
                }
                return idGenerado;
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR VACUNACION: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Vacunacion vacunacion) {
        String sql = "{call MODIFICAR_VACUNACION(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_resultado", Types.INTEGER);
                cs.setString("_observaciones", vacunacion.getObservaciones());
                if (vacunacion.getHoraInicio() == null) {
                    cs.setNull("_hora_inicio", Types.TIME);
                } else {
                    cs.setTime("_hora_inicio", Time.valueOf(vacunacion.getHoraInicio()));
                }
                if (vacunacion.getHoraFin() == null) {
                    cs.setNull("_hora_fin", Types.TIME);
                } else {
                    cs.setTime("_hora_fin", Time.valueOf(vacunacion.getHoraFin()));
                }
                cs.setInt("_id_cita", vacunacion.getCita().getIdCita());
                cs.setDouble("_peso_actual", vacunacion.getPesoActual());
                cs.setString("_alergias", vacunacion.getAlergias());
                cs.setInt("_id_atencion", vacunacion.getIdAtencion());
                if (vacunacion.getFechaAplicacion() == null) {
                    cs.setNull("_fechaaplicacion", Types.DATE);
                } else {
                    cs.setDate("_fechaaplicacion", new java.sql.Date(vacunacion.getFechaAplicacion().getTime()));
                }
                if (vacunacion.getFechaProximaDosis() == null) {
                    cs.setNull("_fechaproximadosis", Types.DATE);
                } else {
                    cs.setDate("_fechaproximadosis", new java.sql.Date(vacunacion.getFechaProximaDosis().getTime()));
                }
                cs.setString("_dosis", vacunacion.getDosis());
                cs.executeUpdate();
                return cs.getInt("_resultado");
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR VACUNACION: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idVacunacion) {
        String sql = "{call ELIMINAR_VACUNACION(?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_resultado", Types.INTEGER);
                cs.setInt("_id_atencion", idVacunacion);
                cs.executeUpdate();
                return cs.getInt("_resultado");
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL ELIMINAR VACUNACION: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Vacunacion obtenerPorId(int idVacunacion) {
        Vacunacion vacunacion = null;
        String sql = "{call LISTAR_VACUNACION_X_ID(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt("_id_atencion", idVacunacion);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    vacunacion = new Vacunacion();
                    vacunacion.setIdAtencion(rs.getInt("id_atencion"));
                    vacunacion.setObservaciones(rs.getString("observaciones"));
                    Time horaInicio = rs.getTime("hora_inicio");
                    if (horaInicio != null) vacunacion.setHoraInicio(horaInicio.toLocalTime());
                    Time horaFin = rs.getTime("hora_fin");
                    if (horaFin != null) vacunacion.setHoraFin(horaFin.toLocalTime());

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
                    try (CallableStatement csVacunas = con.prepareCall("{call CARGARVACUNAS_VACUNACION(?)}")) {
                        csVacunas.setInt("_id_vacunacion", vacunacion.getIdAtencion());
                        try (ResultSet rsVacunas = csVacunas.executeQuery()) {
                            while (rsVacunas.next()) {
                                pe.com.mascovet.atencionmedica.model.Vacuna vacuna = new pe.com.mascovet.atencionmedica.model.Vacuna();
                                vacuna.setIdVacuna(rsVacunas.getInt("id_vacuna"));
                                vacuna.setNombre(rsVacunas.getString("nombre"));
                                vacuna.setDescripcion(rsVacunas.getString("descripcion"));
                                vacunacion.agregarVacuna(vacuna);
                            }
                        }
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
        String sql = "{call LISTAR_VACUNACION_TODOS()}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                Vacunacion vacunacion = new Vacunacion();
                vacunacion.setIdAtencion(rs.getInt("id_atencion"));
                vacunacion.setObservaciones(rs.getString("observaciones"));
                Time horaInicio = rs.getTime("hora_inicio");
                if (horaInicio != null) vacunacion.setHoraInicio(horaInicio.toLocalTime());
                Time horaFin = rs.getTime("hora_fin");
                if (horaFin != null) vacunacion.setHoraFin(horaFin.toLocalTime());

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
                try (CallableStatement csVacunas = con.prepareCall("{call CARGARVACUNAS_VACUNACION(?)}")) {
                        csVacunas.setInt("_id_vacunacion", vacunacion.getIdAtencion());
                        try (ResultSet rsVacunas = csVacunas.executeQuery()) {
                            while (rsVacunas.next()) {
                                pe.com.mascovet.atencionmedica.model.Vacuna vacuna = new pe.com.mascovet.atencionmedica.model.Vacuna();
                                vacuna.setIdVacuna(rsVacunas.getInt("id_vacuna"));
                                vacuna.setNombre(rsVacunas.getString("nombre"));
                                vacuna.setDescripcion(rsVacunas.getString("descripcion"));
                                vacunacion.agregarVacuna(vacuna);
                            }
                        }
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
