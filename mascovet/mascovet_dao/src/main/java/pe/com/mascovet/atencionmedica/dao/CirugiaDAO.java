package pe.com.mascovet.atencionmedica.dao;

import pe.com.mascovet.atencionmedica.model.Cirugia;
import pe.com.mascovet.dao.IDAO;

public interface CirugiaDAO extends IDAO<Cirugia> {
    void registrarCirugiaSP(String dniCliente, String nombreMascota, java.sql.Date fecha, java.sql.Time hora, String procedimiento, String indicacionesPostOperatorias);
}
