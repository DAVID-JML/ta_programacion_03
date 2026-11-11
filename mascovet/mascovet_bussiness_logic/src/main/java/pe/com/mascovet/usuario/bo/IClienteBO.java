package pe.com.mascovet.usuario.bo;
import pe.com.mascovet.bo.IBaseBO;
import pe.com.mascovet.usuario.model.Cliente;
public interface IClienteBO extends IBaseBO<Cliente> {
    SesionCliente autenticar(String correo, String contrasena);

    record SesionCliente(
            int idUsuario,
            String nombre,
            String nombreUsuario,
            String rol
    ) {}
}
