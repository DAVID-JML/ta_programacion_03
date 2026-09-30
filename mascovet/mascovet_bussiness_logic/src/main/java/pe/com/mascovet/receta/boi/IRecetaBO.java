package pe.com.mascovet.receta.boi;
import pe.com.mascovet.bo.IBaseBO;
import pe.com.mascovet.receta.model.Receta;
public interface IRecetaBO extends IBaseBO<Receta> {
    int insertarConDetalles(Receta receta);
}
