package pe.com.mascovet.cita.bo;
import pe.com.mascovet.bo.IBaseBO;
import pe.com.mascovet.cita.model.Cita;
public interface ICitaBO extends IBaseBO<Cita> {
    void registrarCitaSP(String dniCliente, String nombreMascota, java.sql.Date fecha, java.sql.Time hora, String estado, String dniVeterinario);
}
