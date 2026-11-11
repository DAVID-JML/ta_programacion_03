package pe.com.mascovet.receta.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.config.TransactionContext;
import pe.com.mascovet.receta.dao.DetalleRecetaDAO;
import pe.com.mascovet.receta.model.DetalleReceta;
import pe.com.mascovet.receta.model.Medicamento;
import pe.com.mascovet.receta.model.Receta;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class DetalleRecetaImpl implements DetalleRecetaDAO {

    @Override
    public int insertar(DetalleReceta detalle) {
        String sql = "{call INSERTAR_DETALLERECETA(?, ?, ?, ?, ?, ?, ?)}";
        Connection conexion = TransactionContext.getCurrentConnection();
        if (conexion == null)
            conexion = DBManager.getInstance().getConnection();
        try (Connection con = conexion) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_id_detalle_receta", Types.INTEGER);
                cs.setInt("_id_receta", detalle.getReceta().getIdReceta());
                cs.setInt("_id_medicamento", detalle.getMedicamento().getIdMedicamento());
                cs.setString("_dosis", detalle.getDosis());
                cs.setString("_frecuencia", detalle.getFrecuencia());
                cs.setString("_duracion", detalle.getDuracion());
                if (detalle.getMontoTotal() == null) {
                    cs.setNull("_montototal", Types.DOUBLE);
                } else {
                    cs.setDouble("_montototal", detalle.getMontoTotal());
                }
                cs.executeUpdate();

                if (cs.getInt("_id_detalle_receta") > 0) {
                        detalle.setIdDetalleReceta(cs.getInt("_id_detalle_receta"));
                        return detalle.getIdDetalleReceta();
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
        String sql = "{call MODIFICAR_DETALLERECETA(?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter("_resultado", Types.INTEGER);
            cs.setInt("_id_receta", detalle.getReceta().getIdReceta());
            cs.setInt("_id_medicamento", detalle.getMedicamento().getIdMedicamento());
            cs.setString("_dosis", detalle.getDosis());
            cs.setString("_frecuencia", detalle.getFrecuencia());
            cs.setString("_duracion", detalle.getDuracion());
            if (detalle.getMontoTotal() == null) {
                cs.setNull("_montototal", Types.DOUBLE);
            } else {
                cs.setDouble("_montototal", detalle.getMontoTotal());
            }
            cs.setInt("_id_detalle_receta", detalle.getIdDetalleReceta());
            cs.executeUpdate();
            return cs.getInt("_resultado");
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR DETALLE DE RECETA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idDetalle) {
        String sql = "{call ELIMINAR_DETALLERECETA(?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter("_resultado", Types.INTEGER);
            cs.setInt("_id_detalle_receta", idDetalle);
            cs.executeUpdate();
            return cs.getInt("_resultado");
        } catch (Exception ex) {
            System.out.println("ERROR AL ELIMINAR DETALLE DE RECETA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public DetalleReceta obtenerPorId(int idDetalle) {
        DetalleReceta detalle = null;
        String sql = "{call LISTAR_DETALLERECETA_X_ID(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt("_id_detalle_receta", idDetalle);
            try (ResultSet rs = cs.executeQuery()) {
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
                    medicamento.setFechaVencimiento(rs.getDate("fecha_vencimiento"));
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
        String sql = "{call LISTAR_DETALLERECETA_TODOS()}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
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
                    medicamento.setFechaVencimiento(rs.getDate("fecha_vencimiento"));
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
