package pe.com.mascovet.receta.dao;

import pe.com.mascovet.dao.IDAO;
import pe.com.mascovet.receta.model.Medicamento;

public interface MedicamentoDAO extends IDAO<Medicamento> {
    void registrarMedicamentoSP(String nombre, String descripcion, double monto, java.sql.Date fechaVencimiento);
}
