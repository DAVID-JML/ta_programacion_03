package pe.com.mascovet.usuario.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.config.TransactionContext;
import pe.com.mascovet.usuario.dao.ClienteDAO;
import pe.com.mascovet.usuario.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClienteImpl implements ClienteDAO {

    private final UsuarioImpl usuarioDao = new UsuarioImpl();

    @Override
    public int insertar(Cliente cliente) {
        String sql = "INSERT INTO CLIENTE (id_usuario, correo, telefono) VALUES (?, ?, ?)";
        try {
            Connection con = TransactionContext.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                int idUsuario = usuarioDao.insertarUsuarioBase(cliente, con);
                if (idUsuario <= 0) {
                    throw new RuntimeException("No se pudo generar el identificador del usuario");
                }
                ps.setInt(1, idUsuario);
                ps.setString(2, cliente.getCorreo());
                ps.setString(3, cliente.getTelefono());
                ps.executeUpdate();
                cliente.setIdUsuario(idUsuario);
                return cliente.getIdUsuario();
            } catch (Exception ex) {
                System.out.println("ERROR AL INSERTAR CLIENTE: " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR CLIENTE: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Cliente cliente) {
        String sql = "UPDATE CLIENTE SET correo=?, telefono=? WHERE id_usuario=?";
        try {
            Connection con = TransactionContext.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                int filasUsuario = usuarioDao.modificarUsuarioBase(cliente, con);
                ps.setString(1, cliente.getCorreo());
                ps.setString(2, cliente.getTelefono());
                ps.setInt(3, cliente.getIdUsuario());
                int filasCliente = ps.executeUpdate();
                return (filasUsuario > 0 && filasCliente > 0) ? 1 : 0;
            } catch (Exception ex) {
                System.out.println("ERROR AL MODIFICAR CLIENTE: " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR CLIENTE: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idCliente) {
        String sql = "UPDATE USUARIO SET activo=0 WHERE id_usuario=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            return ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR AL ELIMINAR CLIENTE: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Cliente obtenerPorId(int idCliente) {
        Cliente cliente = null;
        String sql = "SELECT u.id_usuario, u.dni, u.nombre, u.apellido, u.nombre_usuario, u.contrasena, u.activo, c.correo, c.telefono " +
                "FROM USUARIO u INNER JOIN CLIENTE c ON u.id_usuario=c.id_usuario WHERE u.id_usuario=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente();
                    cliente.setIdUsuario(rs.getInt("id_usuario"));
                    cliente.setDni(rs.getString("dni"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setApellido(rs.getString("apellido"));
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
        String sql = "SELECT u.id_usuario, u.dni, u.nombre, u.apellido, u.nombre_usuario, u.contrasena, u.activo, c.correo, c.telefono " +
                "FROM USUARIO u INNER JOIN CLIENTE c ON u.id_usuario=c.id_usuario WHERE u.activo=1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdUsuario(rs.getInt("id_usuario"));
                cliente.setDni(rs.getString("dni"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
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
