package pe.com.mascovet.usuario.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.usuario.dao.VeterinarioDAO;
import pe.com.mascovet.usuario.model.Veterinario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VeterinarioImpl implements VeterinarioDAO {
    private final UsuarioImpl usuarioDao = new UsuarioImpl();
    @Override
    public int insertar(Veterinario veterinario) {
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            con.setAutoCommit(false);

            int idUsuario = usuarioDao.insertarUsuarioBase(veterinario, con);
            veterinario.setIdUsuario(idUsuario);

            String sql = "INSERT INTO VETERINARIO (id_usuario, num_colegiatura, especialidad) VALUES (?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idUsuario);
                ps.setString(2, veterinario.getNumeroColegiatura());
                ps.setString(3, veterinario.getEspecialidad().name());
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
    public int modificar(Veterinario veterinario) {
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            con.setAutoCommit(false);

            usuarioDao.modificarUsuarioBase(veterinario, con);
            String sql = "UPDATE VETERINARIO SET num_colegiatura=?, especialidad=? WHERE id_usuario=?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, veterinario.getNumeroColegiatura());
                ps.setString(2, veterinario.getEspecialidad().name());
                ps.setInt(3, veterinario.getIdUsuario());
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
    public int eliminar(int idVeterinario) {
        String sql = "UPDATE USUARIO SET activo=0 WHERE id_usuario=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVeterinario);
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Veterinario obtenerPorId(int idVeterinario) {
        String sql = "SELECT u.id_usuario, u.dni, u.nombre, u.apellido, u.nombre_usuario, u.contrasena, u.activo, v.num_colegiatura, v.especialidad " +
                "FROM USUARIO u INNER JOIN VETERINARIO v ON u.id_usuario = v.id_usuario WHERE u.id_usuario=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVeterinario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Veterinario v = new Veterinario();
                    v.setIdUsuario(rs.getInt("id_usuario"));
                    v.setDni(rs.getString("dni"));
                    v.setNombre(rs.getString("nombre"));
                    v.setApellido(rs.getString("apellido"));
                    v.setNombreUsuario(rs.getString("nombre_usuario"));
                    v.setContrasena(rs.getString("contrasena"));
                    v.setActivo(rs.getBoolean("activo"));
                    v.setNumeroColegiatura(rs.getString("num_colegiatura"));
                    // Convertir enum Especialidad
                    v.setEspecialidad(pe.com.mascovet.enums.model.Especialidad.valueOf(rs.getString("especialidad")));
                    return v;
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    @Override
    public List<Veterinario> listarTodos() {
        List<Veterinario> lista = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.dni, u.nombre, u.apellido, u.nombre_usuario, u.contrasena, u.activo, v.num_colegiatura, v.especialidad " +
                "FROM USUARIO u INNER JOIN VETERINARIO v ON u.id_usuario = v.id_usuario WHERE u.activo=1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Veterinario v = new Veterinario();
                v.setIdUsuario(rs.getInt("id_usuario"));
                v.setDni(rs.getString("dni"));
                v.setNombre(rs.getString("nombre"));
                v.setApellido(rs.getString("apellido"));
                v.setNombreUsuario(rs.getString("nombre_usuario"));
                v.setContrasena(rs.getString("contrasena"));
                v.setActivo(rs.getBoolean("activo"));
                v.setNumeroColegiatura(rs.getString("num_colegiatura"));
                v.setEspecialidad(pe.com.mascovet.enums.model.Especialidad.valueOf(rs.getString("especialidad")));
                lista.add(v);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return lista;
    }
}
