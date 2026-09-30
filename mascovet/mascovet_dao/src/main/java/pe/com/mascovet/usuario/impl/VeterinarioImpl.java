package pe.com.mascovet.usuario.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.config.TransactionContext;
import pe.com.mascovet.enums.model.Especialidad;
import pe.com.mascovet.usuario.dao.VeterinarioDAO;
import pe.com.mascovet.usuario.model.Veterinario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VeterinarioImpl implements VeterinarioDAO {

    private final UsuarioImpl usuarioDao = new UsuarioImpl();

    @Override
    public int insertar(Veterinario veterinario) {
        String sql = "INSERT INTO VETERINARIO (id_usuario, num_colegiatura, especialidad) VALUES (?, ?, ?)";
        try {
            Connection con = TransactionContext.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                int idUsuario = usuarioDao.insertarUsuarioBase(veterinario, con);
                if (idUsuario <= 0) {
                    throw new RuntimeException("No se pudo generar el identificador del usuario");
                }
                ps.setInt(1, idUsuario);
                ps.setString(2, veterinario.getNumeroColegiatura());
                ps.setString(3, veterinario.getEspecialidad().name());
                ps.executeUpdate();
                veterinario.setIdUsuario(idUsuario);
                return veterinario.getIdUsuario();
            } catch (Exception ex) {
                System.out.println("ERROR AL INSERTAR VETERINARIO: " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR VETERINARIO: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Veterinario veterinario) {
        String sql = "UPDATE VETERINARIO SET num_colegiatura=?, especialidad=? WHERE id_usuario=?";
        try {
            Connection con = TransactionContext.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                int filasUsuario = usuarioDao.modificarUsuarioBase(veterinario, con);
                ps.setString(1, veterinario.getNumeroColegiatura());
                ps.setString(2, veterinario.getEspecialidad().name());
                ps.setInt(3, veterinario.getIdUsuario());
                int filasVeterinario = ps.executeUpdate();
                return (filasUsuario > 0 && filasVeterinario > 0) ? 1 : 0;
            } catch (Exception ex) {
                System.out.println("ERROR AL MODIFICAR VETERINARIO: " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR VETERINARIO: " + ex.getMessage());
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
            System.out.println("ERROR AL ELIMINAR VETERINARIO: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Veterinario obtenerPorId(int idVeterinario) {
        Veterinario veterinario = null;
        String sql = "SELECT u.id_usuario, u.dni, u.nombre, u.apellido, u.nombre_usuario, u.contrasena, u.activo, " +
                "v.num_colegiatura, v.especialidad FROM USUARIO u INNER JOIN VETERINARIO v ON u.id_usuario=v.id_usuario " +
                "WHERE u.id_usuario=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVeterinario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    veterinario = new Veterinario();
                    veterinario.setIdUsuario(rs.getInt("id_usuario"));
                    veterinario.setDni(rs.getString("dni"));
                    veterinario.setNombre(rs.getString("nombre"));
                    veterinario.setApellido(rs.getString("apellido"));
                    veterinario.setNombreUsuario(rs.getString("nombre_usuario"));
                    veterinario.setContrasena(rs.getString("contrasena"));
                    veterinario.setActivo(rs.getBoolean("activo"));
                    veterinario.setNumeroColegiatura(rs.getString("num_colegiatura"));
                    veterinario.setEspecialidad(Especialidad.valueOf(rs.getString("especialidad")));
                }
            }
            return veterinario;
        } catch (Exception ex) {
            System.out.println("ERROR AL OBTENER VETERINARIO POR ID: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Veterinario> listarTodos() {
        List<Veterinario> veterinarios = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.dni, u.nombre, u.apellido, u.nombre_usuario, u.contrasena, u.activo, " +
                "v.num_colegiatura, v.especialidad FROM USUARIO u INNER JOIN VETERINARIO v ON u.id_usuario=v.id_usuario " +
                "WHERE u.activo=1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Veterinario veterinario = new Veterinario();
                veterinario.setIdUsuario(rs.getInt("id_usuario"));
                veterinario.setDni(rs.getString("dni"));
                veterinario.setNombre(rs.getString("nombre"));
                veterinario.setApellido(rs.getString("apellido"));
                veterinario.setNombreUsuario(rs.getString("nombre_usuario"));
                veterinario.setContrasena(rs.getString("contrasena"));
                veterinario.setActivo(rs.getBoolean("activo"));
                veterinario.setNumeroColegiatura(rs.getString("num_colegiatura"));
                veterinario.setEspecialidad(Especialidad.valueOf(rs.getString("especialidad")));
                veterinarios.add(veterinario);
            }
            return veterinarios;
        } catch (Exception ex) {
            System.out.println("ERROR AL LISTAR VETERINARIOS: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
