package pe.edu.pucp.softprog.services.rest.receta;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import pe.com.mascovet.receta.bo.IRecetaBO;
import pe.com.mascovet.receta.boi.RecetaBOImpl;
import pe.com.mascovet.receta.model.Receta;
import pe.com.mascovet.receta.model.DetalleReceta;

@Path("RecetaRS")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RecetaRS {

    private IRecetaBO boReceta;

    public RecetaRS() {
        boReceta = new RecetaBOImpl();
    }

    @GET
    @Path("listarTodosRecetas")
    public List<Receta> listarTodosRecetas() {
        return boReceta.listarTodos();
    }

    @POST
    @Path("insertarReceta")
    public int insertarReceta(Receta receta) {
        return boReceta.insertar(receta);
    }

    @GET
    @Path("buscarRecetaPorId/{id}")
    public Receta buscarRecetaPorId(
            @PathParam("id") int idReceta) {
        return boReceta.obtenerPorId(idReceta);
    }

    @PUT
    @Path("modificarReceta")
    public int modificarReceta(Receta receta) {
        return boReceta.modificar(receta);
    }

    @DELETE
    @Path("eliminarReceta/{id}")
    public int eliminarReceta(
            @PathParam("id") int idReceta) {
        return boReceta.eliminar(idReceta);
    }

    @POST
    @Path("insertarConDetalles/{idAtencion}/{tipoAtencion}")
    public int insertarConDetalles(
            @PathParam("idAtencion") int idAtencion,
            @PathParam("tipoAtencion") String tipoAtencion,
            @QueryParam("fecha") String fecha,
            @QueryParam("indicaciones") String indicaciones,
            List<DetalleReceta> detalles) {

        return boReceta.insertarConDetalles(
                idAtencion, tipoAtencion,
                fecha, indicaciones, detalles);
    }
}