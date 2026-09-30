package pe.com.mascovet.atencionmedica.boi;

import pe.com.mascovet.atencionmedica.bo.IControlBO;
import pe.com.mascovet.atencionmedica.dao.ControlDAO;
import pe.com.mascovet.atencionmedica.impl.ControlImpl;
import pe.com.mascovet.atencionmedica.model.Control;
import pe.com.mascovet.config.TransactionContext;

import java.util.List;

public class ControlBOImpl implements IControlBO {

    private ControlDAO daoControl;

    public ControlBOImpl() {
        daoControl = new ControlImpl();
    }

    private void validarControl(Control control) {
        if (control == null)
            throw new RuntimeException("El control que se quiere registrar es null");
        if (control.getMascota() == null || control.getMascota().getIdMascota() <= 0)
            throw new RuntimeException("La mascota asociada al control no es válida");
        if (control.getCita() == null || control.getCita().getIdCita() <= 0)
            throw new RuntimeException("La cita asociada al control no es válida");
        if (control.getHoraInicio() == null || control.getHoraFin() == null)
            throw new RuntimeException("Las horas del control son obligatorias");
        if (!control.getHoraInicio().isBefore(control.getHoraFin()))
            throw new RuntimeException("La hora de inicio debe ser anterior a la hora de fin");
        if (control.getPesoActual() < 0)
            throw new RuntimeException("El peso actual no puede ser negativo");
        if (control.getObservaciones() != null && control.getObservaciones().length() > 255)
            throw new RuntimeException("Las observaciones no deben exceder los 255 caracteres");
        if (control.getAlergias() != null && control.getAlergias().length() > 500)
            throw new RuntimeException("Las alergias no deben exceder los 500 caracteres");
        if (control.getEvolucion() == null || control.getEvolucion().trim().isEmpty())
            throw new RuntimeException("La evolución es obligatoria");
        if (control.getEvolucion().length() > 500)
            throw new RuntimeException("La evolución no debe exceder los 500 caracteres");
        if (control.getIndicaciones() == null || control.getIndicaciones().trim().isEmpty())
            throw new RuntimeException("Las indicaciones son obligatorias");
        if (control.getIndicaciones().length() > 500)
            throw new RuntimeException("Las indicaciones no deben exceder los 500 caracteres");
    }

    @Override
    public int insertar(Control control) {
        validarControl(control);

        try {
            int resultado = daoControl.insertar(control);
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
    public int modificar(Control control) {
        validarControl(control);
        if (control.getIdAtencion() <= 0)
            throw new RuntimeException("El identificador del control no es válido");

        try {
            int resultado = daoControl.modificar(control);
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
    public int eliminar(int idControl) {
        if (idControl <= 0)
            throw new RuntimeException("El identificador del control no es válido");
        return daoControl.eliminar(idControl);
    }

    @Override
    public List<Control> listarTodos() {
        return daoControl.listarTodos();
    }

    @Override
    public Control obtenerPorId(int idControl) {
        if (idControl <= 0)
            throw new RuntimeException("El identificador del control no es válido");
        return daoControl.obtenerPorId(idControl);
    }
}
