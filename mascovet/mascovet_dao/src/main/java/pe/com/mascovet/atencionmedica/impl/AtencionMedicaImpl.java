package pe.com.mascovet.atencionmedica.impl;

import pe.com.mascovet.atencionmedica.dao.AtencionMedicaDAO;
import pe.com.mascovet.config.TransactionContext;
import java.sql.Connection;

public class AtencionMedicaImpl implements AtencionMedicaDAO {
    @Override
    public void crearAtencionMedicaSP(String dniCliente, String nombreMascota, java.sql.Date fecha, java.sql.Time hora, String observaciones, java.sql.Time horaInicio, java.sql.Time horaFin) {
        String sql = "{CALL crear_atencion_medica(?, ?, ?, ?, ?, ?, ?)}";
        try {
            Connection con = TransactionContext.getConnection();
            try (java.sql.CallableStatement cs = con.prepareCall(sql)) {
                cs.setString(1, dniCliente);
                cs.setString(2, nombreMascota);
                cs.setDate(3, fecha);
                cs.setTime(4, hora);
                cs.setString(5, observaciones);
                cs.setTime(6, horaInicio);
                cs.setTime(7, horaFin);
                cs.execute();
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error al ejecutar crear_atencion_medica: " + ex.getMessage(), ex);
        }
    }
}
