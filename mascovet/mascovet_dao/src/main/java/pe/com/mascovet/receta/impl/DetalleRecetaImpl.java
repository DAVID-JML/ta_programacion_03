package pe.com.mascovet.receta.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.config.TransactionContext;
import pe.com.mascovet.receta.dao.DetalleRecetaDAO;
import pe.com.mascovet.receta.model.DetalleReceta;
import pe.com.mascovet.receta.model.Medicamento;
import pe.com.mascovet.receta.model.Receta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class DetalleRecetaImpl implements DetalleRecetaDAO {

    @Override
    public int insertar(DetalleReceta detalle) {
        String sql = "INSERT INTO DETALLE_RECETA (id_receta, id_medicamento, dosis, frecuencia, duracion, montoTotal) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection con = TransactionContext.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, detalle.getReceta().getIdReceta());
                ps.setInt(2, detalle.getMedicamento().getIdMedicamento());
                ps.setString(3, detalle.getDosis());
                ps.setString(4, detalle.getFrecuencia());
                ps.setString(5, detalle.getDuracion());
                if (detalle.getMontoTotal() == null) {
                    ps.setNull(6, Types.DOUBLE);
                } else {
                    ps.setDouble(6, detalle.getMontoTotal());
                }
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        detalle.setIdDetalleReceta(rs.getInt(1));
                        return detalle.getIdDetalleReceta();
                    }
                }
                return 0;
            } catch (Exception ex) {
                System.out.println("ERROR AL INSERTAR DETALLE DE RECETA: " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR DETALLE DE RECETA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(DetalleReceta detalle) {
        String sql = "UPDATE DETALLE_RECETA SET id_receta=?, id_medicamento=?, dosis=?, frecuencia=?, duracion=?, montoTotal=? WHERE id_detalle_receta=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, detalle.getReceta().getIdReceta());
            ps.setInt(2, detalle.getMedicamento().getIdMedicamento());
            ps.setString(3, detalle.getDosis());
            ps.setString(4, detalle.getFrecuencia());
            ps.setString(5, detalle.getDuracion());
            if (detalle.getMontoTotal() == null) {
                ps.setNull(6, Types.DOUBLE);
            } else {
                ps.setDouble(6, detalle.getMontoTotal());
            }
            ps.setInt(7, detalle.getIdDetalleReceta());
            return ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR DETALLE DE RECETA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idDetalle) {
        String sql = "DELETE FROM DETALLE_RECETA WHERE id_detalle_receta=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDetalle);
            return ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR AL ELIMINAR DETALLE DE RECETA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public DetalleReceta obtenerPorId(int idDetalle) {
        DetalleReceta detalle = null;
        String sql = "SELECT d.id_detalle_receta, d.id_receta, d.id_medicamento, d.dosis, d.frecuencia, d.duracion, d.montoTotal, " +
                "m.nombre, m.descripcion, m.monto FROM DETALLE_RECETA d " +
                "INNER JOIN MEDICAMENTO m ON d.id_medicamento=m.id_medicamento WHERE d.id_detalle_receta=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDetalle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    detalle = new DetalleReceta();
                    detalle.setIdDetalleReceta(rs.getInt("id_detalle_receta"));
                    Receta receta = new Receta();
                    receta.setIdReceta(rs.getInt("id_receta"));
                    detalle.setReceta(receta);
                    Medicamento medicamento = new Medicamento();
                    medicamento.setIdMedicamento(rs.getInt("id_medicamento"));
                    medicamento.setNombre(rs.getString("nombre"));
                    medicamento.setDescripcion(rs.getString("descripcion"));
                    medicamento.setMonto(rs.getDouble("monto"));
                    detalle.setMedicamento(medicamento);
                    detalle.setDosis(rs.getString("dosis"));
                    detalle.setFrecuencia(rs.getString("frecuencia"));
                    detalle.setDuracion(rs.getString("duracion"));
                    double montoTotal = rs.getDouble("montoTotal");
                    detalle.setMontoTotal(rs.wasNull() ? null : montoTotal);
                }
            }
            return detalle;
        } catch (Exception ex) {
            System.out.println("ERROR AL OBTENER DETALLE DE RECETA POR ID: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<DetalleReceta> listarTodos() {
        List<DetalleReceta> detalles = new ArrayList<>();
        String sql = "SELECT d.id_detalle_receta, d.id_receta, d.id_medicamento, d.dosis, d.frecuencia, d.duracion, d.montoTotal, " +
                "m.nombre, m.descripcion, m.monto FROM DETALLE_RECETA d " +
                "INNER JOIN MEDICAMENTO m ON d.id_medicamento=m.id_medicamento";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DetalleReceta detalle = new DetalleReceta();
                detalle.setIdDetalleReceta(rs.getInt("id_detalle_receta"));
                Receta receta = new Receta();
                receta.setIdReceta(rs.getInt("id_receta"));
                detalle.setReceta(receta);
                Medicamento medicamento = new Medicamento();
                medicamento.setIdMedicamento(rs.getInt("id_medicamento"));
                medicamento.setNombre(rs.getString("nombre"));
                medicamento.setDescripcion(rs.getString("descripcion"));
                medicamento.setMonto(rs.getDouble("monto"));
                detalle.setMedicamento(medicamento);
                detalle.setDosis(rs.getString("dosis"));
                detalle.setFrecuencia(rs.getString("frecuencia"));
                detalle.setDuracion(rs.getString("duracion"));
                double montoTotal = rs.getDouble("montoTotal");
                detalle.setMontoTotal(rs.wasNull() ? null : montoTotal);
                detalles.add(detalle);
            }
            return detalles;
        } catch (Exception ex) {
            System.out.println("ERROR AL LISTAR DETALLES DE RECETA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
