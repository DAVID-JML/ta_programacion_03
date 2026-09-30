package pe.com.mascovet.cita.boi;

import pe.com.mascovet.cita.bo.ICitaBO;
import pe.com.mascovet.cita.dao.CitaDAO;
import pe.com.mascovet.cita.impl.CitaImpl;
import pe.com.mascovet.cita.model.Cita;

import java.util.List;

public class CitaBOImpl implements ICitaBO {

    private CitaDAO daoCita;

    public CitaBOImpl() {
        daoCita = new CitaImpl();
    }

    private void validarCita(Cita cita) {
        if (cita == null)
            throw new RuntimeException("La cita que se quiere registrar es null");
        if (cita.getMascota() == null || cita.getMascota().getIdMascota() <= 0)
            throw new RuntimeException("La mascota asociada a la cita no es válida");
        if (cita.getCliente() == null || cita.getCliente().getIdUsuario() <= 0)
            throw new RuntimeException("El cliente asociado a la cita no es válido");
        if (cita.getRecepcionista() != null && cita.getRecepcionista().getIdUsuario() <= 0)
            throw new RuntimeException("El recepcionista asociado a la cita no es válido");
        if (cita.getVeterinario() == null || cita.getVeterinario().getIdUsuario() <= 0)
            throw new RuntimeException("El veterinario asociado a la cita no es válido");
        if (cita.getFecha() == null)
            throw new RuntimeException("La fecha de la cita es obligatoria");
        if (cita.getHora() == null)
            throw new RuntimeException("La hora de la cita es obligatoria");
        if (cita.getEstado() == null)
            throw new RuntimeException("El estado de la cita es obligatorio");
    }

    @Override
    public int insertar(Cita cita) {
        validarCita(cita);
        return daoCita.insertar(cita);
    }

    @Override
    public int modificar(Cita cita) {
        validarCita(cita);
        if (cita.getIdCita() <= 0)
            throw new RuntimeException("El identificador de la cita no es válido");
        return daoCita.modificar(cita);
    }

    @Override
    public int eliminar(int idCita) {
        if (idCita <= 0)
            throw new RuntimeException("El identificador de la cita no es válido");
        return daoCita.eliminar(idCita);
    }

    @Override
    public List<Cita> listarTodos() {
        return daoCita.listarTodos();
    }

    @Override
    public Cita obtenerPorId(int idCita) {
        if (idCita <= 0)
            throw new RuntimeException("El identificador de la cita no es válido");
        return daoCita.obtenerPorId(idCita);
    }
}
