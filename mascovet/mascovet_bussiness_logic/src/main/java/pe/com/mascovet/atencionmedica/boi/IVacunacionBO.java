package pe.com.mascovet.atencionmedica.boi;
import pe.com.mascovet.bo.IBaseBO;
import pe.com.mascovet.atencionmedica.model.Vacunacion;
public interface IVacunacionBO extends IBaseBO<Vacunacion> {
    int insertarConVacunas(Vacunacion vacunacion);
}
