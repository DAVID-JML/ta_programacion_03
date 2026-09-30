package pe.com.mascovet.usuario.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.config.TransactionContext;
import pe.com.mascovet.usuario.dao.AdministradorDAO;
import pe.com.mascovet.usuario.model.Administrador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AdministradorImpl implements AdministradorDAO {

    private final UsuarioImpl usuarioDao = new UsuarioImpl();

    @Override
    public int insertar(Administrador administrador) {
        String sql = "INSERT INTO ADMINISTRADOR (id_usuario, cargo) VALUES (?, ?)";
        try {
            Connection con = TransactionContext.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                int idUsuario = usuarioDao.insertarUsuarioBase(administrador, con);
                if (idUsuario <= 0) {
                    throw new RuntimeException("No se pudo generar el identificador del usuario");
                }
                ps.setInt(1, idUsuario);
                ps.setString(2, administrador.getCargo());
                ps.executeUpdate();
                administrador.setIdUsuario(idUsuario);
                return administrador.getIdUsuario();
            } catch (Exception ex) {
                System.out.println("ERROR AL INSERTAR ADMINISTRADOR: " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR ADMINISTRADOR: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Administrador administrador) {
        String sql = "UPDATE ADMINISTRADOR SET cargo=? WHERE id_usuario=?";
        try {
            Connection con = TransactionContext.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                int filasUsuario = usuarioDao.modificarUsuarioBase(administrador, con);
                ps.setString(1, administrador.getCargo());
                ps.setInt(2, administrador.getIdUsuario());
                int filasAdministrador = ps.executeUpdate();
                return (filasUsuario > 0 && filasAdministrador > 0) ? 1 : 0;
            } catch (Exception ex) {
                System.out.println("ERROR AL MODIFICAR ADMINISTRADOR: " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR ADMINISTRADOR: " + ex.getMessage());
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
            System.out.println("ERROR AL ELIMINAR ADMINISTRADOR: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Administrador obtenerPorId(int idAdministrador) {
        Administrador administrador = null;
        String sql = "SELECT u.id_usuario, u.dni, u.nombre, u.apellido, u.nombre_usuario, u.contrasena, u.activo, a.cargo " +
                "FROM USUARIO u INNER JOIN ADMINISTRADOR a ON u.id_usuario=a.id_usuario WHERE u.id_usuario=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAdministrador);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    administrador = new Administrador();
                    administrador.setIdUsuario(rs.getInt("id_usuario"));
                    administrador.setDni(rs.getString("dni"));
                    administrador.setNombre(rs.getString("nombre"));
                    administrador.setApellido(rs.getString("apellido"));
                    administrador.setNombreUsuario(rs.getString("nombre_usuario"));
                    administrador.setContrasena(rs.getString("contrasena"));
                    administrador.setActivo(rs.getBoolean("activo"));
                    administrador.setCargo(rs.getString("cargo"));
                }
            }
            return administrador;
        } catch (Exception ex) {
            System.out.println("ERROR AL OBTENER ADMINISTRADOR POR ID: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Administrador> listarTodos() {
        List<Administrador> administradores = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.dni, u.nombre, u.apellido, u.nombre_usuario, u.contrasena, u.activo, a.cargo " +
                "FROM USUARIO u INNER JOIN ADMINISTRADOR a ON u.id_usuario=a.id_usuario WHERE u.activo=1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Administrador administrador = new Administrador();
                administrador.setIdUsuario(rs.getInt("id_usuario"));
                administrador.setDni(rs.getString("dni"));
                administrador.setNombre(rs.getString("nombre"));
                administrador.setApellido(rs.getString("apellido"));
                administrador.setNombreUsuario(rs.getString("nombre_usuario"));
                administrador.setContrasena(rs.getString("contrasena"));
                administrador.setActivo(rs.getBoolean("activo"));
                administrador.setCargo(rs.getString("cargo"));
                administradores.add(administrador);
            }
            return administradores;
        } catch (Exception ex) {
            System.out.println("ERROR AL LISTAR ADMINISTRADORES: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
