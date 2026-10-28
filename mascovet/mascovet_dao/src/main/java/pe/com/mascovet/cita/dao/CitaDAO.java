package pe.com.mascovet.cita.dao;

import pe.com.mascovet.cita.model.Cita;
import pe.com.mascovet.dao.IDAO;

public interface CitaDAO extends IDAO<Cita> {
    void marcarAtendida(int idCita, int idMascota);
    void registrarCitaSP(String dniCliente, String nombreMascota, java.sql.Date fecha, java.sql.Time hora, String estado, String dniVeterinario);
}
