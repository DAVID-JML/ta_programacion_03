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
import java.sql.CallableStatement;
import java.sql.Types;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UsuarioImpl implements UsuarioDAO {

    public int insertarUsuarioBase(Usuario usuario, Connection con) {
        String sql = "{call INSERTAR_USUARIO_BASE(?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter("_id_usuario", java.sql.Types.INTEGER);
            cs.setString("_dni", usuario.getDni());
            cs.setString("_nombre", usuario.getNombre());
            cs.setString("_apellido_paterno", usuario.getApellidoPaterno());
            cs.setString("_apellido_materno", usuario.getApellidoMaterno());
            cs.setString("_nombre_usuario", usuario.getNombreUsuario());
            cs.setString("_contrasena", usuario.getContrasena());
            cs.setBoolean("_activo", usuario.isActivo());
            cs.executeUpdate();
            usuario.setIdUsuario(cs.getInt("_id_usuario"));
            return usuario.getIdUsuario();
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR USUARIO: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    public int modificarUsuarioBase(Usuario usuario, Connection con) {
        String sql = "{call MODIFICAR_USUARIO_BASE(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter("_resultado", java.sql.Types.INTEGER);
            cs.setString("_dni", usuario.getDni());
            cs.setString("_nombre", usuario.getNombre());
            cs.setString("_apellido_paterno", usuario.getApellidoPaterno());
            cs.setString("_apellido_materno", usuario.getApellidoMaterno());
            cs.setString("_nombre_usuario", usuario.getNombreUsuario());
            cs.setString("_contrasena", usuario.getContrasena());
            cs.setBoolean("_activo", usuario.isActivo());
            cs.setInt("_id_usuario", usuario.getIdUsuario());
            cs.executeUpdate();
            return cs.getInt("_resultado");
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

        try (Connection con = DBManager.getInstance().getConnection()) {
            try (CallableStatement cs = con.prepareCall("{call ELIMINAR_USUARIO(?, ?)}")) {
                cs.registerOutParameter("_resultado", Types.INTEGER);
                cs.setInt("_id_usuario", idUsuario);
                cs.executeUpdate();
                return cs.getInt("_resultado");
            }
        } catch (Exception ex) {
            throw new RuntimeException("ERROR AL ELIMINAR LÓGICAMENTE USUARIO: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Usuario obtenerPorId(int idUsuario) {
        Usuario usuario = null;
        String sql = "{call LISTAR_USUARIO_X_ID(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt("_id_usuario", idUsuario);
            try (ResultSet rs = cs.executeQuery()) {
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
                        usuario.setApellidoPaterno(rs.getString("apellido_paterno"));
                    usuario.setApellidoMaterno(rs.getString("apellido_materno"));
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
        String sql = "{call LISTAR_USUARIO_TODOS()}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
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
                    usuario.setApellidoPaterno(rs.getString("apellido_paterno"));
                    usuario.setApellidoMaterno(rs.getString("apellido_materno"));
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

    @Override
    public UsuarioDAO.DatosLoginPersonal buscarPersonalParaLogin(
            String nombreUsuario) {

        String sql = "{call BUSCAR_PERSONAL_PARA_LOGIN(?)}";

        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, nombreUsuario);

            try (ResultSet rs = cs.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return new UsuarioDAO.DatosLoginPersonal(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("nombre_usuario"),
                        rs.getString("hash_contrasena"),
                        rs.getString("rol")
                );
            }
        } catch (java.sql.SQLException ex) {
            throw new RuntimeException(
                    "Error al buscar la cuenta del personal", ex
            );
        }
    }
}
