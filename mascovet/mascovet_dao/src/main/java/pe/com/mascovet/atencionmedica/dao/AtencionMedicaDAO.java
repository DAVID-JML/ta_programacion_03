package pe.com.mascovet.atencionmedica.dao;

public interface AtencionMedicaDAO {
    void crearAtencionMedicaSP(String dniCliente, String nombreMascota, java.sql.Date fecha, java.sql.Time hora, String observaciones, java.sql.Time horaInicio, java.sql.Time horaFin);
}
