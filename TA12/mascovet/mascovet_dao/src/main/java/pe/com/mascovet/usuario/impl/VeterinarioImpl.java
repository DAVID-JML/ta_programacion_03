package pe.com.mascovet.usuario.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.config.TransactionContext;
import pe.com.mascovet.enums.model.Especialidad;
import pe.com.mascovet.usuario.dao.VeterinarioDAO;
import pe.com.mascovet.usuario.model.Veterinario;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.Types;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VeterinarioImpl implements VeterinarioDAO {

    @Override
    public int insertar(Veterinario veterinario) {
        String sql = "{call INSERTAR_VETERINARIO(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        Connection conexion = TransactionContext.getCurrentConnection();
        if (conexion == null)
            conexion = DBManager.getInstance().getConnection();
        try (Connection con = conexion) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_id_usuario", Types.INTEGER);
                cs.setString("_dni", veterinario.getDni());
                cs.setString("_nombre", veterinario.getNombre());
                cs.setString("_apellido_paterno", veterinario.getApellidoPaterno());
                cs.setString("_apellido_materno", veterinario.getApellidoMaterno());
                cs.setString("_nombre_usuario", veterinario.getNombreUsuario());
                cs.setString("_contrasena", veterinario.getContrasena());
                cs.setBoolean("_activo", veterinario.isActivo());
                cs.setString("_num_colegiatura", veterinario.getNumeroColegiatura());
                cs.setString("_especialidad", veterinario.getEspecialidad().name());
                cs.setString("_correo", veterinario.getCorreo());
                cs.setString("_telefono", veterinario.getTelefono());
                cs.executeUpdate();
                int idGenerado = cs.getInt("_id_usuario");
                veterinario.setIdUsuario(idGenerado);
                return idGenerado;
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR VETERINARIO: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Veterinario veterinario) {
        String sql = "{call MODIFICAR_VETERINARIO(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_resultado", Types.INTEGER);
                cs.setString("_dni", veterinario.getDni());
                cs.setString("_nombre", veterinario.getNombre());
                cs.setString("_apellido_paterno", veterinario.getApellidoPaterno());
                cs.setString("_apellido_materno", veterinario.getApellidoMaterno());
                cs.setString("_nombre_usuario", veterinario.getNombreUsuario());
                cs.setString("_contrasena", veterinario.getContrasena());
                cs.setBoolean("_activo", veterinario.isActivo());
                cs.setInt("_id_usuario", veterinario.getIdUsuario());
                cs.setString("_num_colegiatura", veterinario.getNumeroColegiatura());
                cs.setString("_especialidad", veterinario.getEspecialidad().name());
                cs.setString("_correo", veterinario.getCorreo());
                cs.setString("_telefono", veterinario.getTelefono());
                cs.executeUpdate();
                return cs.getInt("_resultado");
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR VETERINARIO: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idVeterinario) {
        String sql = "{call ELIMINAR_VETERINARIO(?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_resultado", Types.INTEGER);
                cs.setInt("_id_usuario", idVeterinario);
                cs.executeUpdate();
                return cs.getInt("_resultado");
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL ELIMINAR VETERINARIO: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Veterinario obtenerPorId(int idVeterinario) {
        Veterinario veterinario = null;
        String sql = "{call LISTAR_VETERINARIO_X_ID(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt("_id_usuario", idVeterinario);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    veterinario = new Veterinario();
                    veterinario.setIdUsuario(rs.getInt("id_usuario"));
                    veterinario.setDni(rs.getString("dni"));
                    veterinario.setNombre(rs.getString("nombre"));
                    veterinario.setApellidoPaterno(rs.getString("apellido_paterno"));
                    veterinario.setApellidoMaterno(rs.getString("apellido_materno"));
                    veterinario.setNombreUsuario(rs.getString("nombre_usuario"));
                    veterinario.setContrasena(rs.getString("contrasena"));
                    veterinario.setActivo(rs.getBoolean("activo"));
                    veterinario.setCorreo(rs.getString("correo"));
                    veterinario.setTelefono(rs.getString("telefono"));
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
        String sql = "{call LISTAR_VETERINARIO_TODOS()}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                Veterinario veterinario = new Veterinario();
                veterinario.setIdUsuario(rs.getInt("id_usuario"));
                veterinario.setDni(rs.getString("dni"));
                veterinario.setNombre(rs.getString("nombre"));
                veterinario.setApellidoPaterno(rs.getString("apellido_paterno"));
                    veterinario.setApellidoMaterno(rs.getString("apellido_materno"));
                veterinario.setNombreUsuario(rs.getString("nombre_usuario"));
                veterinario.setContrasena(rs.getString("contrasena"));
                veterinario.setActivo(rs.getBoolean("activo"));
                veterinario.setCorreo(rs.getString("correo"));
                    veterinario.setTelefono(rs.getString("telefono"));
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
