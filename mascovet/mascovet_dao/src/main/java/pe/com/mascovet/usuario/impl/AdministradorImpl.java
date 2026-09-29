package pe.com.mascovet.usuario.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.usuario.dao.AdministradorDAO;
import pe.com.mascovet.usuario.model.Administrador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdministradorImpl implements AdministradorDAO {
    private final UsuarioImpl usuarioDao = new UsuarioImpl();
    @Override
    public int insertar(Administrador administrador) {
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            con.setAutoCommit(false);

            int idUsuario = usuarioDao.insertarUsuarioBase(administrador, con);
            administrador.setIdUsuario(idUsuario);

            String sql = "INSERT INTO ADMINISTRADOR (id_usuario, cargo) VALUES (?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idUsuario);
                ps.setString(2, administrador.getCargo());
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
    public int modificar(Administrador administrador) {
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            con.setAutoCommit(false);

            usuarioDao.modificarUsuarioBase(administrador, con);
            String sql = "UPDATE ADMINISTRADOR SET cargo=? WHERE id_usuario=?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, administrador.getCargo());
                ps.setInt(2, administrador.getIdUsuario());
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
    public int eliminar(int idAdministrador) {
        String sql = "UPDATE USUARIO SET activo=0 WHERE id_usuario=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAdministrador);
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Administrador obtenerPorId(int idAdministrador) {
        String sql = "SELECT u.id_usuario, u.dni, u.nombre, u.apellido, u.nombre_usuario, u.contrasena, u.activo, a.cargo " +
                "FROM USUARIO u INNER JOIN ADMINISTRADOR a ON u.id_usuario = a.id_usuario WHERE u.id_usuario=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAdministrador);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Administrador a = new Administrador();
                    a.setIdUsuario(rs.getInt("id_usuario"));
                    a.setDni(rs.getString("dni"));
                    a.setNombre(rs.getString("nombre"));
                    a.setApellido(rs.getString("apellido"));
                    a.setNombreUsuario(rs.getString("nombre_usuario"));
                    a.setContrasena(rs.getString("contrasena"));
                    a.setActivo(rs.getBoolean("activo"));
                    a.setCargo(rs.getString("cargo"));
                    return a;
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    @Override
    public List<Administrador> listarTodos() {
        List<Administrador> lista = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.dni, u.nombre, u.apellido, u.nombre_usuario, u.contrasena, u.activo, a.cargo " +
                "FROM USUARIO u INNER JOIN ADMINISTRADOR a ON u.id_usuario = a.id_usuario WHERE u.activo=1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Administrador a = new Administrador();
                a.setIdUsuario(rs.getInt("id_usuario"));
                a.setDni(rs.getString("dni"));
                a.setNombre(rs.getString("nombre"));
                a.setApellido(rs.getString("apellido"));
                a.setNombreUsuario(rs.getString("nombre_usuario"));
                a.setContrasena(rs.getString("contrasena"));
                a.setActivo(rs.getBoolean("activo"));
                a.setCargo(rs.getString("cargo"));
                lista.add(a);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return lista;
    }
}
