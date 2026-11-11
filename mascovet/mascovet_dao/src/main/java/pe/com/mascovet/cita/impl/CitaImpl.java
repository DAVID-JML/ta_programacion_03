package pe.com.mascovet.cita.impl;

import pe.com.mascovet.cita.dao.CitaDAO;
import pe.com.mascovet.cita.model.Cita;
import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.enums.model.EstadoCita;
import pe.com.mascovet.enums.model.TipoAtencion;
import pe.com.mascovet.mascota.model.Mascota;
import pe.com.mascovet.usuario.model.Cliente;
import pe.com.mascovet.usuario.model.Recepcionista;
import pe.com.mascovet.usuario.model.Veterinario;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CitaImpl implements CitaDAO {

    @Override
    public int insertar(Cita cita) {
        String sql = "{call INSERTAR_CITA(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_id_cita", Types.INTEGER);
                cs.setInt("_id_mascota", cita.getMascota().getIdMascota());
                cs.setString("_tipo_atencion", cita.getTipoAtencion().name());
                cs.setDate("_fecha", new java.sql.Date(cita.getFecha().getTime()));
                cs.setTime("_hora", java.sql.Time.valueOf(cita.getHora()));
                cs.setString("_estado", cita.getEstado().name());
                cs.setString("_motivo_cancelacion", cita.getMotivoCancelacion());

                if (cita.getRecepcionista() == null) {
                    cs.setNull("_id_recepcionista", Types.INTEGER);
                } else {
                    cs.setInt("_id_recepcionista", cita.getRecepcionista().getIdUsuario());
                }

                if (cita.getVeterinario() == null) {
                    cs.setNull("_id_veterinario", Types.INTEGER);
                } else {
                    cs.setInt("_id_veterinario", cita.getVeterinario().getIdUsuario());
                }

                cs.setBoolean("_activo", cita.isActivo());
                cs.executeUpdate();
                if (cs.getInt("_id_cita") > 0) {
                        cita.setIdCita(cs.getInt("_id_cita"));
                        return cita.getIdCita();
                    }
                return 0;
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR CITA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Cita cita) {
        String sql = "{call MODIFICAR_CITA(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_resultado", Types.INTEGER);
                cs.setInt("_id_mascota", cita.getMascota().getIdMascota());
                cs.setString("_tipo_atencion", cita.getTipoAtencion().name());
                cs.setDate("_fecha", new java.sql.Date(cita.getFecha().getTime()));
                cs.setTime("_hora", java.sql.Time.valueOf(cita.getHora()));
                cs.setString("_estado", cita.getEstado().name());
                cs.setString("_motivo_cancelacion", cita.getMotivoCancelacion());

                if (cita.getRecepcionista() == null) {
                    cs.setNull("_id_recepcionista", Types.INTEGER);
                } else {
                    cs.setInt("_id_recepcionista", cita.getRecepcionista().getIdUsuario());
                }

                if (cita.getVeterinario() == null) {
                    cs.setNull("_id_veterinario", Types.INTEGER);
                } else {
                    cs.setInt("_id_veterinario", cita.getVeterinario().getIdUsuario());
                }

                cs.setBoolean("_activo", cita.isActivo());
                cs.setInt("_id_cita", cita.getIdCita());
                cs.executeUpdate();
                return cs.getInt("_resultado");
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR CITA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idCita) {
        String sql = "{call ELIMINAR_CITA(?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter("_resultado", Types.INTEGER);
            cs.setInt("_id_cita", idCita);
            cs.executeUpdate();
            return cs.getInt("_resultado");
        } catch (Exception ex) {
            System.out.println("ERROR AL ELIMINAR CITA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Cita obtenerPorId(int idCita) {
        Cita cita = null;
        String sql = "{call LISTAR_CITA_X_ID(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt("_id_cita", idCita);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    cita = new Cita();
                    cita.setIdCita(rs.getInt("id_cita"));

                    Mascota mascota = new Mascota();
                    mascota.setIdMascota(rs.getInt("id_mascota"));
                    mascota.setActivo(rs.getBoolean("mascota_activa"));
                    Cliente cliente = new Cliente();
                    cliente.setIdUsuario(rs.getInt("id_usuario_cliente"));
                    cliente.setActivo(rs.getBoolean("cliente_activo"));
                    mascota.setCliente(cliente);
                    cita.setMascota(mascota);

                    cita.setTipoAtencion(TipoAtencion.valueOf(rs.getString("tipo_atencion")));
                    cita.setFecha(rs.getDate("fecha"));
                    cita.setHora(rs.getTime("hora").toLocalTime());
                    cita.setEstado(EstadoCita.valueOf(rs.getString("estado")));
                    cita.setMotivoCancelacion(rs.getString("motivo_cancelacion"));
                    cita.setActivo(rs.getBoolean("activo"));

                    int idRecepcionista = rs.getInt("id_recepcionista");
                    if (!rs.wasNull()) {
                        Recepcionista recepcionista = new Recepcionista();
                        recepcionista.setIdUsuario(idRecepcionista);
                        cita.setRecepcionista(recepcionista);
                    }

                    int idVeterinario = rs.getInt("id_veterinario");
                    if (!rs.wasNull()) {
                        Veterinario veterinario = new Veterinario();
                        veterinario.setIdUsuario(idVeterinario);
                        cita.setVeterinario(veterinario);
                    }
                }
            }
            return cita;
        } catch (Exception ex) {
            System.out.println("ERROR AL OBTENER CITA POR ID: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Cita> listarTodos() {
        List<Cita> citas = new ArrayList<>();
        String sql = "{call LISTAR_CITA_TODOS()}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                Cita cita = null;
                        cita = new Cita();
                        cita.setIdCita(rs.getInt("id_cita"));

                        Mascota mascota = new Mascota();
                        mascota.setIdMascota(rs.getInt("id_mascota"));
                        mascota.setActivo(rs.getBoolean("mascota_activa"));
                        Cliente cliente = new Cliente();
                        cliente.setIdUsuario(rs.getInt("id_usuario_cliente"));
                        cliente.setActivo(rs.getBoolean("cliente_activo"));
                        mascota.setCliente(cliente);
                        cita.setMascota(mascota);

                        cita.setTipoAtencion(TipoAtencion.valueOf(rs.getString("tipo_atencion")));
                        cita.setFecha(rs.getDate("fecha"));
                        cita.setHora(rs.getTime("hora").toLocalTime());
                        cita.setEstado(EstadoCita.valueOf(rs.getString("estado")));
                        cita.setMotivoCancelacion(rs.getString("motivo_cancelacion"));
                        cita.setActivo(rs.getBoolean("activo"));

                        int idRecepcionista = rs.getInt("id_recepcionista");
                        if (!rs.wasNull()) {
                            Recepcionista recepcionista = new Recepcionista();
                            recepcionista.setIdUsuario(idRecepcionista);
                            cita.setRecepcionista(recepcionista);
                        }

                        int idVeterinario = rs.getInt("id_veterinario");
                        if (!rs.wasNull()) {
                            Veterinario veterinario = new Veterinario();
                            veterinario.setIdUsuario(idVeterinario);
                            cita.setVeterinario(veterinario);
                        }
                citas.add(cita);
            }
            return citas;
        } catch (Exception ex) {
            System.out.println("ERROR AL LISTAR CITAS: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
