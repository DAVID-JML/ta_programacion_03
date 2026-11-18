package pe.edu.pucp.softprog.services.rest.usuario;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.com.mascovet.usuario.bo.IRecepcionistaBO;
import pe.com.mascovet.usuario.boi.RecepcionistaBOImpl;
import pe.com.mascovet.usuario.model.Recepcionista;

import java.util.List;

@Path("RecepcionistaRS")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RecepcionistaRS {

    private IRecepcionistaBO boRecepcionista;

    public RecepcionistaRS() {
        boRecepcionista = new RecepcionistaBOImpl();
    }

    @GET
    @Path("listarTodosRecepcionistas")
    public List<Recepcionista> listarTodosRecepcionistas() {
        return boRecepcionista.listarTodos();
    }

    @POST
    @Path("insertarRecepcionista")
    public int insertarRecepcionista(Recepcionista recepcionista) {
        return boRecepcionista.insertar(recepcionista);
    }

    @GET
    @Path("buscarRecepcionistaPorId/{id}")
    public Recepcionista buscarRecepcionistaPorId(@PathParam("id") int idRecepcionista) {
        return boRecepcionista.obtenerPorId(idRecepcionista);
    }

    @PUT
    @Path("modificarRecepcionista")
    public int modificarRecepcionista(Recepcionista recepcionista) {
        return boRecepcionista.modificar(recepcionista);
    }

    @DELETE
    @Path("eliminarRecepcionista/{id}")
    public int eliminarRecepcionista(@PathParam("id") int idRecepcionista) {
        return boRecepcionista.eliminar(idRecepcionista);
    }
}
