package pe.com.mascovet.usuario.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.config.TransactionContext;
import pe.com.mascovet.usuario.dao.RecepcionistaDAO;
import pe.com.mascovet.usuario.model.Recepcionista;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RecepcionistaImpl implements RecepcionistaDAO {

    private final UsuarioImpl usuarioDao = new UsuarioImpl();

    @Override
    public int insertar(Recepcionista recepcionista) {
        String sql = "INSERT INTO RECEPCIONISTA (id_usuario, turno) VALUES (?, ?)";
        try {
            Connection con = TransactionContext.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                int idUsuario = usuarioDao.insertarUsuarioBase(recepcionista, con);
                if (idUsuario <= 0) {
                    throw new RuntimeException("No se pudo generar el identificador del usuario");
                }
                ps.setInt(1, idUsuario);
                ps.setString(2, recepcionista.getTurno());
                ps.executeUpdate();
                recepcionista.setIdUsuario(idUsuario);
                return recepcionista.getIdUsuario();
            } catch (Exception ex) {
                System.out.println("ERROR AL INSERTAR RECEPCIONISTA: " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR RECEPCIONISTA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Recepcionista recepcionista) {
        String sql = "UPDATE RECEPCIONISTA SET turno=? WHERE id_usuario=?";
        try {
            Connection con = TransactionContext.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                int filasUsuario = usuarioDao.modificarUsuarioBase(recepcionista, con);
                ps.setString(1, recepcionista.getTurno());
                ps.setInt(2, recepcionista.getIdUsuario());
                int filasRecepcionista = ps.executeUpdate();
                return (filasUsuario > 0 && filasRecepcionista > 0) ? 1 : 0;
            } catch (Exception ex) {
                System.out.println("ERROR AL MODIFICAR RECEPCIONISTA: " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR RECEPCIONISTA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idRecepcionista) {
        String sql = "UPDATE USUARIO SET activo=0 WHERE id_usuario=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRecepcionista);
            return ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR AL ELIMINAR RECEPCIONISTA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Recepcionista obtenerPorId(int idRecepcionista) {
        Recepcionista recepcionista = null;
        String sql = "SELECT u.id_usuario, u.dni, u.nombre, u.apellido, u.nombre_usuario, u.contrasena, u.activo, r.turno " +
                "FROM USUARIO u INNER JOIN RECEPCIONISTA r ON u.id_usuario=r.id_usuario WHERE u.id_usuario=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRecepcionista);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    recepcionista = new Recepcionista();
                    recepcionista.setIdUsuario(rs.getInt("id_usuario"));
                    recepcionista.setDni(rs.getString("dni"));
                    recepcionista.setNombre(rs.getString("nombre"));
                    recepcionista.setApellido(rs.getString("apellido"));
                    recepcionista.setNombreUsuario(rs.getString("nombre_usuario"));
                    recepcionista.setContrasena(rs.getString("contrasena"));
                    recepcionista.setActivo(rs.getBoolean("activo"));
                    recepcionista.setTurno(rs.getString("turno"));
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
        String sql = "SELECT u.id_usuario, u.dni, u.nombre, u.apellido, u.nombre_usuario, u.contrasena, u.activo, r.turno " +
                "FROM USUARIO u INNER JOIN RECEPCIONISTA r ON u.id_usuario=r.id_usuario WHERE u.activo=1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Recepcionista recepcionista = new Recepcionista();
                recepcionista.setIdUsuario(rs.getInt("id_usuario"));
                recepcionista.setDni(rs.getString("dni"));
                recepcionista.setNombre(rs.getString("nombre"));
                recepcionista.setApellido(rs.getString("apellido"));
                recepcionista.setNombreUsuario(rs.getString("nombre_usuario"));
                recepcionista.setContrasena(rs.getString("contrasena"));
                recepcionista.setActivo(rs.getBoolean("activo"));
                recepcionista.setTurno(rs.getString("turno"));
                recepcionistas.add(recepcionista);
            }
            return recepcionistas;
        } catch (Exception ex) {
            System.out.println("ERROR AL LISTAR RECEPCIONISTAS: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
