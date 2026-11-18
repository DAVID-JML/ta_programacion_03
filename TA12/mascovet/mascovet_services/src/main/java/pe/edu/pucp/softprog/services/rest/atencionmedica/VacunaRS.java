package pe.edu.pucp.softprog.services.rest.atencionmedica;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.com.mascovet.atencionmedica.bo.IVacunaBO;
import pe.com.mascovet.atencionmedica.boi.VacunaBOImpl;
import pe.com.mascovet.atencionmedica.model.Vacuna;

import java.util.List;

@Path("VacunaRS")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VacunaRS {

    private IVacunaBO boVacuna;

    public VacunaRS() {
        boVacuna = new VacunaBOImpl();
    }

    @GET
    @Path("listarTodosVacunas")
    public List<Vacuna> listarTodosVacunas() {
        return boVacuna.listarTodos();
    }

    @POST
    @Path("insertarVacuna")
    public int insertarVacuna(Vacuna vacuna) {
        return boVacuna.insertar(vacuna);
    }

    @GET
    @Path("buscarVacunaPorId/{id}")
    public Vacuna buscarVacunaPorId(@PathParam("id") int idVacuna) {
        return boVacuna.obtenerPorId(idVacuna);
    }

    @PUT
    @Path("modificarVacuna")
    public int modificarVacuna(Vacuna vacuna) {
        return boVacuna.modificar(vacuna);
    }

    @DELETE
    @Path("eliminarVacuna/{id}")
    public int eliminarVacuna(@PathParam("id") int idVacuna) {
        return boVacuna.eliminar(idVacuna);
    }
}
