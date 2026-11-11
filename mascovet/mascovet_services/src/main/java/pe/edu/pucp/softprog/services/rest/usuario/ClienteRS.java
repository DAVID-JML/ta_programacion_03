package pe.edu.pucp.softprog.services.rest.usuario;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.com.mascovet.usuario.bo.IClienteBO;
import pe.com.mascovet.usuario.boi.ClienteBOImpl;
import pe.com.mascovet.usuario.model.Cliente;

import java.util.List;

@Path("ClienteRS")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClienteRS {

    private IClienteBO boCliente;

    public ClienteRS() {
        boCliente = new ClienteBOImpl();
    }

    @GET
    @Path("listarTodosClientes")
    public List<Cliente> listarTodosClientes() {
        return boCliente.listarTodos();
    }

    @POST
    @Path("insertarCliente")
    public int insertarCliente(Cliente cliente) {
        return boCliente.insertar(cliente);
    }

    @GET
    @Path("buscarClientePorId/{id}")
    public Cliente buscarClientePorId(@PathParam("id") int idCliente) {
        return boCliente.obtenerPorId(idCliente);
    }

    @PUT
    @Path("modificarCliente")
    public int modificarCliente(Cliente cliente) {
        return boCliente.modificar(cliente);
    }

    @DELETE
    @Path("eliminarCliente/{id}")
    public int eliminarCliente(@PathParam("id") int idCliente) {
        return boCliente.eliminar(idCliente);
    }
}
