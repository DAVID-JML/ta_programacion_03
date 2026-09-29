package pe.com.mascovet.usuario.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.dao.IDAO;
import pe.com.mascovet.usuario.dao.ClienteDAO;
import pe.com.mascovet.usuario.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteImpl implements ClienteDAO {
    private final UsuarioImpl usuarioDao = new UsuarioImpl();
    @Override
    public int insertar(Cliente cliente) {
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            con.setAutoCommit(false);

            int idUsuario = usuarioDao.insertarUsuarioBase(cliente, con);
            cliente.setIdUsuario(idUsuario);

            String sql = "INSERT INTO CLIENTE (id_usuario, correo, telefono) VALUES (?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idUsuario);
                ps.setString(2, cliente.getCorreo());
                ps.setString(3, cliente.getTelefono());
                ps.executeUpdate();
            }
            con.commit();
            return idUsuario;
        } catch (Exception ex) {
            if (con != null) try { con.rollback(); } catch (SQLException e) {}
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Cliente cliente) {
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            con.setAutoCommit(false);

            usuarioDao.modificarUsuarioBase(cliente, con);
            String sql = "UPDATE CLIENTE SET correo=?, telefono=? WHERE id_usuario=?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, cliente.getCorreo());
                ps.setString(2,cliente.getTelefono());
                ps.setInt(3, cliente.getIdUsuario());
                ps.executeUpdate();
            }
            con.commit();
            return 1;
        } catch (Exception ex) {
            if (con != null) try { con.rollback(); } catch (SQLException e) {}
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
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Cliente obtenerPorId(int idCliente) {
        String sql = "SELECT u.id_usuario, u.dni, u.nombre, u.apellido, u.nombre_usuario, u.contrasena, u.activo, c.correo, c.telefono " +
                "FROM USUARIO u INNER JOIN CLIENTE c ON u.id_usuario = c.id_usuario WHERE u.id_usuario=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cliente c = new Cliente();
                    c.setIdUsuario(rs.getInt("id_usuario"));
                    c.setDni(rs.getString("dni"));
                    c.setNombre(rs.getString("nombre"));
                    c.setApellido(rs.getString("apellido"));
                    c.setNombreUsuario(rs.getString("nombre_usuario"));
                    c.setContrasena(rs.getString("contrasena"));
                    c.setActivo(rs.getBoolean("activo"));
                    c.setCorreo(rs.getString("correo"));
                    c.setTelefono(rs.getString("telefono"));
                    return c;
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.dni, u.nombre, u.apellido, u.nombre_usuario, u.contrasena, u.activo, c.correo, c.telefono " +
                "FROM USUARIO u INNER JOIN CLIENTE c ON u.id_usuario = c.id_usuario WHERE u.activo=1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setIdUsuario(rs.getInt("id_usuario"));
                c.setDni(rs.getString("dni"));
                c.setNombre(rs.getString("nombre"));
                c.setApellido(rs.getString("apellido"));
                c.setNombreUsuario(rs.getString("nombre_usuario"));
                c.setContrasena(rs.getString("contrasena"));
                c.setActivo(rs.getBoolean("activo"));
                c.setCorreo(rs.getString("correo"));
                c.setTelefono(rs.getString("telefono"));
                lista.add(c);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return lista;
    }
}
