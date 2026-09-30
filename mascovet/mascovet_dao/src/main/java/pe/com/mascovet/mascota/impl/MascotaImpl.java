package pe.com.mascovet.mascota.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.enums.model.Especie;
import pe.com.mascovet.enums.model.Sexo;
import pe.com.mascovet.mascota.dao.MascotaDAO;
import pe.com.mascovet.mascota.model.Mascota;
import pe.com.mascovet.usuario.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
            ps.setDate(3, new java.sql.Date(mascota.getFechaNacimiento().getTime()));
            ps.setString(4, mascota.getEspecie().name());
            ps.setString(5, mascota.getRaza());
            ps.setString(6, mascota.getSexo().name());
            ps.setBoolean(7, mascota.isActivo());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    mascota.setIdMascota(rs.getInt(1));
                    return mascota.getIdMascota();
                }
            }
            return 0;
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR MASCOTA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Mascota mascota) {
        String sql = "UPDATE MASCOTA SET id_usuario=?, nombre=?, fechaNacimiento=?, especie=?, raza=?, sexo=?, activo=? WHERE id_mascota=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, mascota.getCliente().getIdUsuario());
            ps.setString(2, mascota.getNombre());
            ps.setDate(3, new java.sql.Date(mascota.getFechaNacimiento().getTime()));
            ps.setString(4, mascota.getEspecie().name());
            ps.setString(5, mascota.getRaza());
            ps.setString(6, mascota.getSexo().name());
            ps.setBoolean(7, mascota.isActivo());
            ps.setInt(8, mascota.getIdMascota());
            return ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR MASCOTA: " + ex.getMessage());
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
            System.out.println("ERROR AL ELIMINAR MASCOTA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Mascota obtenerPorId(int idMascota) {
        Mascota mascota = null;
        String sql = "SELECT id_mascota, id_usuario, nombre, fechaNacimiento, especie, raza, sexo, activo FROM MASCOTA WHERE id_mascota=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMascota);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    mascota = new Mascota();
                    mascota.setIdMascota(rs.getInt("id_mascota"));
                    Cliente cliente = new Cliente();
                    cliente.setIdUsuario(rs.getInt("id_usuario"));
                    mascota.setCliente(cliente);
                    mascota.setNombre(rs.getString("nombre"));
                    mascota.setFechaNacimiento(rs.getDate("fechaNacimiento"));
                    mascota.setEspecie(Especie.valueOf(rs.getString("especie")));
                    mascota.setRaza(rs.getString("raza"));
                    mascota.setSexo(Sexo.valueOf(rs.getString("sexo")));
                    mascota.setActivo(rs.getBoolean("activo"));
                }
            }
            return mascota;
        } catch (Exception ex) {
            System.out.println("ERROR AL OBTENER MASCOTA POR ID: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Mascota> listarTodos() {
        List<Mascota> mascotas = new ArrayList<>();
        String sql = "SELECT id_mascota, id_usuario, nombre, fechaNacimiento, especie, raza, sexo, activo FROM MASCOTA WHERE activo=1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Mascota mascota = new Mascota();
                mascota.setIdMascota(rs.getInt("id_mascota"));
                Cliente cliente = new Cliente();
                cliente.setIdUsuario(rs.getInt("id_usuario"));
                mascota.setCliente(cliente);
                mascota.setNombre(rs.getString("nombre"));
                mascota.setFechaNacimiento(rs.getDate("fechaNacimiento"));
                mascota.setEspecie(Especie.valueOf(rs.getString("especie")));
                mascota.setRaza(rs.getString("raza"));
                mascota.setSexo(Sexo.valueOf(rs.getString("sexo")));
                mascota.setActivo(rs.getBoolean("activo"));
                mascotas.add(mascota);
            }
            return mascotas;
        } catch (Exception ex) {
            System.out.println("ERROR AL LISTAR MASCOTAS: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
