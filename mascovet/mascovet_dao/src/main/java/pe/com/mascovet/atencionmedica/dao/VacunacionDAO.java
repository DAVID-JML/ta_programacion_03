package pe.com.mascovet.atencionmedica.dao;

import pe.com.mascovet.atencionmedica.model.Vacunacion;
import pe.com.mascovet.dao.IDAO;

public interface VacunacionDAO extends IDAO<Vacunacion> {
    int asociarVacuna(int idAtencion, int idVacuna);

    void registrarVacunacionSP(String dniCliente, String nombreMascota, java.sql.Date fechaCita, java.sql.Time horaCita, java.sql.Date fechaAplicacion, java.sql.Date fechaProximaDosis, String dosis);
}
