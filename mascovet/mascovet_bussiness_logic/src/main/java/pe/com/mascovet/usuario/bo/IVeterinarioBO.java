package pe.com.mascovet.usuario.bo;
import pe.com.mascovet.bo.IBaseBO;
import pe.com.mascovet.usuario.model.Veterinario;
public interface IVeterinarioBO extends IBaseBO<Veterinario> {
    int insertarConHorarios(Veterinario veterinario);
    void asignarVeterinarioSP(String dni, String numColegiatura, String especialidad);
}
