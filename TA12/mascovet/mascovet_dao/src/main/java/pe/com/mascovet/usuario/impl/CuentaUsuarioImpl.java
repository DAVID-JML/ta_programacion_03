package pe.com.mascovet.usuario.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.usuario.dao.CuentaUsuarioDAO;
import pe.com.mascovet.usuario.model.CuentaUsuario;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CuentaUsuarioImpl implements CuentaUsuarioDAO {

     
    @Override
    public boolean verificar(CuentaUsuario cuentaUsuario) {
        boolean verificacion = false;
        String sql = "{call VERIFICAR_CUENTA_USUARIO(?,?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setString("_username", cuentaUsuario.getUsername());
            cs.setString("_password", cuentaUsuario.getPassword());
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) verificacion = true;
            }
            return verificacion;
        } catch (Exception ex) {
            System.out.println("ERROR AL VERIFICAR CUENTA DE USUARIO: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public CuentaUsuario autenticar(CuentaUsuario cuentaUsuario) {
        String sql = "{call VERIFICAR_CUENTA_USUARIO(?,?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setString("_username", cuentaUsuario.getUsername());
            cs.setString("_password", cuentaUsuario.getPassword());
            try (ResultSet rs = cs.executeQuery()) {
                if (!rs.next()) return null;
                CuentaUsuario cuenta = new CuentaUsuario();
                cuenta.setIdCuentaUsuario(rs.getInt("id_cuenta_usuario"));
                cuenta.setIdUsuario(rs.getInt("fid_usuario"));
                cuenta.setUsername(rs.getString("username"));
                cuenta.setActivo(rs.getBoolean("activo"));
                cuenta.setRol(rs.getString("rol"));
                cuenta.setNombre(rs.getString("nombre"));
                cuenta.setUsuarioVinculado(rs.getString("usuario_vinculado"));
                 
                return cuenta;
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL VERIFICAR CUENTA DE USUARIO: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int cambiarContrasena(CuentaUsuario cuentaUsuario) {
        String sql = "{call CAMBIAR_CONTRASENA_CUENTA_USUARIO(?,?,?,?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter("_resultado", Types.INTEGER);
            cs.setInt("_fid_usuario", cuentaUsuario.getIdUsuario());
            cs.setString("_password_actual", cuentaUsuario.getPassword());
            cs.setString("_password_nueva", cuentaUsuario.getNuevaPassword());
            cs.executeUpdate();
            return cs.getInt("_resultado");
        } catch (Exception ex) {
            System.out.println("ERROR AL CAMBIAR CONTRASEÑA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int insertar(CuentaUsuario cuentaUsuario) {
        String sql = "{call INSERTAR_CUENTA_USUARIO(?,?,?,?,?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter("_id_cuenta_usuario", Types.INTEGER);
            cs.setInt("_fid_usuario", cuentaUsuario.getIdUsuario());
            cs.setString("_username", cuentaUsuario.getUsername());
            cs.setString("_password", cuentaUsuario.getPassword());
            cs.setBoolean("_activo", cuentaUsuario.isActivo());
            cs.executeUpdate();
            int id = cs.getInt("_id_cuenta_usuario");
            cuentaUsuario.setIdCuentaUsuario(id);
            cuentaUsuario.setPassword(null);
            return id;
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR CUENTA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(CuentaUsuario cuentaUsuario) {
        String sql = "{call MODIFICAR_CUENTA_USUARIO(?,?,?,?,?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter("_resultado", Types.INTEGER);
            cs.setInt("_id_cuenta_usuario", cuentaUsuario.getIdCuentaUsuario());
            cs.setString("_username", cuentaUsuario.getUsername());
            cs.setString("_password", cuentaUsuario.getPassword());
            cs.setBoolean("_activo", cuentaUsuario.isActivo());
            cs.executeUpdate();
            cuentaUsuario.setPassword(null);
            return cs.getInt("_resultado");
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR CUENTA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idCuentaUsuario) {
        String sql = "{call ELIMINAR_CUENTA_USUARIO(?,?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter("_resultado", Types.INTEGER);
            cs.setInt("_id_cuenta_usuario", idCuentaUsuario);
            cs.executeUpdate();
            return cs.getInt("_resultado");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public CuentaUsuario obtenerPorId(int idCuentaUsuario) {
        String sql = "{call LISTAR_CUENTA_USUARIO_X_ID(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt("_id_cuenta_usuario", idCuentaUsuario);
            try (ResultSet rs = cs.executeQuery()) {
                if (!rs.next()) return null;
                CuentaUsuario cuenta = new CuentaUsuario();
                cuenta.setIdCuentaUsuario(rs.getInt("id_cuenta_usuario"));
                cuenta.setIdUsuario(rs.getInt("fid_usuario"));
                cuenta.setUsername(rs.getString("username"));
                cuenta.setActivo(rs.getBoolean("activo"));
                cuenta.setRol(rs.getString("rol"));
                cuenta.setNombre(rs.getString("nombre"));
                cuenta.setUsuarioVinculado(rs.getString("usuario_vinculado"));
                return cuenta;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<CuentaUsuario> listarTodos() {
        List<CuentaUsuario> cuentas = new ArrayList<>();
        String sql = "{call LISTAR_CUENTA_USUARIO_TODOS()}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                CuentaUsuario cuenta = new CuentaUsuario();
                cuenta.setIdCuentaUsuario(rs.getInt("id_cuenta_usuario"));
                cuenta.setIdUsuario(rs.getInt("fid_usuario"));
                cuenta.setUsername(rs.getString("username"));
                cuenta.setActivo(rs.getBoolean("activo"));
                cuenta.setRol(rs.getString("rol"));
                cuenta.setNombre(rs.getString("nombre"));
                cuenta.setUsuarioVinculado(rs.getString("usuario_vinculado"));
                cuentas.add(cuenta);
            }
            return cuentas;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
