package pe.edu.pucp.softprog.services.rest.receta;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.com.mascovet.receta.bo.IMedicamentoBO;
import pe.com.mascovet.receta.boi.MedicamentoBOImpl;
import pe.com.mascovet.receta.model.Medicamento;

import java.util.List;

@Path("MedicamentoRS")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MedicamentoRS {

    private IMedicamentoBO boMedicamento;

    public MedicamentoRS() {
        boMedicamento = new MedicamentoBOImpl();
    }

    @GET
    @Path("listarTodosMedicamentos")
    public List<Medicamento> listarTodosMedicamentos() {
        return boMedicamento.listarTodos();
    }

    @POST
    @Path("insertarMedicamento")
    public int insertarMedicamento(Medicamento medicamento) {
        return boMedicamento.insertar(medicamento);
    }

    @GET
    @Path("buscarMedicamentoPorId/{id}")
    public Medicamento buscarMedicamentoPorId(@PathParam("id") int idMedicamento) {
        return boMedicamento.obtenerPorId(idMedicamento);
    }

    @PUT
    @Path("modificarMedicamento")
    public int modificarMedicamento(Medicamento medicamento) {
        return boMedicamento.modificar(medicamento);
    }

    @DELETE
    @Path("eliminarMedicamento/{id}")
    public int eliminarMedicamento(@PathParam("id") int idMedicamento) {
        return boMedicamento.eliminar(idMedicamento);
    }
}
