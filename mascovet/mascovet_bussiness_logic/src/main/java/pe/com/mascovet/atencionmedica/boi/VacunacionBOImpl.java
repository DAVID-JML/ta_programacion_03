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
import pe.com.mascovet.cita.dao.CitaDAO;
import pe.com.mascovet.cita.impl.CitaImpl;

public class VacunacionBOImpl implements IVacunacionBO {

    private CitaDAO daoCita = new CitaImpl();
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
        if (vacunacion.getAlergias() != null && vacunacion.getAlergias().length() > 255)
            throw new RuntimeException("Las alergias no deben exceder los 255 caracteres");
        if (vacunacion.getFechaAplicacion() == null)
            throw new RuntimeException("La fecha de aplicación es obligatoria");
        if (vacunacion.getFechaProximaDosis() == null)
            throw new RuntimeException("La fecha de próxima dosis es obligatoria");
        if (vacunacion.getFechaProximaDosis().before(vacunacion.getFechaAplicacion()))
            throw new RuntimeException("La fecha de próxima dosis no puede ser anterior a la fecha de aplicación");
        if (vacunacion.getDosis() == null || vacunacion.getDosis().trim().isEmpty())
            throw new RuntimeException("La dosis es obligatoria");
        if (vacunacion.getDosis().length() > 50)
            throw new RuntimeException("La dosis no debe exceder los 50 caracteres");
    }

    private void validarVacuna(Vacuna vacuna) {
        if (vacuna == null)
            throw new RuntimeException("La vacuna es null");
        if (vacuna.getNombre() == null || vacuna.getNombre().trim().isEmpty())
            throw new RuntimeException("El nombre de la vacuna es obligatorio");
        if (vacuna.getNombre().length() > 50)
            throw new RuntimeException("El nombre de la vacuna no debe exceder los 50 caracteres");
        if (vacuna.getDescripcion() == null || vacuna.getDescripcion().trim().isEmpty())
            throw new RuntimeException("La descripción de la vacuna es obligatoria");
        if (vacuna.getDescripcion().length() > 255)
            throw new RuntimeException("La descripción no debe exceder los 255 caracteres");
    }

    @Override
    public int insertar(Vacunacion vacunacion) {
        validarVacunacion(vacunacion);

        try {
            daoCita.marcarAtendida(vacunacion.getCita().getIdCita(),
                    vacunacion.getMascota().getIdMascota());
            int resultado = daoVacunacion.insertar(vacunacion);
            if (resultado <= 0) throw new RuntimeException("No se pudo registrar la vacunacion");
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

        java.util.Set<Integer> existentes = new java.util.HashSet<>();
        for (Vacuna vacuna : vacunacion.getVacunas()) {
            if (vacuna == null || vacuna.getIdVacuna() < 0)
                throw new RuntimeException("La vacuna no es valida");
            if (vacuna.getIdVacuna() == 0) {
                validarVacuna(vacuna);
            } else {
                if (daoVacuna.obtenerPorId(vacuna.getIdVacuna()) == null)
                    throw new RuntimeException("La vacuna del catalogo no existe");
                if (!existentes.add(vacuna.getIdVacuna()))
                    throw new RuntimeException("La misma vacuna no puede repetirse en una vacunacion");
            }
        }

        try {
            daoCita.marcarAtendida(vacunacion.getCita().getIdCita(),
                    vacunacion.getMascota().getIdMascota());
            if (daoVacunacion.insertar(vacunacion) <= 0)
                throw new RuntimeException("No se pudo registrar la vacunacion");

            for (Vacuna vacuna : vacunacion.getVacunas()) {
                // ID 0: crear en el catalogo. ID positivo: reutilizar la existente.
                if (vacuna.getIdVacuna() == 0 && daoVacuna.insertar(vacuna) <= 0)
                    throw new RuntimeException("No se pudo registrar la vacuna");
                if (daoVacunacion.asociarVacuna(vacunacion.getIdAtencion(),
                        vacuna.getIdVacuna()) != 1)
                    throw new RuntimeException("No se pudo asociar la vacuna");
            }
            TransactionContext.commit();
            return 1;
        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new RuntimeException("Error al registrar la vacunación con vacunas", ex);
        } finally {
            TransactionContext.close();
        }
    }


    @Override
    public void registrarVacunacionSP(String dniCliente, String nombreMascota, java.sql.Date fechaCita, java.sql.Time horaCita, java.sql.Date fechaAplicacion, java.sql.Date fechaProximaDosis, String dosis) {
        try {
            daoVacunacion.registrarVacunacionSP(dniCliente, nombreMascota, fechaCita, horaCita, fechaAplicacion, fechaProximaDosis, dosis);
            TransactionContext.commit();
        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new RuntimeException("Error en registrarVacunacionSP: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }
}
