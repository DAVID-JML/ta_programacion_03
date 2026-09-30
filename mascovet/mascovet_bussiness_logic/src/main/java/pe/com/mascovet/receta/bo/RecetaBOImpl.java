package pe.com.mascovet.receta.bo;

import pe.com.mascovet.config.TransactionContext;
import pe.com.mascovet.receta.boi.IRecetaBO;
import pe.com.mascovet.receta.dao.DetalleRecetaDAO;
import pe.com.mascovet.receta.dao.RecetaDAO;
import pe.com.mascovet.receta.impl.DetalleRecetaImpl;
import pe.com.mascovet.receta.impl.RecetaImpl;
import pe.com.mascovet.receta.model.DetalleReceta;
import pe.com.mascovet.receta.model.Receta;

import java.util.List;

public class RecetaBOImpl implements IRecetaBO {

    private RecetaDAO daoReceta;
    private DetalleRecetaDAO daoDetalleReceta;

    public RecetaBOImpl() {
        daoReceta = new RecetaImpl();
        daoDetalleReceta = new DetalleRecetaImpl();
    }

    private void validarReceta(Receta receta) {
        if (receta == null)
            throw new RuntimeException("La receta que se quiere registrar es null");
        if (receta.getAtencionMedica() == null || receta.getAtencionMedica().getIdAtencion() <= 0)
            throw new RuntimeException("La atención médica asociada no es válida");
        if (receta.getFecha() == null)
            throw new RuntimeException("La fecha de la receta es obligatoria");
        if (receta.getIndicaciones() == null || receta.getIndicaciones().trim().isEmpty())
            throw new RuntimeException("Las indicaciones de la receta son obligatorias");
        if (receta.getIndicaciones().length() > 600)
            throw new RuntimeException("Las indicaciones no deben exceder los 600 caracteres");
    }

    private void validarDetalleReceta(DetalleReceta detalleReceta) {
        if (detalleReceta == null)
            throw new RuntimeException("El detalle de receta es null");
        if (detalleReceta.getMedicamento() == null || detalleReceta.getMedicamento().getIdMedicamento() <= 0)
            throw new RuntimeException("Cada detalle debe tener un medicamento previamente registrado");
        if (detalleReceta.getDosis() == null || detalleReceta.getDosis().trim().isEmpty())
            throw new RuntimeException("La dosis del detalle es obligatoria");
        if (detalleReceta.getDosis().length() > 200)
            throw new RuntimeException("La dosis no debe exceder los 200 caracteres");
        if (detalleReceta.getFrecuencia() == null || detalleReceta.getFrecuencia().trim().isEmpty())
            throw new RuntimeException("La frecuencia del detalle es obligatoria");
        if (detalleReceta.getFrecuencia().length() > 200)
            throw new RuntimeException("La frecuencia no debe exceder los 200 caracteres");
        if (detalleReceta.getDuracion() == null || detalleReceta.getDuracion().trim().isEmpty())
            throw new RuntimeException("La duración del detalle es obligatoria");
        if (detalleReceta.getDuracion().length() > 200)
            throw new RuntimeException("La duración no debe exceder los 200 caracteres");
        if (detalleReceta.getMontoTotal() != null && detalleReceta.getMontoTotal() < 0)
            throw new RuntimeException("El monto total del detalle no puede ser negativo");
    }

    @Override
    public int insertar(Receta receta) {
        validarReceta(receta);
        try {
            int resultado = daoReceta.insertar(receta);
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
    public int modificar(Receta receta) {
        validarReceta(receta);
        if (receta.getIdReceta() <= 0)
            throw new RuntimeException("El identificador de la receta no es válido");
        return daoReceta.modificar(receta);
    }

    @Override
    public int eliminar(int idReceta) {
        if (idReceta <= 0)
            throw new RuntimeException("El identificador de la receta no es válido");
        return daoReceta.eliminar(idReceta);
    }

    @Override
    public List<Receta> listarTodos() {
        return daoReceta.listarTodos();
    }

    @Override
    public Receta obtenerPorId(int idReceta) {
        if (idReceta <= 0)
            throw new RuntimeException("El identificador de la receta no es válido");
        return daoReceta.obtenerPorId(idReceta);
    }

    @Override
    public int insertarConDetalles(Receta receta) {
        validarReceta(receta);

        if (receta.getDetallesReceta() == null || receta.getDetallesReceta().isEmpty())
            throw new RuntimeException("La receta debe tener al menos un detalle");

        for (DetalleReceta detalle : receta.getDetallesReceta()) {
            validarDetalleReceta(detalle);
        }

        try {
            daoReceta.insertar(receta);

            for (DetalleReceta detalle : receta.getDetallesReceta()) {
                detalle.setReceta(receta);
                daoDetalleReceta.insertar(detalle);
            }

            TransactionContext.commit();
            return 1;
        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new RuntimeException("Error:" + ex.getMessage());
        } finally {
            TransactionContext.close();
        }
    }
}
