package pe.edu.pucp.softprog.services.rest.usuario;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.com.mascovet.usuario.bo.ICuentaUsuarioBO;
import pe.com.mascovet.usuario.boi.CuentaUsuarioBOImpl;
import pe.com.mascovet.usuario.model.CuentaUsuario;
import java.util.List;

@Path("CuentaUsuarioRS")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CuentaUsuarioRS {
    private ICuentaUsuarioBO cuentaUsuarioBO;

    public CuentaUsuarioRS() { cuentaUsuarioBO = new CuentaUsuarioBOImpl(); }

    @POST
    @Path("verificar")
    public boolean verificar(CuentaUsuario cuentaUsuario) {
        return cuentaUsuarioBO.verificar(cuentaUsuario);
    }

     
    @POST
    @Path("autenticar")
    public CuentaUsuario autenticar(CuentaUsuario cuentaUsuario) {
        return cuentaUsuarioBO.autenticar(cuentaUsuario);
    }

     
     
    @POST
    @Path("cambiarContrasena")
    public int cambiarContrasena(CuentaUsuario cuentaUsuario) {
        return cuentaUsuarioBO.cambiarContrasena(cuentaUsuario);
    }

    @POST
    @Path("insertarCuenta")
    public int insertarCuenta(CuentaUsuario cuentaUsuario) {
        return cuentaUsuarioBO.insertar(cuentaUsuario);
    }
    @PUT
    @Path("modificarCuenta")
    public int modificarCuenta(CuentaUsuario cuentaUsuario) {
        return cuentaUsuarioBO.modificar(cuentaUsuario);
    }
    @DELETE
    @Path("eliminarCuenta/{id}")
    public int eliminarCuenta(@PathParam("id") int id) {
        return cuentaUsuarioBO.eliminar(id);
    }
    @GET
    @Path("listarTodos")
    public List<CuentaUsuario> listarTodos() {
        return cuentaUsuarioBO.listarTodos();
    }
    @GET
    @Path("buscarPorId/{id}")
    public CuentaUsuario buscarPorId(@PathParam("id") int id) {
        return cuentaUsuarioBO.obtenerPorId(id);
    }
}
