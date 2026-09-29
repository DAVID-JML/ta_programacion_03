package pe.com.mascovet.mascota.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.enums.model.Especie;
import pe.com.mascovet.enums.model.Sexo;
import pe.com.mascovet.mascota.dao.MascotaDAO;
import pe.com.mascovet.mascota.model.Mascota;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class MascotaImpl implements MascotaDAO {

    @Override
    public int insertar(Mascota mascota) {
        String sql = "INSERT INTO MASCOTA (id_usuario, nombre, fechaNacimiento, especie, raza, sexo, activo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, mascota.getCliente().getIdUsuario());
            ps.setString(2, mascota.getNombre());
            ps.setDate(3, new Date(mascota.getFechaNacimiento().getTime()));
            ps.setString(4, mascota.getEspecie().name());
            ps.setString(5, mascota.getRaza());
            ps.setString(6, mascota.getSexo().name());
            ps.setBoolean(7, true);
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
    public int modificar(Mascota mascota) {
        String sql = "UPDATE MASCOTA SET nombre=?, fechaNacimiento=?, especie=?, raza=?, sexo=? WHERE id_mascota=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, mascota.getNombre());
            ps.setDate(2, new Date(mascota.getFechaNacimiento().getTime()));
            ps.setString(3, mascota.getEspecie().name());
            ps.setString(4, mascota.getRaza());
            ps.setString(5, mascota.getSexo().name());
            ps.setInt(6, mascota.getIdMascota());
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idMascota) {
        String sql = "UPDATE MASCOTA SET activo=0 WHERE id_mascota=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMascota);
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Mascota obtenerPorId(int idMascota) {
        String sql = "SELECT id_mascota, id_usuario, nombre, fechaNacimiento, especie, raza, sexo, activo FROM MASCOTA WHERE id_mascota=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMascota);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Mascota m = new Mascota();
                    m.setIdMascota(rs.getInt("id_mascota"));
                    m.setNombre(rs.getString("nombre"));
                    m.setFechaNacimiento(rs.getDate("fechaNacimiento"));
                    m.setEspecie(Especie.valueOf(rs.getString("especie")));
                    m.setRaza(rs.getString("raza"));
                    m.setSexo(Sexo.valueOf(rs.getString("sexo")));
                    m.setActivo(rs.getBoolean("activo"));
                    return m;
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    @Override
    public List<Mascota> listarTodos() {
        List<Mascota> lista = new ArrayList<>();
        String sql = "SELECT id_mascota, id_usuario, nombre, fechaNacimiento, especie, raza, sexo, activo FROM MASCOTA WHERE activo=1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Mascota m = new Mascota();
                m.setIdMascota(rs.getInt("id_mascota"));
                m.setNombre(rs.getString("nombre"));
                m.setFechaNacimiento(rs.getDate("fechaNacimiento"));
                m.setEspecie(Especie.valueOf(rs.getString("especie")));
                m.setRaza(rs.getString("raza"));
                m.setSexo(Sexo.valueOf(rs.getString("sexo")));
                m.setActivo(rs.getBoolean("activo"));
                lista.add(m);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return lista;
    }
}
