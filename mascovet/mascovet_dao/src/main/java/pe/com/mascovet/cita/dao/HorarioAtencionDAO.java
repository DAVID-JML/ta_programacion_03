package pe.com.mascovet.cita.dao;

import pe.com.mascovet.cita.model.HorarioAtencion;
import pe.com.mascovet.dao.IDAO;

public interface HorarioAtencionDAO extends IDAO<HorarioAtencion> {
    void asignarHorarioVeterinarioSP(String dni, String dia, java.sql.Time horaInicio, java.sql.Time horaFin);
}
