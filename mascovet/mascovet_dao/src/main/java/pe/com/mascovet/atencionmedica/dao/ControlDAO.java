package pe.com.mascovet.atencionmedica.dao;

import pe.com.mascovet.atencionmedica.model.Control;
import pe.com.mascovet.dao.IDAO;

public interface ControlDAO extends IDAO<Control> {
    void registrarControlSP(String dniCliente, String nombreMascota, java.sql.Date fecha, java.sql.Time hora, String evolucion, String indicaciones);
}
