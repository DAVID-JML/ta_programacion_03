package pe.com.mascovet.usuario.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.usuario.dao.ClienteDAO;
import pe.com.mascovet.usuario.model.Cliente;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.Types;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClienteImpl implements ClienteDAO {

    @Override
    public int insertar(Cliente cliente) {
        String sql = "{call INSERTAR_CLIENTE(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_id_usuario", Types.INTEGER);
                cs.setString("_dni", cliente.getDni());
                cs.setString("_nombre", cliente.getNombre());
                cs.setString("_apellido_paterno", cliente.getApellidoPaterno());
                cs.setString("_apellido_materno", cliente.getApellidoMaterno());
                cs.setString("_nombre_usuario", cliente.getNombreUsuario());
                cs.setString("_contrasena", cliente.getContrasena());
                cs.setBoolean("_activo", cliente.isActivo());
                cs.setString("_correo", cliente.getCorreo());
                cs.setString("_telefono", cliente.getTelefono());
                cs.executeUpdate();
                int idGenerado = cs.getInt("_id_usuario");
                cliente.setIdUsuario(idGenerado);
                return idGenerado;
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR CLIENTE: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Cliente cliente) {
        String sql = "{call MODIFICAR_CLIENTE(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_resultado", Types.INTEGER);
                cs.setString("_dni", cliente.getDni());
                cs.setString("_nombre", cliente.getNombre());
                cs.setString("_apellido_paterno", cliente.getApellidoPaterno());
                cs.setString("_apellido_materno", cliente.getApellidoMaterno());
                cs.setString("_nombre_usuario", cliente.getNombreUsuario());
                cs.setString("_contrasena", cliente.getContrasena());
                cs.setBoolean("_activo", cliente.isActivo());
                cs.setInt("_id_usuario", cliente.getIdUsuario());
                cs.setString("_correo", cliente.getCorreo());
                cs.setString("_telefono", cliente.getTelefono());
                cs.executeUpdate();
                return cs.getInt("_resultado");
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR CLIENTE: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idCliente) {
        String sql = "{call ELIMINAR_CLIENTE(?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_resultado", Types.INTEGER);
                cs.setInt("_id_usuario", idCliente);
                cs.executeUpdate();
                return cs.getInt("_resultado");
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL ELIMINAR CLIENTE: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Cliente obtenerPorId(int idCliente) {
        Cliente cliente = null;
        String sql = "{call LISTAR_CLIENTE_X_ID(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt("_id_usuario", idCliente);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente();
                    cliente.setIdUsuario(rs.getInt("id_usuario"));
                    cliente.setDni(rs.getString("dni"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setApellidoPaterno(rs.getString("apellido_paterno"));
                    cliente.setApellidoMaterno(rs.getString("apellido_materno"));
                    cliente.setNombreUsuario(rs.getString("nombre_usuario"));
                    cliente.setContrasena(rs.getString("contrasena"));
                    cliente.setActivo(rs.getBoolean("activo"));
                    cliente.setCorreo(rs.getString("correo"));
                    cliente.setTelefono(rs.getString("telefono"));
                }
            }
            return cliente;
        } catch (Exception ex) {
            System.out.println("ERROR AL OBTENER CLIENTE POR ID: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "{call LISTAR_CLIENTE_TODOS()}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdUsuario(rs.getInt("id_usuario"));
                cliente.setDni(rs.getString("dni"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellidoPaterno(rs.getString("apellido_paterno"));
                    cliente.setApellidoMaterno(rs.getString("apellido_materno"));
                cliente.setNombreUsuario(rs.getString("nombre_usuario"));
                cliente.setContrasena(rs.getString("contrasena"));
                cliente.setActivo(rs.getBoolean("activo"));
                cliente.setCorreo(rs.getString("correo"));
                cliente.setTelefono(rs.getString("telefono"));
                clientes.add(cliente);
            }
            return clientes;
        } catch (Exception ex) {
            System.out.println("ERROR AL LISTAR CLIENTES: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
