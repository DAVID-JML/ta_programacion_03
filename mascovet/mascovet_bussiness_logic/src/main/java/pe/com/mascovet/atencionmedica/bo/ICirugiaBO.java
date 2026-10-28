package pe.com.mascovet.atencionmedica.bo;
import pe.com.mascovet.bo.IBaseBO;
import pe.com.mascovet.atencionmedica.model.Cirugia;
public interface ICirugiaBO extends IBaseBO<Cirugia> {
    void registrarCirugiaSP(String dniCliente, String nombreMascota, java.sql.Date fecha, java.sql.Time hora, String procedimiento, String indicacionesPostOperatorias);
}
