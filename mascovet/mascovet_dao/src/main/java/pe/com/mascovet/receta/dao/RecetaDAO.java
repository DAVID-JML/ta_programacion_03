package pe.com.mascovet.receta.dao;

import pe.com.mascovet.dao.IDAO;
import pe.com.mascovet.receta.model.Receta;

public interface RecetaDAO extends IDAO<Receta> {
    void registrarRecetaSP(String dniCliente, String nombreMascota, java.sql.Date fechaCita, java.sql.Time horaCita, java.sql.Date fechaReceta, String indicaciones);
}
