package pe.com.mascovet.receta.boi;

import pe.com.mascovet.receta.bo.IMedicamentoBO;
import pe.com.mascovet.receta.dao.MedicamentoDAO;
import pe.com.mascovet.receta.impl.MedicamentoImpl;
import pe.com.mascovet.receta.model.Medicamento;

import java.util.List;

public class MedicamentoBOImpl implements IMedicamentoBO {

    private MedicamentoDAO daoMedicamento;

    public MedicamentoBOImpl() {
        daoMedicamento = new MedicamentoImpl();
    }

    private void validarMedicamento(Medicamento medicamento) {
        if (medicamento == null)
            throw new RuntimeException("El medicamento que se quiere registrar es null");
        if (medicamento.getNombre() == null || medicamento.getNombre().trim().isEmpty())
            throw new RuntimeException("El nombre del medicamento es obligatorio");
        if (medicamento.getNombre().length() > 200)
            throw new RuntimeException("El nombre del medicamento no debe exceder los 200 caracteres");
        if (medicamento.getDescripcion() == null || medicamento.getDescripcion().trim().isEmpty())
            throw new RuntimeException("La descripción del medicamento es obligatoria");
        if (medicamento.getDescripcion().length() > 200)
            throw new RuntimeException("La descripción no debe exceder los 200 caracteres");
        if (medicamento.getMonto() < 0)
            throw new RuntimeException("El monto no puede ser negativo");
    }

    @Override
    public int insertar(Medicamento medicamento) {
        validarMedicamento(medicamento);
        return daoMedicamento.insertar(medicamento);
    }

    @Override
    public int modificar(Medicamento medicamento) {
        validarMedicamento(medicamento);
        if (medicamento.getIdMedicamento() <= 0)
            throw new RuntimeException("El identificador del medicamento no es válido");
        return daoMedicamento.modificar(medicamento);
    }

    @Override
    public int eliminar(int idMedicamento) {
        if (idMedicamento <= 0)
            throw new RuntimeException("El identificador del medicamento no es válido");
        return daoMedicamento.eliminar(idMedicamento);
    }

    @Override
    public List<Medicamento> listarTodos() {
        return daoMedicamento.listarTodos();
    }

    @Override
    public Medicamento obtenerPorId(int idMedicamento) {
        if (idMedicamento <= 0)
            throw new RuntimeException("El identificador del medicamento no es válido");
        return daoMedicamento.obtenerPorId(idMedicamento);
    }
}
