package pe.com.mascovet.atencionmedica.bo;
import pe.com.mascovet.bo.IBaseBO;
import pe.com.mascovet.atencionmedica.model.Vacunacion;
public interface IVacunacionBO extends IBaseBO<Vacunacion> {
    int insertarConVacunas(Vacunacion vacunacion);
    void registrarVacunacionSP(String dniCliente, String nombreMascota, java.sql.Date fechaCita, java.sql.Time horaCita, java.sql.Date fechaAplicacion, java.sql.Date fechaProximaDosis, String dosis);

}
