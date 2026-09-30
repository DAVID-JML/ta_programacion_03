package pe.com.mascovet.receta.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.receta.dao.MedicamentoDAO;
import pe.com.mascovet.receta.model.Medicamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoImpl implements MedicamentoDAO {

    @Override
    public int insertar(Medicamento medicamento) {
        String sql = "INSERT INTO MEDICAMENTO (nombre, descripcion, monto) VALUES (?, ?, ?)";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, medicamento.getNombre());
            ps.setString(2, medicamento.getDescripcion());
            ps.setDouble(3, medicamento.getMonto());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    medicamento.setIdMedicamento(rs.getInt(1));
                    return medicamento.getIdMedicamento();
                }
            }
            return 0;
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR MEDICAMENTO: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
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
            System.out.println("ERROR AL MODIFICAR MEDICAMENTO: " + ex.getMessage());
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
            System.out.println("ERROR AL ELIMINAR MEDICAMENTO: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Medicamento obtenerPorId(int idMedicamento) {
        Medicamento medicamento = null;
        String sql = "SELECT id_medicamento, nombre, descripcion, monto FROM MEDICAMENTO WHERE id_medicamento=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMedicamento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    medicamento = new Medicamento();
                    medicamento.setIdMedicamento(rs.getInt("id_medicamento"));
                    medicamento.setNombre(rs.getString("nombre"));
                    medicamento.setDescripcion(rs.getString("descripcion"));
                    medicamento.setMonto(rs.getDouble("monto"));
                }
            }
            return medicamento;
        } catch (Exception ex) {
            System.out.println("ERROR AL OBTENER MEDICAMENTO POR ID: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Medicamento> listarTodos() {
        List<Medicamento> medicamentos = new ArrayList<>();
        String sql = "SELECT id_medicamento, nombre, descripcion, monto FROM MEDICAMENTO";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Medicamento medicamento = new Medicamento();
                medicamento.setIdMedicamento(rs.getInt("id_medicamento"));
                medicamento.setNombre(rs.getString("nombre"));
                medicamento.setDescripcion(rs.getString("descripcion"));
                medicamento.setMonto(rs.getDouble("monto"));
                medicamentos.add(medicamento);
            }
            return medicamentos;
        } catch (Exception ex) {
            System.out.println("ERROR AL LISTAR MEDICAMENTOS: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
