package pe.com.mascovet.usuario.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.usuario.dao.AdministradorDAO;
import pe.com.mascovet.usuario.model.Administrador;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.Types;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AdministradorImpl implements AdministradorDAO {

    @Override
    public int insertar(Administrador administrador) {
        String sql = "{call INSERTAR_ADMINISTRADOR(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_id_usuario", Types.INTEGER);
                cs.setString("_dni", administrador.getDni());
                cs.setString("_nombre", administrador.getNombre());
                cs.setString("_apellido_paterno", administrador.getApellidoPaterno());
                cs.setString("_apellido_materno", administrador.getApellidoMaterno());
                cs.setString("_nombre_usuario", administrador.getNombreUsuario());
                cs.setString("_contrasena", administrador.getContrasena());
                cs.setBoolean("_activo", administrador.isActivo());
                cs.setString("_cargo", administrador.getCargo());
                cs.executeUpdate();
                int idGenerado = cs.getInt("_id_usuario");
                administrador.setIdUsuario(idGenerado);
                return idGenerado;
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR ADMINISTRADOR: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Administrador administrador) {
        String sql = "{call MODIFICAR_ADMINISTRADOR(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_resultado", Types.INTEGER);
                cs.setString("_dni", administrador.getDni());
                cs.setString("_nombre", administrador.getNombre());
                cs.setString("_apellido_paterno", administrador.getApellidoPaterno());
                cs.setString("_apellido_materno", administrador.getApellidoMaterno());
                cs.setString("_nombre_usuario", administrador.getNombreUsuario());
                cs.setString("_contrasena", administrador.getContrasena());
                cs.setBoolean("_activo", administrador.isActivo());
                cs.setInt("_id_usuario", administrador.getIdUsuario());
                cs.setString("_cargo", administrador.getCargo());
                cs.executeUpdate();
                return cs.getInt("_resultado");
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR ADMINISTRADOR: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idAdministrador) {
        String sql = "{call ELIMINAR_ADMINISTRADOR(?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_resultado", Types.INTEGER);
                cs.setInt("_id_usuario", idAdministrador);
                cs.executeUpdate();
                return cs.getInt("_resultado");
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL ELIMINAR ADMINISTRADOR: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Administrador obtenerPorId(int idAdministrador) {
        Administrador administrador = null;
        String sql = "{call LISTAR_ADMINISTRADOR_X_ID(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt("_id_usuario", idAdministrador);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    administrador = new Administrador();
                    administrador.setIdUsuario(rs.getInt("id_usuario"));
                    administrador.setDni(rs.getString("dni"));
                    administrador.setNombre(rs.getString("nombre"));
                    administrador.setApellidoPaterno(rs.getString("apellido_paterno"));
                    administrador.setApellidoMaterno(rs.getString("apellido_materno"));
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
        String sql = "{call LISTAR_ADMINISTRADOR_TODOS()}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                Administrador administrador = new Administrador();
                administrador.setIdUsuario(rs.getInt("id_usuario"));
                administrador.setDni(rs.getString("dni"));
                administrador.setNombre(rs.getString("nombre"));
                administrador.setApellidoPaterno(rs.getString("apellido_paterno"));
                    administrador.setApellidoMaterno(rs.getString("apellido_materno"));
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
