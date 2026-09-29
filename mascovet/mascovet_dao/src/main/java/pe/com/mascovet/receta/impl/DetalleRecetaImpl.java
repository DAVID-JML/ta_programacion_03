package pe.com.mascovet.receta.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.receta.dao.DetalleRecetaDAO;
import pe.com.mascovet.receta.model.DetalleReceta;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class DetalleRecetaImpl implements DetalleRecetaDAO {
    @Override
    public int insertar(DetalleReceta detalle) {
        String sql = "INSERT INTO DETALLE_RECETA (id_receta, dosis, frecuencia, duracion, montoTotal) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, detalle.getReceta().getIdReceta());
            ps.setString(2, detalle.getDosis());
            ps.setString(3, detalle.getFrecuencia());
            ps.setString(4, detalle.getDuracion());
            ps.setDouble(5, detalle.getMontoTotal());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return 0;
    }

    @Override
    public int modificar(DetalleReceta detalle) {
        String sql = "UPDATE DETALLE_RECETA SET dosis=?, frecuencia=?, duracion=?, montoTotal=? WHERE id_detalle_receta=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, detalle.getDosis());
            ps.setString(2, detalle.getFrecuencia());
            ps.setString(3, detalle.getDuracion());
            ps.setDouble(4, detalle.getMontoTotal());
            ps.setInt(5, detalle.getIdDetalleReceta());
            return ps.executeUpdate();
        } catch (Exception ex) {
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
            throw new RuntimeException(ex);
        }
    }

    @Override
    public DetalleReceta obtenerPorId(int idDetalle) {
        String sql = "SELECT id_detalle_receta, id_receta, dosis, frecuencia, duracion, montoTotal FROM DETALLE_RECETA WHERE id_detalle_receta=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDetalle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DetalleReceta dr = new DetalleReceta();
                    dr.setIdDetalleReceta(rs.getInt("id_detalle_receta"));
                    dr.setDosis(rs.getString("dosis"));
                    dr.setFrecuencia(rs.getString("frecuencia"));
                    dr.setDuracion(rs.getString("duracion"));
                    dr.setMontoTotal(rs.getDouble("montoTotal"));
                    return dr;
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    @Override
    public List<DetalleReceta> listarTodos() {
        List<DetalleReceta> lista = new ArrayList<>();
        String sql = "SELECT id_detalle_receta, id_receta, dosis, frecuencia, duracion, montoTotal FROM DETALLE_RECETA";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DetalleReceta dr = new DetalleReceta();
                dr.setIdDetalleReceta(rs.getInt("id_detalle_receta"));
                dr.setDosis(rs.getString("dosis"));
                dr.setFrecuencia(rs.getString("frecuencia"));
                dr.setDuracion(rs.getString("duracion"));
                dr.setMontoTotal(rs.getDouble("montoTotal"));
                lista.add(dr);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return lista;
    }
}
