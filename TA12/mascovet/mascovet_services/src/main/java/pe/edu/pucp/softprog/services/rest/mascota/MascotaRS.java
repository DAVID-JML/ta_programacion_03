package pe.edu.pucp.softprog.services.rest.mascota;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.com.mascovet.mascota.bo.IMascotaBO;
import pe.com.mascovet.mascota.boi.MascotaBOImpl;
import pe.com.mascovet.mascota.model.Mascota;

import java.util.List;

@Path("MascotaRS")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MascotaRS {

    private IMascotaBO boMascota;

    public MascotaRS() {
        boMascota = new MascotaBOImpl();
    }

    @GET
    @Path("listarTodosMascotas")
    public List<Mascota> listarTodosMascotas() {
        return boMascota.listarTodos();
    }

    @POST
    @Path("insertarMascota")
    public int insertarMascota(Mascota mascota) {
        return boMascota.insertar(mascota);
    }

    @GET
    @Path("buscarMascotaPorId/{id}")
    public Mascota buscarMascotaPorId(@PathParam("id") int idMascota) {
        return boMascota.obtenerPorId(idMascota);
    }

    @PUT
    @Path("modificarMascota")
    public int modificarMascota(Mascota mascota) {
        return boMascota.modificar(mascota);
    }

    @DELETE
    @Path("eliminarMascota/{id}")
    public int eliminarMascota(@PathParam("id") int idMascota) {
        return boMascota.eliminar(idMascota);
    }
}
