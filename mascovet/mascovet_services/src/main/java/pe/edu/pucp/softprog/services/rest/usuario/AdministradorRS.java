package pe.edu.pucp.softprog.services.rest.usuario;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.com.mascovet.usuario.bo.IAdministradorBO;
import pe.com.mascovet.usuario.boi.AdministradorBOImpl;
import pe.com.mascovet.usuario.model.Administrador;

import java.util.List;

@Path("AdministradorRS")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdministradorRS {

    private IAdministradorBO boAdministrador;

    public AdministradorRS() {
        boAdministrador = new AdministradorBOImpl();
    }

    @GET
    @Path("listarTodosAdministradores")
    public List<Administrador> listarTodosAdministradores() {
        return boAdministrador.listarTodos();
    }

    @POST
    @Path("insertarAdministrador")
    public int insertarAdministrador(Administrador administrador) {
        return boAdministrador.insertar(administrador);
    }

    @GET
    @Path("buscarAdministradorPorId/{id}")
    public Administrador buscarAdministradorPorId(@PathParam("id") int idAdministrador) {
        return boAdministrador.obtenerPorId(idAdministrador);
    }

    @PUT
    @Path("modificarAdministrador")
    public int modificarAdministrador(Administrador administrador) {
        return boAdministrador.modificar(administrador);
    }

    @DELETE
    @Path("eliminarAdministrador/{id}")
    public int eliminarAdministrador(@PathParam("id") int idAdministrador) {
        return boAdministrador.eliminar(idAdministrador);
    }
}
