package pe.com.mascovet.usuario.boi;

import pe.com.mascovet.usuario.bo.IClienteBO;
import pe.com.mascovet.usuario.dao.ClienteDAO;
import pe.com.mascovet.usuario.impl.ClienteImpl;
import pe.com.mascovet.usuario.model.Cliente;

import java.util.List;

public class ClienteBOImpl implements IClienteBO {

    private ClienteDAO daoCliente;

    public ClienteBOImpl() {
        daoCliente = new ClienteImpl();
    }

    private void validarCliente(Cliente cliente) {
        if (cliente == null)
            throw new RuntimeException("El cliente que se quiere registrar es null");
        if (cliente.getDni() == null || cliente.getDni().trim().isEmpty())
            throw new RuntimeException("El DNI del cliente es obligatorio");
        if (cliente.getDni().length() > 9)
            throw new RuntimeException("El DNI no debe exceder los 9 caracteres");
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty())
            throw new RuntimeException("El nombre del cliente es obligatorio");
        if (cliente.getNombre().length() > 50)
            throw new RuntimeException("El nombre no debe exceder los 50 caracteres");
        if (cliente.getApellidoPaterno() == null || cliente.getApellidoPaterno().trim().isEmpty())
            throw new RuntimeException("El apellido del cliente es obligatorio");
        if (cliente.getApellidoPaterno().length() > 50)
            throw new RuntimeException("El apellido no debe exceder los 50 caracteres");
        if (cliente.getApellidoMaterno() == null || cliente.getApellidoMaterno().trim().isEmpty())
            throw new RuntimeException("El apellido materno es obligatorio");
        if (cliente.getApellidoMaterno().length() > 50)
            throw new RuntimeException("El apellido materno no debe exceder los 50 caracteres");
        if (cliente.getNombreUsuario() == null || cliente.getNombreUsuario().trim().isEmpty())
            throw new RuntimeException("El nombre de usuario es obligatorio");
        if (cliente.getNombreUsuario().length() > 100)
            throw new RuntimeException("El nombre de usuario no debe exceder los 100 caracteres");
        if (cliente.getContrasena() == null || cliente.getContrasena().trim().isEmpty())
            throw new RuntimeException("La contraseña es obligatoria");
        if (cliente.getContrasena().length() > 255)
            throw new RuntimeException("La contraseña no debe exceder los 255 caracteres");
        if (cliente.getCorreo() == null || cliente.getCorreo().trim().isEmpty())
            throw new RuntimeException("El correo del cliente es obligatorio");
        if (cliente.getCorreo().length() > 200)
            throw new RuntimeException("El correo no debe exceder los 200 caracteres");
        if (cliente.getTelefono() == null || cliente.getTelefono().trim().isEmpty())
            throw new RuntimeException("El teléfono del cliente es obligatorio");
        if (cliente.getTelefono().length() > 20)
            throw new RuntimeException("El teléfono no debe exceder los 20 caracteres");
    }

    @Override
    public int insertar(Cliente cliente) {
        validarCliente(cliente);
        cliente.setActivo(true);
        return daoCliente.insertar(cliente);
    }

    @Override
    public int modificar(Cliente cliente) {
        validarCliente(cliente);
        if (cliente.getIdUsuario() <= 0)
            throw new RuntimeException("El identificador del cliente no es válido");
        return daoCliente.modificar(cliente);
    }

    @Override
    public int eliminar(int idCliente) {
        if (idCliente <= 0)
            throw new RuntimeException("El identificador del cliente no es válido");
        return daoCliente.eliminar(idCliente);
    }

    @Override
    public List<Cliente> listarTodos() {
        return daoCliente.listarTodos();
    }

    @Override
    public Cliente obtenerPorId(int idCliente) {
        if (idCliente <= 0)
            throw new RuntimeException("El identificador del cliente no es válido");
        return daoCliente.obtenerPorId(idCliente);
    }
}
