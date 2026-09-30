package pe.com.mascovet.receta.boi;

import pe.com.mascovet.config.TransactionContext;
import pe.com.mascovet.receta.bo.IDetalleRecetaBO;
import pe.com.mascovet.receta.dao.DetalleRecetaDAO;
import pe.com.mascovet.receta.impl.DetalleRecetaImpl;
import pe.com.mascovet.receta.model.DetalleReceta;

import java.util.List;

public class DetalleRecetaBOImpl implements IDetalleRecetaBO {

    private DetalleRecetaDAO daoDetalleReceta;

    public DetalleRecetaBOImpl() {
        daoDetalleReceta = new DetalleRecetaImpl();
    }

    private void validarDetalleReceta(DetalleReceta detalleReceta) {
        if (detalleReceta == null)
            throw new RuntimeException("El detalle de receta que se quiere registrar es null");
        if (detalleReceta.getReceta() == null || detalleReceta.getReceta().getIdReceta() <= 0)
            throw new RuntimeException("La receta asociada no es válida");
        if (detalleReceta.getMedicamento() == null || detalleReceta.getMedicamento().getIdMedicamento() <= 0)
            throw new RuntimeException("El medicamento asociado no es válido");
        if (detalleReceta.getDosis() == null || detalleReceta.getDosis().trim().isEmpty())
            throw new RuntimeException("La dosis es obligatoria");
        if (detalleReceta.getDosis().length() > 200)
            throw new RuntimeException("La dosis no debe exceder los 200 caracteres");
        if (detalleReceta.getFrecuencia() == null || detalleReceta.getFrecuencia().trim().isEmpty())
            throw new RuntimeException("La frecuencia es obligatoria");
        if (detalleReceta.getFrecuencia().length() > 200)
            throw new RuntimeException("La frecuencia no debe exceder los 200 caracteres");
        if (detalleReceta.getDuracion() == null || detalleReceta.getDuracion().trim().isEmpty())
            throw new RuntimeException("La duración es obligatoria");
        if (detalleReceta.getDuracion().length() > 200)
            throw new RuntimeException("La duración no debe exceder los 200 caracteres");
        if (detalleReceta.getMontoTotal() != null && detalleReceta.getMontoTotal() < 0)
            throw new RuntimeException("El monto total no puede ser negativo");
    }

    @Override
    public int insertar(DetalleReceta detalleReceta) {
        validarDetalleReceta(detalleReceta);
        try {
            int resultado = daoDetalleReceta.insertar(detalleReceta);
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
    public int modificar(DetalleReceta detalleReceta) {
        validarDetalleReceta(detalleReceta);
        if (detalleReceta.getIdDetalleReceta() <= 0)
            throw new RuntimeException("El identificador del detalle no es válido");
        return daoDetalleReceta.modificar(detalleReceta);
    }

    @Override
    public int eliminar(int idDetalleReceta) {
        if (idDetalleReceta <= 0)
            throw new RuntimeException("El identificador del detalle no es válido");
        return daoDetalleReceta.eliminar(idDetalleReceta);
    }

    @Override
    public List<DetalleReceta> listarTodos() {
        return daoDetalleReceta.listarTodos();
    }

    @Override
    public DetalleReceta obtenerPorId(int idDetalleReceta) {
        if (idDetalleReceta <= 0)
            throw new RuntimeException("El identificador del detalle no es válido");
        return daoDetalleReceta.obtenerPorId(idDetalleReceta);
    }
}
