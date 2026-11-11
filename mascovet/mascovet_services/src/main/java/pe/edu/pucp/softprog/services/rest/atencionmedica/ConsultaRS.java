package pe.edu.pucp.softprog.services.rest.atencionmedica;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.com.mascovet.atencionmedica.bo.IConsultaBO;
import pe.com.mascovet.atencionmedica.boi.ConsultaBOImpl;
import pe.com.mascovet.atencionmedica.model.Consulta;

import java.util.List;

@Path("ConsultaRS")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConsultaRS {

    private IConsultaBO boConsulta;

    public ConsultaRS() {
        boConsulta = new ConsultaBOImpl();
    }

    @GET
    @Path("listarTodosConsultas")
    public List<Consulta> listarTodosConsultas() {
        return boConsulta.listarTodos();
    }

    @POST
    @Path("insertarConsulta")
    public int insertarConsulta(Consulta consulta) {
        return boConsulta.insertar(consulta);
    }

    @GET
    @Path("buscarConsultaPorId/{id}")
    public Consulta buscarConsultaPorId(@PathParam("id") int idConsulta) {
        return boConsulta.obtenerPorId(idConsulta);
    }

    @PUT
    @Path("modificarConsulta")
    public int modificarConsulta(Consulta consulta) {
        return boConsulta.modificar(consulta);
    }

    @DELETE
    @Path("eliminarConsulta/{id}")
    public int eliminarConsulta(@PathParam("id") int idConsulta) {
        return boConsulta.eliminar(idConsulta);
    }
}
