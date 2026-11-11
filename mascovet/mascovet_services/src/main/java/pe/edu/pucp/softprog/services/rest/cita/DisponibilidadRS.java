package pe.edu.pucp.softprog.services.rest.cita;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.com.mascovet.cita.bo.IDisponibilidadBO;
import pe.com.mascovet.cita.boi.DisponibilidadBOImpl;
import pe.com.mascovet.cita.model.HorarioAtencion;

import java.util.List;

@Path("DisponibilidadRS")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DisponibilidadRS {

    private IDisponibilidadBO boDisponibilidad;

    public DisponibilidadRS() {
        boDisponibilidad = new DisponibilidadBOImpl();
    }

    @GET
    @Path("listarDisponibles")
    public List<HorarioAtencion> listarDisponibles(@QueryParam("fecha") String fecha,
                                                   @QueryParam("especialidad") String especialidad,
                                                   @QueryParam("idVeterinario") int idVeterinario) {
        return boDisponibilidad.listarDisponibles(fecha, especialidad, idVeterinario);
    }
}
