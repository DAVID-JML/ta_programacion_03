package pe.com.mascovet.atencionmedica.boi;

import pe.com.mascovet.config.TransactionContext;
import pe.com.mascovet.atencionmedica.bo.IVacunaBO;
import pe.com.mascovet.atencionmedica.dao.VacunaDAO;
import pe.com.mascovet.atencionmedica.impl.VacunaImpl;
import pe.com.mascovet.atencionmedica.model.Vacuna;

import java.util.List;

public class VacunaBOImpl implements IVacunaBO {

    private VacunaDAO daoVacuna;

    public VacunaBOImpl() {
        daoVacuna = new VacunaImpl();
    }

    private void validarVacuna(Vacuna vacuna) {
        if (vacuna == null)
            throw new RuntimeException("La vacuna que se quiere registrar es null");
        if (vacuna.getVacunacion() == null || vacuna.getVacunacion().getIdAtencion() <= 0)
            throw new RuntimeException("La vacunación asociada no es válida");
        if (vacuna.getNombre() == null || vacuna.getNombre().trim().isEmpty())
            throw new RuntimeException("El nombre de la vacuna es obligatorio");
        if (vacuna.getNombre().length() > 200)
            throw new RuntimeException("El nombre de la vacuna no debe exceder los 200 caracteres");
        if (vacuna.getDescripcion() == null || vacuna.getDescripcion().trim().isEmpty())
            throw new RuntimeException("La descripción de la vacuna es obligatoria");
        if (vacuna.getDescripcion().length() > 500)
            throw new RuntimeException("La descripción no debe exceder los 500 caracteres");
    }

    @Override
    public int insertar(Vacuna vacuna) {
        validarVacuna(vacuna);
        try {
            int resultado = daoVacuna.insertar(vacuna);
            TransactionContext.commit();
            return resultado;
        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new RuntimeException("Error:" + ex.getMessage());
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public int modificar(Vacuna vacuna) {
        validarVacuna(vacuna);
        if (vacuna.getIdVacuna() <= 0)
            throw new RuntimeException("El identificador de la vacuna no es válido");
        return daoVacuna.modificar(vacuna);
    }

    @Override
    public int eliminar(int idVacuna) {
        if (idVacuna <= 0)
            throw new RuntimeException("El identificador de la vacuna no es válido");
        return daoVacuna.eliminar(idVacuna);
    }

    @Override
    public List<Vacuna> listarTodos() {
        return daoVacuna.listarTodos();
    }

    @Override
    public Vacuna obtenerPorId(int idVacuna) {
        if (idVacuna <= 0)
            throw new RuntimeException("El identificador de la vacuna no es válido");
        return daoVacuna.obtenerPorId(idVacuna);
    }
}
