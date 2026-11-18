package pe.edu.pucp.softprog.services.rest.atencionmedica;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.com.mascovet.atencionmedica.bo.IVacunacionBO;
import pe.com.mascovet.atencionmedica.boi.VacunacionBOImpl;
import pe.com.mascovet.atencionmedica.model.Vacunacion;

import java.util.List;

@Path("VacunacionRS")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VacunacionRS {

    private IVacunacionBO boVacunacion;

    public VacunacionRS() {
        boVacunacion = new VacunacionBOImpl();
    }

    @GET
    @Path("listarTodosVacunaciones")
    public List<Vacunacion> listarTodosVacunaciones() {
        return boVacunacion.listarTodos();
    }

    @POST
    @Path("insertarVacunacion")
    public int insertarVacunacion(Vacunacion vacunacion) {
        return boVacunacion.insertar(vacunacion);
    }

    @GET
    @Path("buscarVacunacionPorId/{id}")
    public Vacunacion buscarVacunacionPorId(@PathParam("id") int idVacunacion) {
        return boVacunacion.obtenerPorId(idVacunacion);
    }

    @PUT
    @Path("modificarVacunacion")
    public int modificarVacunacion(Vacunacion vacunacion) {
        return boVacunacion.modificar(vacunacion);
    }

    @DELETE
    @Path("eliminarVacunacion/{id}")
    public int eliminarVacunacion(@PathParam("id") int idVacunacion) {
        return boVacunacion.eliminar(idVacunacion);
    }

    @POST
    @Path("insertarConVacunas")
    public int insertarConVacunas(Vacunacion vacunacion) {
        return boVacunacion.insertarConVacunas(vacunacion);
    }
}
