package pe.edu.pucp.softprog.services.rest.atencionmedica;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.com.mascovet.atencionmedica.bo.IControlBO;
import pe.com.mascovet.atencionmedica.boi.ControlBOImpl;
import pe.com.mascovet.atencionmedica.model.Control;

import java.util.List;

@Path("ControlRS")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ControlRS {

    private IControlBO boControl;

    public ControlRS() {
        boControl = new ControlBOImpl();
    }

    @GET
    @Path("listarTodosControles")
    public List<Control> listarTodosControles() {
        return boControl.listarTodos();
    }

    @POST
    @Path("insertarControl")
    public int insertarControl(Control control) {
        return boControl.insertar(control);
    }

    @GET
    @Path("buscarControlPorId/{id}")
    public Control buscarControlPorId(@PathParam("id") int idControl) {
        return boControl.obtenerPorId(idControl);
    }

    @PUT
    @Path("modificarControl")
    public int modificarControl(Control control) {
        return boControl.modificar(control);
    }

    @DELETE
    @Path("eliminarControl/{id}")
    public int eliminarControl(@PathParam("id") int idControl) {
        return boControl.eliminar(idControl);
    }
}
