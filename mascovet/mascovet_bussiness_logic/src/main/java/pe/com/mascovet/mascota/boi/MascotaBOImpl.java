package pe.com.mascovet.mascota.boi;

import pe.com.mascovet.mascota.bo.IMascotaBO;
import pe.com.mascovet.mascota.dao.MascotaDAO;
import pe.com.mascovet.mascota.impl.MascotaImpl;
import pe.com.mascovet.mascota.model.Mascota;

import java.util.List;

public class MascotaBOImpl implements IMascotaBO {

    private MascotaDAO daoMascota;

    public MascotaBOImpl() {
        daoMascota = new MascotaImpl();
    }

    private void validarMascota(Mascota mascota) {
        if (mascota == null)
            throw new RuntimeException("La mascota que se quiere registrar es null");
        if (mascota.getCliente() == null || mascota.getCliente().getIdUsuario() <= 0)
            throw new RuntimeException("El cliente asociado a la mascota no es válido");
        if (mascota.getNombre() == null || mascota.getNombre().trim().isEmpty())
            throw new RuntimeException("El nombre de la mascota es obligatorio");
        if (mascota.getNombre().length() > 150)
            throw new RuntimeException("El nombre de la mascota no debe exceder los 150 caracteres");
        if (mascota.getFechaNacimiento() == null)
            throw new RuntimeException("La fecha de nacimiento es obligatoria");
        if (mascota.getEspecie() == null)
            throw new RuntimeException("La especie es obligatoria");
        if (mascota.getRaza() == null || mascota.getRaza().trim().isEmpty())
            throw new RuntimeException("La raza es obligatoria");
        if (mascota.getRaza().length() > 150)
            throw new RuntimeException("La raza no debe exceder los 150 caracteres");
        if (mascota.getSexo() == null)
            throw new RuntimeException("El sexo es obligatorio");
    }

    @Override
    public int insertar(Mascota mascota) {
        validarMascota(mascota);
        mascota.setActivo(true);
        return daoMascota.insertar(mascota);
    }

    @Override
    public int modificar(Mascota mascota) {
        validarMascota(mascota);
        if (mascota.getIdMascota() <= 0)
            throw new RuntimeException("El identificador de la mascota no es válido");
        return daoMascota.modificar(mascota);
    }

    @Override
    public int eliminar(int idMascota) {
        if (idMascota <= 0)
            throw new RuntimeException("El identificador de la mascota no es válido");
        return daoMascota.eliminar(idMascota);
    }

    @Override
    public List<Mascota> listarTodos() {
        return daoMascota.listarTodos();
    }

    @Override
    public Mascota obtenerPorId(int idMascota) {
        if (idMascota <= 0)
            throw new RuntimeException("El identificador de la mascota no es válido");
        return daoMascota.obtenerPorId(idMascota);
    }
}
