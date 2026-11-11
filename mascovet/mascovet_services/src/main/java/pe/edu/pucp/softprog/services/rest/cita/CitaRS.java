package pe.edu.pucp.softprog.services.rest.cita;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.com.mascovet.cita.bo.ICitaBO;
import pe.com.mascovet.cita.boi.CitaBOImpl;
import pe.com.mascovet.cita.model.Cita;

import java.util.List;

@Path("CitaRS")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CitaRS {

    private ICitaBO boCita;

    public CitaRS() {
        boCita = new CitaBOImpl();
    }

    @GET
    @Path("listarTodosCitas")
    public List<Cita> listarTodosCitas() {
        return boCita.listarTodos();
    }

    @POST
    @Path("insertarCita")
    public int insertarCita(Cita cita) {
        return boCita.insertar(cita);
    }

    @GET
    @Path("buscarCitaPorId/{id}")
    public Cita buscarCitaPorId(@PathParam("id") int idCita) {
        return boCita.obtenerPorId(idCita);
    }

    @PUT
    @Path("modificarCita")
    public int modificarCita(Cita cita) {
        return boCita.modificar(cita);
    }

    @DELETE
    @Path("eliminarCita/{id}")
    public int eliminarCita(@PathParam("id") int idCita) {
        return boCita.eliminar(idCita);
    }

    @POST
    @Path("reservarCita")
    public int reservarCita(Cita cita) {
        return boCita.insertar(cita);
    }
}
