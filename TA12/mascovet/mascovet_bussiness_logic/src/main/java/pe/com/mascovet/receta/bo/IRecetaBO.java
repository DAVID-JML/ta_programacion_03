package pe.com.mascovet.receta.bo;

import java.util.List;
import pe.com.mascovet.bo.IBaseBO;
import pe.com.mascovet.receta.model.Receta;
import pe.com.mascovet.receta.model.DetalleReceta;

public interface IRecetaBO extends IBaseBO<Receta> {

    int insertarConDetalles(Receta receta);

    int insertarConDetalles(
            int idAtencion,
            String tipoAtencion,
            String fecha,
            String indicaciones,
            List<DetalleReceta> detalles);
}
