package pe.com.mascovet.atencionmedica.bo;

import pe.com.mascovet.atencionmedica.boi.ICirugiaBO;
import pe.com.mascovet.atencionmedica.dao.CirugiaDAO;
import pe.com.mascovet.atencionmedica.impl.CirugiaImpl;
import pe.com.mascovet.atencionmedica.model.Cirugia;
import pe.com.mascovet.config.TransactionContext;

import java.util.List;

public class CirugiaBOImpl implements ICirugiaBO {

    private CirugiaDAO daoCirugia;

    public CirugiaBOImpl() {
        daoCirugia = new CirugiaImpl();
    }

    private void validarCirugia(Cirugia cirugia) {
        if (cirugia == null)
            throw new RuntimeException("La cirugía que se quiere registrar es null");
        if (cirugia.getMascota() == null || cirugia.getMascota().getIdMascota() <= 0)
            throw new RuntimeException("La mascota asociada a la cirugía no es válida");
        if (cirugia.getCita() == null || cirugia.getCita().getIdCita() <= 0)
            throw new RuntimeException("La cita asociada a la cirugía no es válida");
        if (cirugia.getHoraInicio() == null || cirugia.getHoraFin() == null)
            throw new RuntimeException("Las horas de la cirugía son obligatorias");
        if (!cirugia.getHoraInicio().isBefore(cirugia.getHoraFin()))
            throw new RuntimeException("La hora de inicio debe ser anterior a la hora de fin");
        if (cirugia.getPesoActual() < 0)
            throw new RuntimeException("El peso actual no puede ser negativo");
        if (cirugia.getObservaciones() != null && cirugia.getObservaciones().length() > 255)
            throw new RuntimeException("Las observaciones no deben exceder los 255 caracteres");
        if (cirugia.getAlergias() != null && cirugia.getAlergias().length() > 500)
            throw new RuntimeException("Las alergias no deben exceder los 500 caracteres");
        if (cirugia.getProcedimiento() == null || cirugia.getProcedimiento().trim().isEmpty())
            throw new RuntimeException("El procedimiento es obligatorio");
        if (cirugia.getProcedimiento().length() > 500)
            throw new RuntimeException("El procedimiento no debe exceder los 500 caracteres");
        if (cirugia.getIndicacionesPostOperatorias() == null || cirugia.getIndicacionesPostOperatorias().trim().isEmpty())
            throw new RuntimeException("Las indicaciones postoperatorias son obligatorias");
        if (cirugia.getIndicacionesPostOperatorias().length() > 500)
            throw new RuntimeException("Las indicaciones postoperatorias no deben exceder los 500 caracteres");
    }

    @Override
    public int insertar(Cirugia cirugia) {
        validarCirugia(cirugia);

        try {
            int resultado = daoCirugia.insertar(cirugia);
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
    public int modificar(Cirugia cirugia) {
        validarCirugia(cirugia);
        if (cirugia.getIdAtencion() <= 0)
            throw new RuntimeException("El identificador de la cirugía no es válido");

        try {
            int resultado = daoCirugia.modificar(cirugia);
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
    public int eliminar(int idCirugia) {
        if (idCirugia <= 0)
            throw new RuntimeException("El identificador de la cirugía no es válido");
        return daoCirugia.eliminar(idCirugia);
    }

    @Override
    public List<Cirugia> listarTodos() {
        return daoCirugia.listarTodos();
    }

    @Override
    public Cirugia obtenerPorId(int idCirugia) {
        if (idCirugia <= 0)
            throw new RuntimeException("El identificador de la cirugía no es válido");
        return daoCirugia.obtenerPorId(idCirugia);
    }
}
