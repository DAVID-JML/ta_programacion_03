package pe.com.mascovet.atencionmedica.impl;

import pe.com.mascovet.atencionmedica.dao.VacunaDAO;
import pe.com.mascovet.atencionmedica.model.Vacuna;
import pe.com.mascovet.config.DBManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VacunaImpl implements VacunaDAO {
    @Override
    public int insertar(Vacuna vacuna) {
        String sql = "INSERT INTO VACUNA (id_vacunacion, nombre, descripcion) VALUES (?, ?, ?)";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, vacuna.getVacunacion().getIdAtencion());
            ps.setString(2, vacuna.getNombre());
            ps.setString(3, vacuna.getDescripcion());
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
    public int modificar(Vacuna vacuna) {
        String sql = "UPDATE VACUNA SET nombre=?, descripcion=? WHERE id_vacuna=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vacuna.getNombre());
            ps.setString(2, vacuna.getDescripcion());
            ps.setInt(3, vacuna.getIdVacuna());
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int eliminar(int idVacuna) {
        String sql = "DELETE FROM VACUNA WHERE id_vacuna=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVacuna);
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Vacuna obtenerPorId(int idVacuna) {
        String sql = "SELECT id_vacuna, id_vacunacion, nombre, descripcion FROM VACUNA WHERE id_vacuna=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVacuna);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Vacuna v = new Vacuna();
                    v.setIdVacuna(rs.getInt("id_vacuna"));
                    v.setNombre(rs.getString("nombre"));
                    v.setDescripcion(rs.getString("descripcion"));
                    return v;
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    @Override
    public List<Vacuna> listarTodos() {
        List<Vacuna> lista = new ArrayList<>();
        String sql = "SELECT id_vacuna, id_vacunacion, nombre, descripcion FROM VACUNA";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Vacuna v = new Vacuna();
                v.setIdVacuna(rs.getInt("id_vacuna"));
                v.setNombre(rs.getString("nombre"));
                v.setDescripcion(rs.getString("descripcion"));
                lista.add(v);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return lista;
    }
}
