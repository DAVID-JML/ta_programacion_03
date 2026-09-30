package pe.com.mascovet.atencionmedica.boi;

import pe.com.mascovet.atencionmedica.bo.IVacunacionBO;
import pe.com.mascovet.atencionmedica.dao.VacunaDAO;
import pe.com.mascovet.atencionmedica.dao.VacunacionDAO;
import pe.com.mascovet.atencionmedica.impl.VacunaImpl;
import pe.com.mascovet.atencionmedica.impl.VacunacionImpl;
import pe.com.mascovet.atencionmedica.model.Vacuna;
import pe.com.mascovet.atencionmedica.model.Vacunacion;
import pe.com.mascovet.config.TransactionContext;

import java.util.List;

public class VacunacionBOImpl implements IVacunacionBO {

    private VacunacionDAO daoVacunacion;
    private VacunaDAO daoVacuna;

    public VacunacionBOImpl() {
        daoVacunacion = new VacunacionImpl();
        daoVacuna = new VacunaImpl();
    }

    private void validarVacunacion(Vacunacion vacunacion) {
        if (vacunacion == null)
            throw new RuntimeException("La vacunación que se quiere registrar es null");
        if (vacunacion.getMascota() == null || vacunacion.getMascota().getIdMascota() <= 0)
            throw new RuntimeException("La mascota asociada a la vacunación no es válida");
        if (vacunacion.getCita() == null || vacunacion.getCita().getIdCita() <= 0)
            throw new RuntimeException("La cita asociada a la vacunación no es válida");
        if (vacunacion.getHoraInicio() == null || vacunacion.getHoraFin() == null)
            throw new RuntimeException("Las horas de la vacunación son obligatorias");
        if (!vacunacion.getHoraInicio().isBefore(vacunacion.getHoraFin()))
            throw new RuntimeException("La hora de inicio debe ser anterior a la hora de fin");
        if (vacunacion.getPesoActual() < 0)
            throw new RuntimeException("El peso actual no puede ser negativo");
        if (vacunacion.getObservaciones() != null && vacunacion.getObservaciones().length() > 255)
            throw new RuntimeException("Las observaciones no deben exceder los 255 caracteres");
        if (vacunacion.getAlergias() != null && vacunacion.getAlergias().length() > 500)
            throw new RuntimeException("Las alergias no deben exceder los 500 caracteres");
        if (vacunacion.getFechaAplicacion() == null)
            throw new RuntimeException("La fecha de aplicación es obligatoria");
        if (vacunacion.getFechaProximaDosis() == null)
            throw new RuntimeException("La fecha de próxima dosis es obligatoria");
        if (vacunacion.getFechaProximaDosis().before(vacunacion.getFechaAplicacion()))
            throw new RuntimeException("La fecha de próxima dosis no puede ser anterior a la fecha de aplicación");
        if (vacunacion.getDosis() == null || vacunacion.getDosis().trim().isEmpty())
            throw new RuntimeException("La dosis es obligatoria");
        if (vacunacion.getDosis().length() > 200)
            throw new RuntimeException("La dosis no debe exceder los 200 caracteres");
    }

    private void validarVacuna(Vacuna vacuna) {
        if (vacuna == null)
            throw new RuntimeException("La vacuna es null");
        if (vacuna.getNombre() == null || vacuna.getNombre().trim().isEmpty())
            throw new RuntimeException("El nombre de la vacuna es obligatorio");
        if (vacuna.getNombre().length() > 200)
            throw new RuntimeException("El nombre de la vacuna no debe exceder los 200 caracteres");
        if (vacuna.getDescripcion() == null || vacuna.getDescripcion().trim().isEmpty())
            throw new RuntimeException("La descripción de la vacuna es obligatoria");
        if (vacuna.getDescripcion().length() > 500)
            throw new RuntimeException("La descripción no debe exceder los 500 caracteres");
    }

    @Override
    public int insertar(Vacunacion vacunacion) {
        validarVacunacion(vacunacion);

        try {
            int resultado = daoVacunacion.insertar(vacunacion);
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
    public int modificar(Vacunacion vacunacion) {
        validarVacunacion(vacunacion);
        if (vacunacion.getIdAtencion() <= 0)
            throw new RuntimeException("El identificador de la vacunación no es válido");

        try {
            int resultado = daoVacunacion.modificar(vacunacion);
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
    public int eliminar(int idVacunacion) {
        if (idVacunacion <= 0)
            throw new RuntimeException("El identificador de la vacunación no es válido");
        return daoVacunacion.eliminar(idVacunacion);
    }

    @Override
    public List<Vacunacion> listarTodos() {
        return daoVacunacion.listarTodos();
    }

    @Override
    public Vacunacion obtenerPorId(int idVacunacion) {
        if (idVacunacion <= 0)
            throw new RuntimeException("El identificador de la vacunación no es válido");
        return daoVacunacion.obtenerPorId(idVacunacion);
    }

    @Override
    public int insertarConVacunas(Vacunacion vacunacion) {
        validarVacunacion(vacunacion);
        if (vacunacion.getVacunas() == null || vacunacion.getVacunas().isEmpty())
            throw new RuntimeException("La vacunación debe tener al menos una vacuna");
        for (Vacuna vacuna : vacunacion.getVacunas())
            validarVacuna(vacuna);

        try {
            daoVacunacion.insertar(vacunacion);
            for (Vacuna vacuna : vacunacion.getVacunas()) {
                vacuna.setVacunacion(vacunacion);
                daoVacuna.insertar(vacuna);
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
