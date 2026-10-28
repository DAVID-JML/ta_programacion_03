package pe.com.mascovet.usuario.bo;
import pe.com.mascovet.bo.IBaseBO;
import pe.com.mascovet.usuario.model.Administrador;
public interface IAdministradorBO extends IBaseBO<Administrador> {
    void asignarAdministradorSP(String dni, String cargo);
}
