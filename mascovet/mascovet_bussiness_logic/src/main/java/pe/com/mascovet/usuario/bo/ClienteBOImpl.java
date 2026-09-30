package pe.com.mascovet.usuario.bo;

import pe.com.mascovet.config.TransactionContext;
import pe.com.mascovet.usuario.boi.IClienteBO;
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
        if (cliente.getNombre().length() > 150)
            throw new RuntimeException("El nombre no debe exceder los 150 caracteres");
        if (cliente.getApellido() == null || cliente.getApellido().trim().isEmpty())
            throw new RuntimeException("El apellido del cliente es obligatorio");
        if (cliente.getApellido().length() > 150)
            throw new RuntimeException("El apellido no debe exceder los 150 caracteres");
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
        if (cliente.getTelefono().length() > 100)
            throw new RuntimeException("El teléfono no debe exceder los 100 caracteres");
    }

    @Override
    public int insertar(Cliente cliente) {
        validarCliente(cliente);
        cliente.setActivo(true);

        try {
            int resultado = daoCliente.insertar(cliente);
            TransactionContext.commit();
            return resultado;
        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new RuntimeException("Error:" + ex.getMessage());
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public int modificar(Cliente cliente) {
        validarCliente(cliente);
        if (cliente.getIdUsuario() <= 0)
            throw new RuntimeException("El identificador del cliente no es válido");

        try {
            int resultado = daoCliente.modificar(cliente);
            TransactionContext.commit();
            return resultado;
        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new RuntimeException("Error:" + ex.getMessage());
        } finally {
            TransactionContext.close();
        }
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
