package pe.com.mascovet.atencionmedica.impl;

import pe.com.mascovet.atencionmedica.dao.VacunaDAO;
import pe.com.mascovet.atencionmedica.model.Vacuna;
import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.config.TransactionContext;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VacunaImpl implements VacunaDAO {

    @Override
    public int insertar(Vacuna vacuna) {
        String sql = "{call INSERTAR_VACUNA(?, ?, ?, ?)}";
        Connection conexion = TransactionContext.getCurrentConnection();
        if (conexion == null)
            conexion = DBManager.getInstance().getConnection();
        try (Connection con = conexion) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.registerOutParameter("_id_vacuna", Types.INTEGER);
                cs.setString("_nombre", vacuna.getNombre());
                cs.setString("_descripcion", vacuna.getDescripcion());
                cs.setBoolean("_activo", vacuna.isActivo());
                cs.executeUpdate();

                if (cs.getInt("_id_vacuna") > 0) {
                        vacuna.setIdVacuna(cs.getInt("_id_vacuna"));
                        return vacuna.getIdVacuna();
                    }
                return 0;
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR VACUNA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Vacuna vacuna) {
        String sql = "{call MODIFICAR_VACUNA(?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter("_resultado", Types.INTEGER);
            cs.setString("_nombre", vacuna.getNombre());
            cs.setString("_descripcion", vacuna.getDescripcion());
            cs.setInt("_id_vacuna", vacuna.getIdVacuna());
            cs.executeUpdate();
            return cs.getInt("_resultado");
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR VACUNA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idVacuna) {
        String sql = "{call ELIMINAR_VACUNA(?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter("_resultado", Types.INTEGER);
            cs.setInt("_id_vacuna", idVacuna);
            cs.executeUpdate();
            return cs.getInt("_resultado");
        } catch (Exception ex) {
            System.out.println("ERROR AL ELIMINAR VACUNA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Vacuna obtenerPorId(int idVacuna) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            Vacuna vacuna = null;
            try (CallableStatement cs = con.prepareCall("{call LISTAR_VACUNA_X_ID(?)}")) {
                cs.setInt("_id_vacuna", idVacuna);
                try (ResultSet rs = cs.executeQuery()) {
                    if (rs.next()) {
                        vacuna = new Vacuna();
                        vacuna.setIdVacuna(rs.getInt("id_vacuna"));
                        vacuna.setNombre(rs.getString("nombre"));
                        vacuna.setDescripcion(rs.getString("descripcion"));
                        vacuna.setActivo(rs.getBoolean("activo"));
                    }
                }
            }
            return vacuna;
        } catch (Exception ex) {
            System.out.println("ERROR AL OBTENER VACUNA POR ID: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Vacuna> listarTodos() {
        List<Vacuna> vacunas = new ArrayList<>();
        String sql = "{call LISTAR_VACUNA_TODOS()}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                Vacuna vacuna = new Vacuna();
                vacuna.setIdVacuna(rs.getInt("id_vacuna"));
                vacuna.setNombre(rs.getString("nombre"));
                vacuna.setDescripcion(rs.getString("descripcion"));
                vacuna.setActivo(rs.getBoolean("activo"));
                vacunas.add(vacuna);
            }
            return vacunas;
        } catch (Exception ex) {
            System.out.println("ERROR AL LISTAR VACUNAS: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
