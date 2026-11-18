package pe.com.mascovet.usuario.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.usuario.dao.AutenticacionDAO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AutenticacionImpl implements AutenticacionDAO {

    private static final String ROL_CLIENTE = "CLIENTE";

    @Override
    public DatosLogin buscarClienteParaLogin(String correo) {
        return buscar(
                "{call BUSCAR_CLIENTE_PARA_LOGIN(?)}",
                correo,
                ROL_CLIENTE, // el procedimiento del cliente no devuelve rol: es fijo
                "Error al buscar el cliente para iniciar sesión"
        );
    }

    @Override
    public DatosLogin buscarPersonalParaLogin(String nombreUsuario) {
        return buscar(
                "{call BUSCAR_PERSONAL_PARA_LOGIN(?)}",
                nombreUsuario,
                null, // el rol del personal viene de la base de datos
                "Error al buscar el personal para iniciar sesión"
        );
    }

    @Override
    public String obtenerHashPorId(int idUsuario) {
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall("{call OBTENER_HASH_USUARIO(?)}")) {

            cs.setInt(1, idUsuario);

            try (ResultSet rs = cs.executeQuery()) {
                return rs.next() ? rs.getString("hash_contrasena") : null;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al obtener la contraseña del usuario", ex);
        }
    }

    @Override
    public boolean actualizarHash(int idUsuario, String nuevoHash) {
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall("{call ACTUALIZAR_CONTRASENA_USUARIO(?, ?)}")) {

            cs.setInt(1, idUsuario);
            cs.setString(2, nuevoHash);

            try (ResultSet rs = cs.executeQuery()) {
                return rs.next() && rs.getInt("filas") > 0;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al actualizar la contraseña del usuario", ex);
        }
    }
    private DatosLogin buscar(String sql, String valor, String rolFijo, String mensajeError) {
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, valor);

            try (ResultSet rs = cs.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return new DatosLogin(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("nombre_usuario"),
                        rs.getString("hash_contrasena"),
                        rolFijo != null ? rolFijo : rs.getString("rol")
                );
            }
        } catch (SQLException ex) {
            throw new RuntimeException(mensajeError, ex);
        }
    }
}
