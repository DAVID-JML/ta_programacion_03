package pe.com.mascovet.usuario.impl;

import pe.com.mascovet.usuario.model.Usuario;

import java.sql.*;

public class UsuarioImpl {
    public int insertarUsuarioBase(Usuario u, Connection con) throws SQLException {
        String sql = "INSERT INTO USUARIO (dni, nombre, apellido, nombre_usuario, contrasena, activo) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getDni());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getApellido());
            ps.setString(4, u.getNombreUsuario());
            ps.setString(5, u.getContrasena());
            ps.setBoolean(6, u.isActivo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public int modificarUsuarioBase(Usuario u, Connection con) throws SQLException {
        String sql = "UPDATE USUARIO SET dni=?, nombre=?, apellido=?, nombre_usuario=?, contrasena=?, activo=? WHERE id_usuario=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getDni());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getApellido());
            ps.setString(4, u.getNombreUsuario());
            ps.setString(5, u.getContrasena());
            ps.setBoolean(6, u.isActivo());
            ps.setInt(7, u.getIdUsuario());
            return ps.executeUpdate();
        }
    }
}
