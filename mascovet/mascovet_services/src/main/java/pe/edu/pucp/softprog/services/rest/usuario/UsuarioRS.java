package pe.edu.pucp.softprog.services.rest.usuario;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import pe.com.mascovet.usuario.bo.IClienteBO;
import pe.com.mascovet.usuario.boi.ClienteBOImpl;
import pe.com.mascovet.usuario.bo.IUsuarioBO;
import pe.com.mascovet.usuario.boi.UsuarioBOImpl;

import java.util.Map;

@Path("UsuarioRS")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioRS {

    private final IClienteBO boCliente = new ClienteBOImpl();
    private final IUsuarioBO boUsuario = new UsuarioBOImpl();

    @POST
    @Path("autenticar")
    public Response autenticar(SolicitudLogin solicitud) {
        if (solicitud == null
                || solicitud.correo == null || solicitud.correo.isBlank()
                || solicitud.contrasena == null || solicitud.contrasena.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("mensaje", "Ingresa el correo y la contraseña."))
                    .build();
        }

        IClienteBO.SesionCliente sesion =
                boCliente.autenticar(solicitud.correo, solicitud.contrasena);

        if (sesion == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("mensaje", "Correo o contraseña incorrectos."))
                    .build();
        }

        return Response.ok(Map.of(
                "idUsuario", sesion.idUsuario(),
                "nombre", sesion.nombre(),
                "nombreUsuario", sesion.nombreUsuario(),
                "rol", sesion.rol()
        )).build();
    }

    public static class SolicitudLogin {
        public String correo;
        public String contrasena;
    }

    @POST
    @Path("autenticarPersonal")
    public Response autenticarPersonal(SolicitudLoginPersonal solicitud) {
        if (solicitud == null
                || solicitud.nombreUsuario == null
                || solicitud.nombreUsuario.isBlank()
                || solicitud.contrasena == null
                || solicitud.contrasena.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("mensaje", "Ingresa el usuario y la contraseña."))
                    .build();
        }

        IUsuarioBO.SesionPersonal sesion = boUsuario.autenticarPersonal(
                solicitud.nombreUsuario,
                solicitud.contrasena
        );

        if (sesion == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("mensaje", "Usuario o contraseña incorrectos."))
                    .build();
        }

        return Response.ok(Map.of(
                "idUsuario", sesion.idUsuario(),
                "nombre", sesion.nombre(),
                "nombreUsuario", sesion.nombreUsuario(),
                "rol", sesion.rol()
        )).build();
    }

    public static class SolicitudLoginPersonal {
        public String nombreUsuario;
        public String contrasena;
    }
}