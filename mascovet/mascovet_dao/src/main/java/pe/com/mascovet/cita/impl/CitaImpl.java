package pe.com.mascovet.cita.impl;

import pe.com.mascovet.cita.dao.CitaDAO;
import pe.com.mascovet.cita.model.Cita;
import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.enums.model.EstadoCita;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaImpl implements CitaDAO {

    @Override
    public int insertar(Cita cita) {
        String sql = "INSERT INTO CITA (id_mascota, fecha, hora, estado, id_usuario, id_veterinario) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, cita.getMascota().getIdMascota());
            ps.setDate(2, new Date(cita.getFecha().getTime()));
            //ps.setTime(3, cita.getHora().atDate());
            ps.setObject(3,cita.getHora());
            ps.setString(4, cita.getEstado().name());
            ps.setInt(5, cita.getCliente().getIdUsuario());
            ps.setInt(6, cita.getVeterinario().getIdUsuario());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return 0;
    }

    @Override
    public int modificar(Cita cita) {
        String sql = "UPDATE CITA SET id_mascota=?, fecha=?, hora=?, estado=?, id_usuario=?, id_veterinario=? WHERE id_cita=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cita.getMascota().getIdMascota());
            ps.setDate(2, new Date(cita.getFecha().getTime()));
            ps.setObject(3, cita.getHora());
            ps.setString(4, cita.getEstado().name());
            ps.setInt(5, cita.getCliente().getIdUsuario());
            ps.setInt(6, cita.getVeterinario().getIdUsuario());
            ps.setInt(7, cita.getIdCita());
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idCita) {
        String sql = "DELETE FROM CITA WHERE id_cita=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCita);
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Cita obtenerPorId(int idCita) {
        String sql = "SELECT id_cita, id_mascota, fecha, hora, estado, id_usuario, id_veterinario FROM CITA WHERE id_cita=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCita);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cita c = new Cita();
                    c.setIdCita(rs.getInt("id_cita"));
                    c.setFecha(rs.getDate("fecha"));
                    c.setHora(rs.getTime("hora").toLocalTime());
                    c.setEstado(EstadoCita.valueOf(rs.getString("estado")));
                    return c;
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    @Override
    public List<Cita> listarTodos() {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT id_cita, id_mascota, fecha, hora, estado, id_usuario, id_veterinario FROM CITA";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cita c = new Cita();
                c.setIdCita(rs.getInt("id_cita"));
                c.setFecha(rs.getDate("fecha"));
                c.setHora(rs.getTime("hora").toLocalTime());
                c.setEstado(EstadoCita.valueOf(rs.getString("estado")));
                lista.add(c);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return lista;
    }
}
