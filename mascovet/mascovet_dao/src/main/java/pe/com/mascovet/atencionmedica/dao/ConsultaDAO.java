package pe.com.mascovet.atencionmedica.dao;

import pe.com.mascovet.atencionmedica.model.Consulta;
import pe.com.mascovet.dao.IDAO;

public interface ConsultaDAO extends IDAO<Consulta> {

    void registrarConsultaSP(String dniCliente, String nombreMascota, java.sql.Date fecha, java.sql.Time hora, String motivoConsulta, String diagnostico, String tratamiento);
}
