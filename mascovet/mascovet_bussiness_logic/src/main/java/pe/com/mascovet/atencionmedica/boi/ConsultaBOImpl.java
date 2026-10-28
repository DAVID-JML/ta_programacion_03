package pe.com.mascovet.atencionmedica.boi;

import pe.com.mascovet.atencionmedica.bo.IConsultaBO;
import pe.com.mascovet.atencionmedica.dao.ConsultaDAO;
import pe.com.mascovet.atencionmedica.impl.ConsultaImpl;
import pe.com.mascovet.atencionmedica.model.Consulta;
import pe.com.mascovet.config.TransactionContext;
import pe.com.mascovet.cita.dao.CitaDAO;
import pe.com.mascovet.cita.impl.CitaImpl;

import java.util.List;

public class ConsultaBOImpl implements IConsultaBO {

    private ConsultaDAO daoConsulta;
    private CitaDAO daoCita;
    public ConsultaBOImpl() {
        daoConsulta = new ConsultaImpl();
        daoCita = new CitaImpl();
    }

    private void validarConsulta(Consulta consulta) {
        if (consulta == null)
            throw new RuntimeException("La consulta que se quiere registrar es null");
        if (consulta.getMascota() == null || consulta.getMascota().getIdMascota() <= 0)
            throw new RuntimeException("La mascota asociada a la consulta no es válida");
        if (consulta.getCita() == null || consulta.getCita().getIdCita() <= 0)
            throw new RuntimeException("La cita asociada a la consulta no es válida");
        if (consulta.getHoraInicio() == null || consulta.getHoraFin() == null)
            throw new RuntimeException("Las horas de la consulta son obligatorias");
        if (!consulta.getHoraInicio().isBefore(consulta.getHoraFin()))
            throw new RuntimeException("La hora de inicio debe ser anterior a la hora de fin");
        if (consulta.getPesoActual() < 0)
            throw new RuntimeException("El peso actual no puede ser negativo");
        if (consulta.getObservaciones() != null && consulta.getObservaciones().length() > 255)
            throw new RuntimeException("Las observaciones no deben exceder los 255 caracteres");

        if (consulta.getAlergias() != null && consulta.getAlergias().length() > 255)
            throw new RuntimeException("Las alergias no deben exceder los 500 caracteres");

        if (consulta.getMotivoConsulta() == null || consulta.getMotivoConsulta().trim().isEmpty())
            throw new RuntimeException("El motivo de consulta es obligatorio");

        if (consulta.getMotivoConsulta().length() > 150)
            throw new RuntimeException("El motivo de consulta no debe exceder los 400 caracteres");

        if (consulta.getDiagnostico() == null || consulta.getDiagnostico().trim().isEmpty())
            throw new RuntimeException("El diagnóstico es obligatorio");

        if (consulta.getDiagnostico().length() > 150)
            throw new RuntimeException("El diagnóstico no debe exceder los 400 caracteres");

        if (consulta.getTratamiento() == null || consulta.getTratamiento().trim().isEmpty())
            throw new RuntimeException("El tratamiento es obligatorio");

        if (consulta.getTratamiento().length() > 255)
            throw new RuntimeException("El tratamiento no debe exceder los 400 caracteres");



    }

    @Override
    public int insertar(Consulta consulta) {
        validarConsulta(consulta);

        try {
            daoCita.marcarAtendida(
                    consulta.getCita().getIdCita(),
                    consulta.getMascota().getIdMascota()
            );

            int resultado = daoConsulta.insertar(consulta);

            if (resultado <= 0) {
                throw new RuntimeException(
                        "No se pudo registrar la consulta"
                );
            }

            TransactionContext.commit();
            return resultado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new RuntimeException(
                    "Error al registrar la consulta", ex
            );

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public int modificar(Consulta consulta) {
        validarConsulta(consulta);
        if (consulta.getIdAtencion() <= 0)
            throw new RuntimeException("El identificador de la consulta no es válido");

        try {
            int resultado = daoConsulta.modificar(consulta);
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
    public int eliminar(int idConsulta) {
        if (idConsulta <= 0)
            throw new RuntimeException("El identificador de la consulta no es válido");
        return daoConsulta.eliminar(idConsulta);
    }

    @Override
    public List<Consulta> listarTodos() {
        return daoConsulta.listarTodos();
    }

    @Override
    public Consulta obtenerPorId(int idConsulta) {
        if (idConsulta <= 0)
            throw new RuntimeException("El identificador de la consulta no es válido");
        return daoConsulta.obtenerPorId(idConsulta);
    }

    @Override
    public void registrarConsultaSP(String dniCliente, String nombreMascota, java.sql.Date fecha, java.sql.Time hora, String motivoConsulta, String diagnostico, String tratamiento) {
        try {
            daoConsulta.registrarConsultaSP(dniCliente, nombreMascota, fecha, hora, motivoConsulta, diagnostico, tratamiento);
            TransactionContext.commit();
        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new RuntimeException("Error en registrarConsultaSP: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }
}
