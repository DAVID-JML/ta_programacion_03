package pe.edu.pucp.softprog.services.rest.usuario;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import pe.com.mascovet.usuario.bo.IAutenticacionBO;
import pe.com.mascovet.usuario.boi.AutenticacionBOImpl;

import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
@Path("AutenticacionRS")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AutenticacionRS {

    private static final Logger LOGGER = Logger.getLogger(AutenticacionRS.class.getName());

    private final IAutenticacionBO boAutenticacion = new AutenticacionBOImpl();

    @POST
    @Path("cliente")
    public Response autenticarCliente(SolicitudLoginCliente solicitud) {
        if (solicitud == null || estaVacio(solicitud.correo) || estaVacio(solicitud.contrasena)) {
            return error(Response.Status.BAD_REQUEST, "Ingresa el correo y la contraseña.");
        }

        return responder(
                () -> boAutenticacion.autenticarCliente(solicitud.correo, solicitud.contrasena),
                "Correo o contraseña incorrectos.");
    }

    @POST
    @Path("personal")
    public Response autenticarPersonal(SolicitudLoginPersonal solicitud) {
        if (solicitud == null || estaVacio(solicitud.nombreUsuario) || estaVacio(solicitud.contrasena)) {
            return error(Response.Status.BAD_REQUEST, "Ingresa el usuario y la contraseña.");
        }

        return responder(
                () -> boAutenticacion.autenticarPersonal(solicitud.nombreUsuario, solicitud.contrasena),
                "Usuario o contraseña incorrectos.");
    }
    @POST
    @Path("cambiarContrasena")
    public Response cambiarContrasena(SolicitudCambioContrasena s) {
        if (s == null || s.idUsuario <= 0 || estaVacio(s.contrasenaActual) || estaVacio(s.contrasenaNueva))
            return error(Response.Status.BAD_REQUEST, "Completa todos los campos.");
        try {
            boolean ok = boAutenticacion.cambiarContrasena(s.idUsuario, s.contrasenaActual, s.contrasenaNueva);
            if (!ok) return error(Response.Status.UNAUTHORIZED, "La contraseña actual es incorrecta o la nueva no es válida.");
            return Response.ok(Map.of("mensaje", "Contraseña actualizada.")).build();
        } catch (RuntimeException ex) {
            LOGGER.log(Level.SEVERE, "Error al cambiar contraseña", ex);
            return error(Response.Status.INTERNAL_SERVER_ERROR, "No se pudo cambiar la contraseña.");
        }
    }

    public static class SolicitudCambioContrasena {
        public int idUsuario;
        public String contrasenaActual;
        public String contrasenaNueva;
    }
    // La respuesta se arma una sola vez para los dos logins.
    private Response responder(Supplier<IAutenticacionBO.Sesion> autenticacion, String mensajeNoValido) {
        IAutenticacionBO.Sesion sesion;
        try {
            sesion = autenticacion.get();
        } catch (RuntimeException ex) {
            // Se registra el error técnico, nunca los datos de la solicitud (RNF007).
            LOGGER.log(Level.SEVERE, "Error al autenticar", ex);
            return error(Response.Status.INTERNAL_SERVER_ERROR,
                    "No se pudo iniciar sesión. Inténtalo más tarde.");
        }

        if (sesion == null) {
            return error(Response.Status.UNAUTHORIZED, mensajeNoValido);
        }

        return Response.ok(Map.of(
                "idUsuario", sesion.idUsuario(),
                "nombre", sesion.nombre(),
                "nombreUsuario", sesion.nombreUsuario(),
                "rol", sesion.rol()
        )).build();
    }

    private static Response error(Response.Status estado, String mensaje) {
        return Response.status(estado).entity(Map.of("mensaje", mensaje)).build();
    }

    private static boolean estaVacio(String valor) {
        return valor == null || valor.isBlank();
    }

    public static class SolicitudLoginCliente {
        public String correo;
        public String contrasena;
    }

    public static class SolicitudLoginPersonal {
        public String nombreUsuario;
        public String contrasena;
    }
}

