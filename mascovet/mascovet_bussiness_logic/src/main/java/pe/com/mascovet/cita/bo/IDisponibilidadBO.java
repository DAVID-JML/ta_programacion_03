package pe.com.mascovet.cita.bo;

import pe.com.mascovet.cita.model.HorarioAtencion;

import java.util.List;

public interface IDisponibilidadBO {
    List<HorarioAtencion> listarDisponibles(String fecha, String especialidad, int idVeterinario)
            throws RuntimeException;
}
