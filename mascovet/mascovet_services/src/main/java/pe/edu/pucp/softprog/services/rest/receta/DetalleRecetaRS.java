package pe.edu.pucp.softprog.services.rest.receta;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.com.mascovet.receta.bo.IDetalleRecetaBO;
import pe.com.mascovet.receta.boi.DetalleRecetaBOImpl;
import pe.com.mascovet.receta.model.DetalleReceta;

import java.util.List;

@Path("DetalleRecetaRS")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DetalleRecetaRS {

    private IDetalleRecetaBO boDetalleReceta;

    public DetalleRecetaRS() {
        boDetalleReceta = new DetalleRecetaBOImpl();
    }

    @GET
    @Path("listarTodosDetallesReceta")
    public List<DetalleReceta> listarTodosDetallesReceta() {
        return boDetalleReceta.listarTodos();
    }

    @POST
    @Path("insertarDetalleReceta")
    public int insertarDetalleReceta(DetalleReceta detalleReceta) {
        return boDetalleReceta.insertar(detalleReceta);
    }

    @GET
    @Path("buscarDetalleRecetaPorId/{id}")
    public DetalleReceta buscarDetalleRecetaPorId(@PathParam("id") int idDetalleReceta) {
        return boDetalleReceta.obtenerPorId(idDetalleReceta);
    }

    @PUT
    @Path("modificarDetalleReceta")
    public int modificarDetalleReceta(DetalleReceta detalleReceta) {
        return boDetalleReceta.modificar(detalleReceta);
    }

    @DELETE
    @Path("eliminarDetalleReceta/{id}")
    public int eliminarDetalleReceta(@PathParam("id") int idDetalleReceta) {
        return boDetalleReceta.eliminar(idDetalleReceta);
    }
}
