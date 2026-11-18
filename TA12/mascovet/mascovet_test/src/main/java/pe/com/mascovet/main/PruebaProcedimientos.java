package pe.com.mascovet.main;

import pe.com.mascovet.config.DBManager;
import pe.com.mascovet.usuario.boi.*;
import pe.com.mascovet.mascota.boi.MascotaBOImpl;
import pe.com.mascovet.cita.boi.*;
import pe.com.mascovet.atencionmedica.boi.*;
import pe.com.mascovet.receta.boi.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class PruebaProcedimientos {

    private static int comprobaciones = 0;

    private static void probar(String procedimiento, Runnable operacion, Set<String> existentes) {
        if (!existentes.contains(procedimiento))
            throw new AssertionError("Falta el procedimiento " + procedimiento);
        operacion.run();
        comprobaciones++;
        System.out.println("[OK] " + procedimiento);
    }

    public static void main(String[] args) {
        try {
            Set<String> procedimientos = new HashSet<>();
            try (Connection con = DBManager.getInstance().getConnection();
                 ResultSet rs = con.getMetaData().getProcedures(con.getCatalog(), null, "%")) {
                while (rs.next()) procedimientos.add(rs.getString("PROCEDURE_NAME").toUpperCase());
            }

            System.out.println("=== PROCEDIMIENTOS ALMACENADOS DE MASCOVET ===");
            System.out.println("Procedimientos encontrados: " + procedimientos.size());

            probar("LISTAR_USUARIO_TODOS", () -> new UsuarioBOImpl().listarTodos(), procedimientos);
            probar("LISTAR_CLIENTE_TODOS", () -> new ClienteBOImpl().listarTodos(), procedimientos);
            probar("LISTAR_VETERINARIO_TODOS", () -> new VeterinarioBOImpl().listarTodos(), procedimientos);
            probar("LISTAR_RECEPCIONISTA_TODOS", () -> new RecepcionistaBOImpl().listarTodos(), procedimientos);
            probar("LISTAR_ADMINISTRADOR_TODOS", () -> new AdministradorBOImpl().listarTodos(), procedimientos);
            probar("LISTAR_MASCOTA_TODOS", () -> new MascotaBOImpl().listarTodos(), procedimientos);
            probar("LISTAR_HORARIOATENCION_TODOS", () -> new HorarioAtencionBOImpl().listarTodos(), procedimientos);
            probar("LISTAR_CITA_TODOS", () -> new CitaBOImpl().listarTodos(), procedimientos);
            probar("LISTAR_CONSULTA_TODOS", () -> new ConsultaBOImpl().listarTodos(), procedimientos);
            probar("LISTAR_CONTROL_TODOS", () -> new ControlBOImpl().listarTodos(), procedimientos);
            probar("LISTAR_CIRUGIA_TODOS", () -> new CirugiaBOImpl().listarTodos(), procedimientos);
            probar("LISTAR_VACUNACION_TODOS", () -> new VacunacionBOImpl().listarTodos(), procedimientos);
            probar("LISTAR_VACUNA_TODOS", () -> new VacunaBOImpl().listarTodos(), procedimientos);
            probar("LISTAR_MEDICAMENTO_TODOS", () -> new MedicamentoBOImpl().listarTodos(), procedimientos);
            probar("LISTAR_RECETA_TODOS", () -> new RecetaBOImpl().listarTodos(), procedimientos);
            probar("LISTAR_DETALLERECETA_TODOS", () -> new DetalleRecetaBOImpl().listarTodos(), procedimientos);

            System.out.println("Comprobaciones correctas: " + comprobaciones + "/16");
            System.out.println("Para probar inserciones, modificaciones y rollback, ejecutar MainTest y PruebasNuevas.");
        } catch (Exception ex) {
            throw new RuntimeException("Fallo la prueba de procedimientos", ex);
        }
    }
}
