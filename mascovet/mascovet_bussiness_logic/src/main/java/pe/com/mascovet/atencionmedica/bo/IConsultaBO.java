package pe.com.mascovet.atencionmedica.bo;
import pe.com.mascovet.bo.IBaseBO;
import pe.com.mascovet.atencionmedica.model.Consulta;
public interface IConsultaBO extends IBaseBO<Consulta> {
    void registrarConsultaSP(String dniCliente, String nombreMascota, java.sql.Date fecha, java.sql.Time hora, String motivoConsulta, String diagnostico, String tratamiento);
}
