package pe.com.mascovet.receta.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.receta.dao.MedicamentoDAO;
import pe.com.mascovet.receta.model.Medicamento;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class MedicamentoImpl implements MedicamentoDAO {

    @Override
    public int insertar(Medicamento medicamento) {
        String sql = "INSERT INTO MEDICAMENTO (id_detalle_receta, nombre, descripcion, monto) VALUES (?, ?, ?, ?)";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, medicamento.getDetalleReceta().getIdDetalleReceta());
            ps.setString(2, medicamento.getNombre());
            ps.setString(3, medicamento.getDescripcion());
            ps.setDouble(4, medicamento.getMonto());
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
    public int modificar(Medicamento medicamento) {
        String sql = "UPDATE MEDICAMENTO SET nombre=?, descripcion=?, monto=? WHERE id_medicamento=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, medicamento.getNombre());
            ps.setString(2, medicamento.getDescripcion());
            ps.setDouble(3, medicamento.getMonto());
            ps.setInt(4, medicamento.getIdMedicamento());
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idMedicamento) {
        String sql = "DELETE FROM MEDICAMENTO WHERE id_medicamento=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMedicamento);
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Medicamento obtenerPorId(int idMedicamento) {
        String sql = "SELECT id_medicamento, id_detalle_receta, nombre, descripcion, monto FROM MEDICAMENTO WHERE id_medicamento=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMedicamento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Medicamento m = new Medicamento();
                    m.setIdMedicamento(rs.getInt("id_medicamento"));
                    m.setNombre(rs.getString("nombre"));
                    m.setDescripcion(rs.getString("descripcion"));
                    m.setMonto(rs.getDouble("monto"));
                    return m;
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    @Override
    public List<Medicamento> listarTodos() {
        List<Medicamento> lista = new ArrayList<>();
        String sql = "SELECT id_medicamento, id_detalle_receta, nombre, descripcion, monto FROM MEDICAMENTO";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Medicamento m = new Medicamento();
                m.setIdMedicamento(rs.getInt("id_medicamento"));
                m.setNombre(rs.getString("nombre"));
                m.setDescripcion(rs.getString("descripcion"));
                m.setMonto(rs.getDouble("monto"));
                lista.add(m);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return lista;
    }
}
