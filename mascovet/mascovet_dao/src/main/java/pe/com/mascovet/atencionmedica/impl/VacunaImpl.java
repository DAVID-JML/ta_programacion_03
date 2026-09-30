package pe.com.mascovet.atencionmedica.impl;

import pe.com.mascovet.atencionmedica.dao.VacunaDAO;
import pe.com.mascovet.atencionmedica.model.Vacuna;
import pe.com.mascovet.atencionmedica.model.Vacunacion;
import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.config.TransactionContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VacunaImpl implements VacunaDAO {

    @Override
    public int insertar(Vacuna vacuna) {
        String sql = "INSERT INTO VACUNA (id_vacunacion, nombre, descripcion) VALUES (?, ?, ?)";
        try {
            Connection con = TransactionContext.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, vacuna.getVacunacion().getIdAtencion());
                ps.setString(2, vacuna.getNombre());
                ps.setString(3, vacuna.getDescripcion());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        vacuna.setIdVacuna(rs.getInt(1));
                        return vacuna.getIdVacuna();
                    }
                }
                return 0;
            } catch (Exception ex) {
                System.out.println("ERROR AL INSERTAR VACUNA: " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL INSERTAR VACUNA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int modificar(Vacuna vacuna) {
        String sql = "UPDATE VACUNA SET id_vacunacion=?, nombre=?, descripcion=? WHERE id_vacuna=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, vacuna.getVacunacion().getIdAtencion());
            ps.setString(2, vacuna.getNombre());
            ps.setString(3, vacuna.getDescripcion());
            ps.setInt(4, vacuna.getIdVacuna());
            return ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR AL MODIFICAR VACUNA: " + ex.getMessage());
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
            System.out.println("ERROR AL ELIMINAR VACUNA: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Vacuna obtenerPorId(int idVacuna) {
        Vacuna vacuna = null;
        String sql = "SELECT id_vacuna, id_vacunacion, nombre, descripcion FROM VACUNA WHERE id_vacuna=?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVacuna);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    vacuna = new Vacuna();
                    vacuna.setIdVacuna(rs.getInt("id_vacuna"));
                    Vacunacion vacunacion = new Vacunacion();
                    vacunacion.setIdAtencion(rs.getInt("id_vacunacion"));
                    vacuna.setVacunacion(vacunacion);
                    vacuna.setNombre(rs.getString("nombre"));
                    vacuna.setDescripcion(rs.getString("descripcion"));
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
        String sql = "SELECT id_vacuna, id_vacunacion, nombre, descripcion FROM VACUNA";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Vacuna vacuna = new Vacuna();
                vacuna.setIdVacuna(rs.getInt("id_vacuna"));
                Vacunacion vacunacion = new Vacunacion();
                vacunacion.setIdAtencion(rs.getInt("id_vacunacion"));
                vacuna.setVacunacion(vacunacion);
                vacuna.setNombre(rs.getString("nombre"));
                vacuna.setDescripcion(rs.getString("descripcion"));
                vacunas.add(vacuna);
            }
            return vacunas;
        } catch (Exception ex) {
            System.out.println("ERROR AL LISTAR VACUNAS: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
