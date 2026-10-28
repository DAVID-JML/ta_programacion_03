package pe.com.mascovet.mascota.bo;
import pe.com.mascovet.bo.IBaseBO;
import pe.com.mascovet.mascota.model.Mascota;
public interface IMascotaBO extends IBaseBO<Mascota> {


    void vincularMascotaSP(String dni, String nombre, java.sql.Date fechaNacimiento, String especie, String raza, String sexo);


    void cambiarEstadoMascotaSP(String dniCliente, String nombreMascota, boolean activo);
}
