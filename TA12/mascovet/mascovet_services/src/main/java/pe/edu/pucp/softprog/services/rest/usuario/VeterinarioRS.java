package pe.edu.pucp.softprog.services.rest.usuario;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.com.mascovet.usuario.bo.IVeterinarioBO;
import pe.com.mascovet.usuario.boi.VeterinarioBOImpl;
import pe.com.mascovet.usuario.model.Veterinario;

import java.util.List;

@Path("VeterinarioRS")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VeterinarioRS {

    private IVeterinarioBO boVeterinario;

    public VeterinarioRS() {
        boVeterinario = new VeterinarioBOImpl();
    }

    @GET
    @Path("listarTodosVeterinarios")
    public List<Veterinario> listarTodosVeterinarios() {
        return boVeterinario.listarTodos();
    }

    @POST
    @Path("insertarVeterinario")
    public int insertarVeterinario(Veterinario veterinario) {
        return boVeterinario.insertar(veterinario);
    }

    @GET
    @Path("buscarVeterinarioPorId/{id}")
    public Veterinario buscarVeterinarioPorId(@PathParam("id") int idVeterinario) {
        return boVeterinario.obtenerPorId(idVeterinario);
    }

    @PUT
    @Path("modificarVeterinario")
    public int modificarVeterinario(Veterinario veterinario) {
        return boVeterinario.modificar(veterinario);
    }

    @DELETE
    @Path("eliminarVeterinario/{id}")
    public int eliminarVeterinario(@PathParam("id") int idVeterinario) {
        return boVeterinario.eliminar(idVeterinario);
    }

    @POST
    @Path("insertarConHorarios")
    public int insertarConHorarios(Veterinario veterinario) {
        return boVeterinario.insertarConHorarios(veterinario);
    }
}
