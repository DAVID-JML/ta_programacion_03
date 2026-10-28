package pe.com.mascovet.atencionmedica.bo;
import pe.com.mascovet.bo.IBaseBO;
import pe.com.mascovet.atencionmedica.model.Control;
public interface IControlBO extends IBaseBO<Control> {

    void registrarControlSP(String dniCliente, String nombreMascota, java.sql.Date fecha, java.sql.Time hora, String evolucion, String indicaciones);
}
