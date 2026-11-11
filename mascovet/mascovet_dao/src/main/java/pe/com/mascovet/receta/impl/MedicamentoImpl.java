package pe.com.mascovet.receta.impl;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.receta.dao.MedicamentoDAO;
import pe.com.mascovet.receta.model.Medicamento;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.Types;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoImpl implements MedicamentoDAO {

    @Override
    public int insertar(Medicamento medicamento) {
        String sql = "{call INSERTAR_MEDICAMENTO(?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter("_id_medicamento", Types.INTEGER);
            cs.setString("_nombre", medicamento.getNombre());
            cs.setString("_descripcion", medicamento.getDescripcion());
            cs.setDate("_fecha_vencimiento", new java.sql.Date(medicamento.getFechaVencimiento().getTime()));
            cs.setDouble("_monto", medicamento.getMonto());
            cs.executeUpdate();

            if (cs.getInt("_id_medicamento") > 0) {
                    medicamento.setIdMedicamento(cs.getInt("_id_medicamento"));
                    return medicamento.getIdMedicamento();
                }
            return 0;
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR MEDICAMENTO: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Medicamento medicamento) {
        String sql = "{call MODIFICAR_MEDICAMENTO(?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter("_resultado", Types.INTEGER);
            cs.setString("_nombre", medicamento.getNombre());
            cs.setString("_descripcion", medicamento.getDescripcion());
            cs.setDate("_fecha_vencimiento", new java.sql.Date(medicamento.getFechaVencimiento().getTime()));
            cs.setDouble("_monto", medicamento.getMonto());
            cs.setInt("_id_medicamento", medicamento.getIdMedicamento());
            cs.executeUpdate();
            return cs.getInt("_resultado");
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR MEDICAMENTO: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idMedicamento) {
        String sql = "{call ELIMINAR_MEDICAMENTO(?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter("_resultado", Types.INTEGER);
            cs.setInt("_id_medicamento", idMedicamento);
            cs.executeUpdate();
            return cs.getInt("_resultado");
        } catch (Exception ex) {
            System.out.println("ERROR AL ELIMINAR MEDICAMENTO: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Medicamento obtenerPorId(int idMedicamento) {
        Medicamento medicamento = null;
        String sql = "{call LISTAR_MEDICAMENTO_X_ID(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt("_id_medicamento", idMedicamento);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    medicamento = new Medicamento();
                    medicamento.setIdMedicamento(rs.getInt("id_medicamento"));
                    medicamento.setNombre(rs.getString("nombre"));
                    medicamento.setDescripcion(rs.getString("descripcion"));
                    medicamento.setFechaVencimiento(rs.getDate("fecha_vencimiento"));
                    medicamento.setActivo(rs.getBoolean("activo"));
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
        String sql = "{call LISTAR_MEDICAMENTO_TODOS()}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                Medicamento medicamento = new Medicamento();
                medicamento.setIdMedicamento(rs.getInt("id_medicamento"));
                medicamento.setNombre(rs.getString("nombre"));
                medicamento.setDescripcion(rs.getString("descripcion"));
                    medicamento.setFechaVencimiento(rs.getDate("fecha_vencimiento"));
                    medicamento.setActivo(rs.getBoolean("activo"));
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
