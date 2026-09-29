package pe.com.mascovet.atencionmedica.impl;

import pe.com.mascovet.atencionmedica.model.AtencionMedica;

import java.sql.*;

public class AtencionMedicaImpl {
    public int insertarAtencionBase(AtencionMedica a, Connection con) throws SQLException {
        String sql = "INSERT INTO ATENCION_MEDICA (observaciones, hora_inicio, hora_fin, id_mascota, id_cita, peso_actual, alergias) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getObservaciones());
            ps.setObject(2, a.getHoraInicio());
            ps.setObject(3, a.getHoraFin());
            ps.setInt(4, a.getMascota().getIdMascota());
            ps.setInt(5, a.getCita().getIdCita());
            ps.setDouble(6, a.getPesoActual()); // RF024[cite: 1]
            ps.setString(7, a.getAlergias());  // RF024[cite: 1]
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

}
