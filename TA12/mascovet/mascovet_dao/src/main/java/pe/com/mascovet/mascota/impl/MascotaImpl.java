package pe.com.mascovet.mascota.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.enums.model.Especie;
import pe.com.mascovet.enums.model.Sexo;
import pe.com.mascovet.mascota.dao.MascotaDAO;
import pe.com.mascovet.mascota.model.Mascota;
import pe.com.mascovet.usuario.model.Cliente;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.Types;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MascotaImpl implements MascotaDAO {

    @Override
    public int insertar(Mascota mascota) {
        String sql = "{call INSERTAR_MASCOTA(?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter("_id_mascota", Types.INTEGER);
            cs.setInt("_id_usuario", mascota.getCliente().getIdUsuario());
            cs.setString("_nombre", mascota.getNombre());
            cs.setDate("_fechanacimiento", new java.sql.Date(mascota.getFechaNacimiento().getTime()));
            cs.setString("_especie", mascota.getEspecie().name());
            cs.setString("_raza", mascota.getRaza());
            cs.setString("_sexo", mascota.getSexo().name());
            cs.setBoolean("_activo", mascota.isActivo());
            cs.executeUpdate();

            if (cs.getInt("_id_mascota") > 0) {
                    mascota.setIdMascota(cs.getInt("_id_mascota"));
                    return mascota.getIdMascota();
                }
            return 0;
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR MASCOTA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Mascota mascota) {
        String sql = "{call MODIFICAR_MASCOTA(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter("_resultado", Types.INTEGER);
            cs.setInt("_id_usuario", mascota.getCliente().getIdUsuario());
            cs.setString("_nombre", mascota.getNombre());
            cs.setDate("_fechanacimiento", new java.sql.Date(mascota.getFechaNacimiento().getTime()));
            cs.setString("_especie", mascota.getEspecie().name());
            cs.setString("_raza", mascota.getRaza());
            cs.setString("_sexo", mascota.getSexo().name());
            cs.setBoolean("_activo", mascota.isActivo());
            cs.setInt("_id_mascota", mascota.getIdMascota());
            cs.executeUpdate();
            return cs.getInt("_resultado");
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR MASCOTA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idMascota) {
        String sql = "{call ELIMINAR_MASCOTA(?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter("_resultado", Types.INTEGER);
            cs.setInt("_id_mascota", idMascota);
            cs.executeUpdate();
            return cs.getInt("_resultado");
        } catch (Exception ex) {
            System.out.println("ERROR AL ELIMINAR MASCOTA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Mascota obtenerPorId(int idMascota) {
        Mascota mascota = null;
        String sql = "{call LISTAR_MASCOTA_X_ID(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt("_id_mascota", idMascota);
            try (ResultSet rs = cs.executeQuery()) {
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
        String sql = "{call LISTAR_MASCOTA_TODOS()}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
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
