package pe.edu.pucp.softprog.services.rest.atencionmedica;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.com.mascovet.atencionmedica.bo.ICirugiaBO;
import pe.com.mascovet.atencionmedica.boi.CirugiaBOImpl;
import pe.com.mascovet.atencionmedica.model.Cirugia;

import java.util.List;

@Path("CirugiaRS")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CirugiaRS {

    private ICirugiaBO boCirugia;

    public CirugiaRS() {
        boCirugia = new CirugiaBOImpl();
    }

    @GET
    @Path("listarTodosCirugias")
    public List<Cirugia> listarTodosCirugias() {
        return boCirugia.listarTodos();
    }

    @POST
    @Path("insertarCirugia")
    public int insertarCirugia(Cirugia cirugia) {
        return boCirugia.insertar(cirugia);
    }

    @GET
    @Path("buscarCirugiaPorId/{id}")
    public Cirugia buscarCirugiaPorId(@PathParam("id") int idCirugia) {
        return boCirugia.obtenerPorId(idCirugia);
    }

    @PUT
    @Path("modificarCirugia")
    public int modificarCirugia(Cirugia cirugia) {
        return boCirugia.modificar(cirugia);
    }

    @DELETE
    @Path("eliminarCirugia/{id}")
    public int eliminarCirugia(@PathParam("id") int idCirugia) {
        return boCirugia.eliminar(idCirugia);
    }
}
