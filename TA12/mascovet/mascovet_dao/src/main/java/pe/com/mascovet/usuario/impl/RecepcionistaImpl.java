package pe.com.mascovet.usuario.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.usuario.dao.RecepcionistaDAO;
import pe.com.mascovet.usuario.model.Recepcionista;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.Types;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RecepcionistaImpl implements RecepcionistaDAO {

    @Override
    public int insertar(Recepcionista recepcionista) {
        String sql = "{call INSERTAR_RECEPCIONISTA(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_id_usuario", Types.INTEGER);
                cs.setString("_dni", recepcionista.getDni());
                cs.setString("_nombre", recepcionista.getNombre());
                cs.setString("_apellido_paterno", recepcionista.getApellidoPaterno());
                cs.setString("_apellido_materno", recepcionista.getApellidoMaterno());
                cs.setString("_nombre_usuario", recepcionista.getNombreUsuario());
                cs.setString("_contrasena", recepcionista.getContrasena());
                cs.setBoolean("_activo", recepcionista.isActivo());
                cs.setString("_turno", recepcionista.getTurno());
                cs.setString("_correo", recepcionista.getCorreo());
                cs.setString("_telefono", recepcionista.getTelefono());
                cs.executeUpdate();
                int idGenerado = cs.getInt("_id_usuario");
                recepcionista.setIdUsuario(idGenerado);
                return idGenerado;
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR RECEPCIONISTA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Recepcionista recepcionista) {
        String sql = "{call MODIFICAR_RECEPCIONISTA(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_resultado", Types.INTEGER);
                cs.setString("_dni", recepcionista.getDni());
                cs.setString("_nombre", recepcionista.getNombre());
                cs.setString("_apellido_paterno", recepcionista.getApellidoPaterno());
                cs.setString("_apellido_materno", recepcionista.getApellidoMaterno());
                cs.setString("_nombre_usuario", recepcionista.getNombreUsuario());
                cs.setString("_contrasena", recepcionista.getContrasena());
                cs.setBoolean("_activo", recepcionista.isActivo());
                cs.setInt("_id_usuario", recepcionista.getIdUsuario());
                cs.setString("_turno", recepcionista.getTurno());
                cs.setString("_correo", recepcionista.getCorreo());
                cs.setString("_telefono", recepcionista.getTelefono());
                cs.executeUpdate();
                return cs.getInt("_resultado");
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR RECEPCIONISTA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idRecepcionista) {
        String sql = "{call ELIMINAR_RECEPCIONISTA(?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_resultado", Types.INTEGER);
                cs.setInt("_id_usuario", idRecepcionista);
                cs.executeUpdate();
                return cs.getInt("_resultado");
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL ELIMINAR RECEPCIONISTA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Recepcionista obtenerPorId(int idRecepcionista) {
        Recepcionista recepcionista = null;
        String sql = "{call LISTAR_RECEPCIONISTA_X_ID(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt("_id_usuario", idRecepcionista);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    recepcionista = new Recepcionista();
                    recepcionista.setIdUsuario(rs.getInt("id_usuario"));
                    recepcionista.setDni(rs.getString("dni"));
                    recepcionista.setNombre(rs.getString("nombre"));
                    recepcionista.setApellidoPaterno(rs.getString("apellido_paterno"));
                    recepcionista.setApellidoMaterno(rs.getString("apellido_materno"));
                    recepcionista.setNombreUsuario(rs.getString("nombre_usuario"));
                    recepcionista.setContrasena(rs.getString("contrasena"));
                    recepcionista.setActivo(rs.getBoolean("activo"));
                    recepcionista.setTurno(rs.getString("turno"));
                    recepcionista.setCorreo(rs.getString("correo"));
                    recepcionista.setTelefono(rs.getString("telefono"));
                }
            }
            return recepcionista;
        } catch (Exception ex) {
            System.out.println("ERROR AL OBTENER RECEPCIONISTA POR ID: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Recepcionista> listarTodos() {
        List<Recepcionista> recepcionistas = new ArrayList<>();
        String sql = "{call LISTAR_RECEPCIONISTA_TODOS()}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                Recepcionista recepcionista = new Recepcionista();
                recepcionista.setIdUsuario(rs.getInt("id_usuario"));
                recepcionista.setDni(rs.getString("dni"));
                recepcionista.setNombre(rs.getString("nombre"));
                recepcionista.setApellidoPaterno(rs.getString("apellido_paterno"));
                    recepcionista.setApellidoMaterno(rs.getString("apellido_materno"));
                recepcionista.setNombreUsuario(rs.getString("nombre_usuario"));
                recepcionista.setContrasena(rs.getString("contrasena"));
                recepcionista.setActivo(rs.getBoolean("activo"));
                recepcionista.setTurno(rs.getString("turno"));
                    recepcionista.setCorreo(rs.getString("correo"));
                    recepcionista.setTelefono(rs.getString("telefono"));
                recepcionistas.add(recepcionista);
            }
            return recepcionistas;
        } catch (Exception ex) {
            System.out.println("ERROR AL LISTAR RECEPCIONISTAS: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
