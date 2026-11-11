package pe.edu.pucp.softprog.services.rest.cita;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.com.mascovet.cita.bo.IHorarioAtencionBO;
import pe.com.mascovet.cita.boi.HorarioAtencionBOImpl;
import pe.com.mascovet.cita.model.HorarioAtencion;

import java.util.List;

@Path("HorarioAtencionRS")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HorarioAtencionRS {

    private IHorarioAtencionBO boHorarioAtencion;

    public HorarioAtencionRS() {
        boHorarioAtencion = new HorarioAtencionBOImpl();
    }

    @GET
    @Path("listarTodosHorariosAtencion")
    public List<HorarioAtencion> listarTodosHorariosAtencion() {
        return boHorarioAtencion.listarTodos();
    }

    @POST
    @Path("insertarHorarioAtencion")
    public int insertarHorarioAtencion(HorarioAtencion horarioAtencion) {
        return boHorarioAtencion.insertar(horarioAtencion);
    }

    @GET
    @Path("buscarHorarioAtencionPorId/{id}")
    public HorarioAtencion buscarHorarioAtencionPorId(@PathParam("id") int idHorarioAtencion) {
        return boHorarioAtencion.obtenerPorId(idHorarioAtencion);
    }

    @PUT
    @Path("modificarHorarioAtencion")
    public int modificarHorarioAtencion(HorarioAtencion horarioAtencion) {
        return boHorarioAtencion.modificar(horarioAtencion);
    }

    @DELETE
    @Path("eliminarHorarioAtencion/{id}")
    public int eliminarHorarioAtencion(@PathParam("id") int idHorarioAtencion) {
        return boHorarioAtencion.eliminar(idHorarioAtencion);
    }
}
