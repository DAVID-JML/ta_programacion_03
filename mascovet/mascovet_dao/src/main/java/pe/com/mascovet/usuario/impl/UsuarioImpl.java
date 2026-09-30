package pe.com.mascovet.usuario.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.usuario.dao.UsuarioDAO;
import pe.com.mascovet.usuario.model.Administrador;
import pe.com.mascovet.usuario.model.Cliente;
import pe.com.mascovet.usuario.model.Recepcionista;
import pe.com.mascovet.usuario.model.Usuario;
import pe.com.mascovet.usuario.model.Veterinario;
import pe.com.mascovet.enums.model.Especialidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioImpl implements UsuarioDAO {

    public int insertarUsuarioBase(Usuario usuario, Connection con) {
        String sql = "INSERT INTO USUARIO (dni, nombre, apellido, nombre_usuario, contrasena, activo) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getDni());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getApellido());
            ps.setString(4, usuario.getNombreUsuario());
            ps.setString(5, usuario.getContrasena());
            ps.setBoolean(6, usuario.isActivo());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idUsuario = rs.getInt(1);
                    usuario.setIdUsuario(idUsuario);
                    return idUsuario;
                }
            }
            return 0;
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR USUARIO: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    public int modificarUsuarioBase(Usuario usuario, Connection con) {
        String sql = "UPDATE USUARIO SET dni=?, nombre=?, apellido=?, nombre_usuario=?, contrasena=?, activo=? WHERE id_usuario=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getDni());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getApellido());
            ps.setString(4, usuario.getNombreUsuario());
            ps.setString(5, usuario.getContrasena());
            ps.setBoolean(6, usuario.isActivo());
            ps.setInt(7, usuario.getIdUsuario());
            return ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR USUARIO: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int insertar(Usuario usuario) {
        try {
            if (usuario instanceof Cliente) {
                return new ClienteImpl().insertar((Cliente) usuario);
            }
            if (usuario instanceof Veterinario) {
                return new VeterinarioImpl().insertar((Veterinario) usuario);
            }
            if (usuario instanceof Recepcionista) {
                return new RecepcionistaImpl().insertar((Recepcionista) usuario);
            }
            if (usuario instanceof Administrador) {
                return new AdministradorImpl().insertar((Administrador) usuario);
            }
            throw new RuntimeException("Tipo de usuario no soportado");
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR USUARIO: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Usuario usuario) {
        try {
            if (usuario instanceof Cliente) {
                return new ClienteImpl().modificar((Cliente) usuario);
            }
            if (usuario instanceof Veterinario) {
                return new VeterinarioImpl().modificar((Veterinario) usuario);
            }
            if (usuario instanceof Recepcionista) {
                return new RecepcionistaImpl().modificar((Recepcionista) usuario);
            }
            if (usuario instanceof Administrador) {
                return new AdministradorImpl().modificar((Administrador) usuario);
            }
            throw new RuntimeException("Tipo de usuario no soportado");
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR USUARIO: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idUsuario) {
        String sql = "UPDATE USUARIO SET activo=0 WHERE id_usuario=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            return ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR AL ELIMINAR USUARIO: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Usuario obtenerPorId(int idUsuario) {
        Usuario usuario = null;
        String sql = "SELECT u.id_usuario, u.dni, u.nombre, u.apellido, u.nombre_usuario, u.contrasena, u.activo, " +
                "c.correo, c.telefono, v.num_colegiatura, v.especialidad, r.turno, a.cargo, " +
                "c.id_usuario AS id_cliente, v.id_usuario AS id_veterinario, r.id_usuario AS id_recepcionista, a.id_usuario AS id_administrador " +
                "FROM USUARIO u " +
                "LEFT JOIN CLIENTE c ON u.id_usuario=c.id_usuario " +
                "LEFT JOIN VETERINARIO v ON u.id_usuario=v.id_usuario " +
                "LEFT JOIN RECEPCIONISTA r ON u.id_usuario=r.id_usuario " +
                "LEFT JOIN ADMINISTRADOR a ON u.id_usuario=a.id_usuario " +
                "WHERE u.id_usuario=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    if (rs.getObject("id_cliente") != null) {
                        Cliente cliente = new Cliente();
                        cliente.setCorreo(rs.getString("correo"));
                        cliente.setTelefono(rs.getString("telefono"));
                        usuario = cliente;
                    } else if (rs.getObject("id_veterinario") != null) {
                        Veterinario veterinario = new Veterinario();
                        veterinario.setNumeroColegiatura(rs.getString("num_colegiatura"));
                        String especialidad = rs.getString("especialidad");
                        if (especialidad != null) veterinario.setEspecialidad(Especialidad.valueOf(especialidad));
                        usuario = veterinario;
                    } else if (rs.getObject("id_recepcionista") != null) {
                        Recepcionista recepcionista = new Recepcionista();
                        recepcionista.setTurno(rs.getString("turno"));
                        usuario = recepcionista;
                    } else if (rs.getObject("id_administrador") != null) {
                        Administrador administrador = new Administrador();
                        administrador.setCargo(rs.getString("cargo"));
                        usuario = administrador;
                    }

                    if (usuario != null) {
                        usuario.setIdUsuario(rs.getInt("id_usuario"));
                        usuario.setDni(rs.getString("dni"));
                        usuario.setNombre(rs.getString("nombre"));
                        usuario.setApellido(rs.getString("apellido"));
                        usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                        usuario.setContrasena(rs.getString("contrasena"));
                        usuario.setActivo(rs.getBoolean("activo"));
                    }
                }
            }
            return usuario;
        } catch (Exception ex) {
            System.out.println("ERROR AL OBTENER USUARIO POR ID: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.dni, u.nombre, u.apellido, u.nombre_usuario, u.contrasena, u.activo, " +
                "c.correo, c.telefono, v.num_colegiatura, v.especialidad, r.turno, a.cargo, " +
                "c.id_usuario AS id_cliente, v.id_usuario AS id_veterinario, r.id_usuario AS id_recepcionista, a.id_usuario AS id_administrador " +
                "FROM USUARIO u " +
                "LEFT JOIN CLIENTE c ON u.id_usuario=c.id_usuario " +
                "LEFT JOIN VETERINARIO v ON u.id_usuario=v.id_usuario " +
                "LEFT JOIN RECEPCIONISTA r ON u.id_usuario=r.id_usuario " +
                "LEFT JOIN ADMINISTRADOR a ON u.id_usuario=a.id_usuario " +
                "WHERE u.activo=1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario usuario = null;
                if (rs.getObject("id_cliente") != null) {
                    Cliente cliente = new Cliente();
                    cliente.setCorreo(rs.getString("correo"));
                    cliente.setTelefono(rs.getString("telefono"));
                    usuario = cliente;
                } else if (rs.getObject("id_veterinario") != null) {
                    Veterinario veterinario = new Veterinario();
                    veterinario.setNumeroColegiatura(rs.getString("num_colegiatura"));
                    String especialidad = rs.getString("especialidad");
                    if (especialidad != null) veterinario.setEspecialidad(Especialidad.valueOf(especialidad));
                    usuario = veterinario;
                } else if (rs.getObject("id_recepcionista") != null) {
                    Recepcionista recepcionista = new Recepcionista();
                    recepcionista.setTurno(rs.getString("turno"));
                    usuario = recepcionista;
                } else if (rs.getObject("id_administrador") != null) {
                    Administrador administrador = new Administrador();
                    administrador.setCargo(rs.getString("cargo"));
                    usuario = administrador;
                }

                if (usuario != null) {
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setDni(rs.getString("dni"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setApellido(rs.getString("apellido"));
                    usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                    usuario.setContrasena(rs.getString("contrasena"));
                    usuario.setActivo(rs.getBoolean("activo"));
                    usuarios.add(usuario);
                }
            }
            return usuarios;
        } catch (Exception ex) {
            System.out.println("ERROR AL LISTAR USUARIOS: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
