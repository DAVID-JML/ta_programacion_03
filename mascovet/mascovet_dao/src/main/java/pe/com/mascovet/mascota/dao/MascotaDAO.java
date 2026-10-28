package pe.com.mascovet.mascota.dao;

import pe.com.mascovet.dao.IDAO;
import pe.com.mascovet.mascota.model.Mascota;

public interface MascotaDAO extends IDAO<Mascota> {

    void vincularMascotaSP(String dni, String nombre, java.sql.Date fechaNacimiento, String especie, String raza, String sexo);
    void cambiarEstadoMascotaSP(String dniCliente, String nombreMascota, boolean activo);
}
