package pe.com.mascovet.receta.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.receta.dao.RecetaDAO;
import pe.com.mascovet.receta.model.Receta;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;
public class RecetaImpl implements RecetaDAO {

    @Override
    public int insertar(Receta receta) {
        String sql = "INSERT INTO RECETA (id_atencion, fecha, indicaciones) VALUES (?, ?, ?)";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, receta.getAtencionMedica().getIdAtencion());
            ps.setDate(2, new Date(receta.getFecha().getTime()));
            ps.setString(3, receta.getIndicaciones());
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
    public int modificar(Receta receta) {
        String sql = "UPDATE RECETA SET fecha=?, indicaciones=? WHERE id_receta=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, new Date(receta.getFecha().getTime()));
            ps.setString(2, receta.getIndicaciones());
            ps.setInt(3, receta.getIdReceta());
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idReceta) {
        String sql = "DELETE FROM RECETA WHERE id_receta=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idReceta);
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Receta obtenerPorId(int idReceta) {
        String sql = "SELECT id_receta, id_atencion, fecha, indicaciones FROM RECETA WHERE id_receta=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idReceta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Receta r = new Receta();
                    r.setIdReceta(rs.getInt("id_receta"));
                    r.setFecha(rs.getDate("fecha"));
                    r.setIndicaciones(rs.getString("indicaciones"));
                    return r;
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    @Override
    public List<Receta> listarTodos() {
        List<Receta> lista = new ArrayList<>();
        String sql = "SELECT id_receta, id_atencion, fecha, indicaciones FROM RECETA";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Receta r = new Receta();
                r.setIdReceta(rs.getInt("id_receta"));
                r.setFecha(rs.getDate("fecha"));
                r.setIndicaciones(rs.getString("indicaciones"));
                lista.add(r);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return lista;
    }
}
