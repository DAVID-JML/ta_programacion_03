package pe.com.mascovet.atencionmedica.boi;

import pe.com.mascovet.atencionmedica.bo.IAtencionMedicaBO;
import pe.com.mascovet.atencionmedica.dao.AtencionMedicaDAO;
import pe.com.mascovet.atencionmedica.impl.AtencionMedicaImpl;
import pe.com.mascovet.config.TransactionContext;

public class AtencionMedicaBOImpl implements IAtencionMedicaBO {
    private final AtencionMedicaDAO dao = new AtencionMedicaImpl();

    @Override
    public void crearAtencionMedicaSP(String dniCliente, String nombreMascota, java.sql.Date fecha, java.sql.Time hora, String observaciones, java.sql.Time horaInicio, java.sql.Time horaFin) {
        if (dniCliente == null || dniCliente.trim().isEmpty()) throw new IllegalArgumentException("El DNI del cliente es obligatorio");
        if (nombreMascota == null || nombreMascota.trim().isEmpty()) throw new IllegalArgumentException("El nombre de la mascota es obligatorio");
        if (fecha == null || hora == null || horaInicio == null || horaFin == null) throw new IllegalArgumentException("La fecha y las horas son obligatorias");
        if (!horaInicio.before(horaFin)) throw new IllegalArgumentException("La hora de inicio debe ser anterior a la hora de fin");
        try {
            dao.crearAtencionMedicaSP(dniCliente, nombreMascota, fecha, hora, observaciones, horaInicio, horaFin);
            TransactionContext.commit();
        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new RuntimeException("Error en crearAtencionMedicaSP: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }
}
