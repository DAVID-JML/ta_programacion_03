package pe.com.mascovet.cita.impl;

import pe.com.mascovet.cita.dao.CitaDAO;
import pe.com.mascovet.cita.model.Cita;
import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.enums.model.EstadoCita;
import pe.com.mascovet.mascota.model.Mascota;
import pe.com.mascovet.usuario.model.Cliente;
import pe.com.mascovet.usuario.model.Recepcionista;
import pe.com.mascovet.usuario.model.Veterinario;
import pe.com.mascovet.config.TransactionContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CitaImpl implements CitaDAO {

    @Override
    public int insertar(Cita cita) {
        String sql = "INSERT INTO CITA (id_mascota, fecha, hora, estado, id_usuario, id_recepcionista, id_veterinario) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, cita.getMascota().getIdMascota());
            ps.setDate(2, new java.sql.Date(cita.getFecha().getTime()));
            ps.setTime(3, java.sql.Time.valueOf(cita.getHora()));
            ps.setString(4, cita.getEstado().name());
            ps.setInt(5, cita.getCliente().getIdUsuario());

            if (cita.getRecepcionista() == null) {
                ps.setNull(6, Types.INTEGER);
            } else {
                ps.setInt(6, cita.getRecepcionista().getIdUsuario());
            }

            if (cita.getVeterinario() == null) {
                ps.setNull(7, Types.INTEGER);
            } else {
                ps.setInt(7, cita.getVeterinario().getIdUsuario());
            }

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    cita.setIdCita(rs.getInt(1));
                    return cita.getIdCita();
                }
            }
            return 0;
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR CITA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Cita cita) {
        String sql =
                "UPDATE CITA SET "
                        + "id_mascota=?, fecha=?, hora=?, estado=?, "
                        + "id_usuario=?, id_recepcionista=?, id_veterinario=? "
                        + "WHERE id_cita=? AND activo = TRUE";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cita.getMascota().getIdMascota());
            ps.setDate(2, new java.sql.Date(cita.getFecha().getTime()));
            ps.setTime(3, java.sql.Time.valueOf(cita.getHora()));
            ps.setString(4, cita.getEstado().name());
            ps.setInt(5, cita.getCliente().getIdUsuario());

            if (cita.getRecepcionista() == null) {
                ps.setNull(6, Types.INTEGER);
            } else {
                ps.setInt(6, cita.getRecepcionista().getIdUsuario());
            }

            if (cita.getVeterinario() == null) {
                ps.setNull(7, Types.INTEGER);
            } else {
                ps.setInt(7, cita.getVeterinario().getIdUsuario());
            }

            ps.setInt(8, cita.getIdCita());
            return ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR CITA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idCita) {
        String sql =
                "UPDATE CITA SET activo = FALSE "
                        + "WHERE id_cita = ? AND activo = TRUE";

        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCita);

            return ps.executeUpdate();

        } catch (Exception ex) {
            throw new RuntimeException(
                    "Error al eliminar lógicamente la cita", ex
            );
        }
    }

    @Override
    public Cita obtenerPorId(int idCita) {
        Cita cita = null;
        String sql =
                "SELECT id_cita, id_mascota, fecha, hora, estado, "
                        + "id_usuario, id_recepcionista, id_veterinario "
                        + "FROM CITA "
                        + "WHERE id_cita = ? AND activo = TRUE";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCita);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cita = new Cita();
                    cita.setIdCita(rs.getInt("id_cita"));

                    Mascota mascota = new Mascota();
                    mascota.setIdMascota(rs.getInt("id_mascota"));
                    cita.setMascota(mascota);

                    cita.setFecha(rs.getDate("fecha"));
                    cita.setHora(rs.getTime("hora").toLocalTime());
                    cita.setEstado(EstadoCita.valueOf(rs.getString("estado")));

                    Cliente cliente = new Cliente();
                    cliente.setIdUsuario(rs.getInt("id_usuario"));
                    cita.setCliente(cliente);

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
        String sql =
                "SELECT id_cita, id_mascota, fecha, hora, estado, "
                        + "id_usuario, id_recepcionista, id_veterinario "
                        + "FROM CITA "
                        + "WHERE activo = TRUE";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cita cita = new Cita();
                cita.setIdCita(rs.getInt("id_cita"));

                Mascota mascota = new Mascota();
                mascota.setIdMascota(rs.getInt("id_mascota"));
                cita.setMascota(mascota);

                cita.setFecha(rs.getDate("fecha"));
                cita.setHora(rs.getTime("hora").toLocalTime());
                cita.setEstado(EstadoCita.valueOf(rs.getString("estado")));

                Cliente cliente = new Cliente();
                cliente.setIdUsuario(rs.getInt("id_usuario"));
                cita.setCliente(cliente);

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

    @Override
    public void marcarAtendida(int idCita, int idMascota) {

        String consultar =
                "SELECT estado, id_mascota FROM CITA "
                        + "WHERE id_cita = ? AND activo = TRUE FOR UPDATE";

        String actualizar =
                "UPDATE CITA SET estado = ? "
                        + "WHERE id_cita = ? AND activo = TRUE";

        try {
            Connection con = TransactionContext.getConnection();

            EstadoCita estadoActual;

            try (PreparedStatement ps = con.prepareStatement(consultar)) {
                ps.setInt(1, idCita);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new RuntimeException(
                                "La cita no existe o fue eliminada"
                        );
                    }

                    if (rs.getInt("id_mascota") != idMascota) {
                        throw new RuntimeException(
                                "La mascota de la atención no coincide "
                                        + "con la mascota de la cita"
                        );
                    }

                    estadoActual = EstadoCita.valueOf(
                            rs.getString("estado")
                    );
                }
            }

            // Permite agregar otra atención a una cita ya atendida.
            if (estadoActual == EstadoCita.ATENDIDA) {
                return;
            }

            if (estadoActual != EstadoCita.CONFIRMADA) {
                throw new RuntimeException(
                        "Solo se puede registrar una atención "
                                + "para una cita confirmada o ya atendida"
                );
            }

            try (PreparedStatement ps = con.prepareStatement(actualizar)) {
                ps.setString(1, EstadoCita.ATENDIDA.name());
                ps.setInt(2, idCita);

                if (ps.executeUpdate() != 1) {
                    throw new RuntimeException(
                            "No se pudo actualizar el estado de la cita"
                    );
                }
            }

        } catch (Exception ex) {
            throw new RuntimeException(
                    "Error al marcar la cita como atendida", ex
            );
        }
    }
}
