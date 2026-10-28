
package pe.com.mascovet.main;

import pe.com.mascovet.usuario.boi.*;
import pe.com.mascovet.cita.boi.*;
import pe.com.mascovet.mascota.boi.MascotaBOImpl;
import pe.com.mascovet.receta.boi.*;
import pe.com.mascovet.atencionmedica.boi.*;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;

public class PruebaProcedimientos {

    private static final String DNI_CLIENTE = "12345678";
    private static final String DNI_VETERINARIO = "23456781";

    // Usuarios existentes que todavía no tengan asignado el rol
    // que probará cada procedimiento.
    private static final String DNI_P_ASIGNAR_CLIENTE = "11111111";
    private static final String DNI_P_ASIGNAR_VET = "55555555";
    private static final String DNI_P_ASIGNAR_RECEP = "66666666";
    private static final String DNI_P_ASIGNAR_ADMIN = "77777777";

    private static final String MASCOTA = "Firulais";

    private static final LocalDate FECHA =
            LocalDate.now().plusWeeks(2);

    private static final Time HORA = Time.valueOf("10:00:00");
    private static final Time HORA_INICIO = Time.valueOf("09:00:00");
    private static final Time HORA_FIN = Time.valueOf("17:00:00");

    private static Date fecha() {
        return Date.valueOf(FECHA);
    }

    private static String diaSemana() {
        DayOfWeek dia = FECHA.getDayOfWeek();
        return dia.getDisplayName(TextStyle.FULL, new Locale("es", "ES"))
                .toUpperCase(new Locale("es", "ES"));
    }

    private static void probar(String nombre, Runnable operacion) {
        System.out.println("\n>>> " + nombre);

        try {
            operacion.run();
            System.out.println("[OK] " + nombre);
        } catch (Exception e) {
            System.err.println("[ERROR] " + nombre + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println(" PRUEBAS DE PROCEDIMIENTOS - MASCOVET");
        System.out.println("========================================");

        // 1. Asignar rol de cliente.
        probar("asignar_cliente", () ->
                new ClienteBOImpl().asignarClienteSP(
                        DNI_P_ASIGNAR_CLIENTE,
                        "prueba_cliente@correo.com",
                        "912345675"
                )
        );

        // 2. Asignar rol de veterinario.
        probar("asignar_veterinario", () ->
                new VeterinarioBOImpl().asignarVeterinarioSP(
                        DNI_P_ASIGNAR_VET,
                        "CMVP-TEST-02",
                        "MEDICINA_GENERAL"
                )
        );

        // 3. Asignar rol de recepcionista.
        probar("asignar_recepcionista", () ->
                new RecepcionistaBOImpl().asignarRecepcionistaSP(
                        DNI_P_ASIGNAR_RECEP,
                        "MANANA"
                )
        );

        // 4. Asignar rol de administrador.
        probar("asignar_administrador", () ->
                new AdministradorBOImpl().asignarAdministradorSP(
                        DNI_P_ASIGNAR_ADMIN,
                        "Administrador de prueba"
                )
        );

        // 5. Asignar horario al veterinario.
        probar("asignar_horario_veterinario", () ->
                new HorarioAtencionBOImpl().asignarHorarioVeterinarioSP(
                        DNI_VETERINARIO,
                        diaSemana(),
                        HORA_INICIO,
                        HORA_FIN
                )
        );

        // 6. Vincular una mascota al cliente.
        probar("vincular_mascota", () ->
                new MascotaBOImpl().vincularMascotaSP(
                        DNI_CLIENTE,
                        MASCOTA,
                        Date.valueOf("2022-01-15"),
                        "PERRO",
                        "Schnauzer",
                        "MACHO"
                )
        );

        // 7. Registrar una cita.
        probar("registrar_cita", () ->
                new CitaBOImpl().registrarCitaSP(
                        DNI_CLIENTE,
                        MASCOTA,
                        fecha(),
                        HORA,
                        "CONFIRMADA",
                        DNI_VETERINARIO
                )
        );

        // 8. Crear atención médica para la cita anterior.
        probar("crear_atencion_medica", () ->
                new AtencionMedicaBOImpl().crearAtencionMedicaSP(
                        DNI_CLIENTE,
                        MASCOTA,
                        fecha(),
                        HORA,
                        "Atención médica de prueba",
                        HORA_INICIO,
                        HORA_FIN
                )
        );

        // 9. Cambiar estado de la mascota.
        probar("cambiar_estado_mascota", () ->
                new MascotaBOImpl().cambiarEstadoMascotaSP(
                        DNI_CLIENTE,
                        MASCOTA,
                        true
                )
        );

        // 10. Registrar medicamento.
        probar("registrar_medicamento", () ->
                new MedicamentoBOImpl().registrarMedicamentoSP(
                        "Medicamento prueba",
                        "Medicamento registrado desde Java",
                        25.50,
                        Date.valueOf("2030-12-31")
                )
        );

        // 11. Registrar vacuna en el catálogo.
        probar("registrar_vacuna", () ->
                new VacunaBOImpl().registrarVacunaSP(
                        "Vacuna prueba",
                        "Vacuna registrada desde Java"
                )
        );

        // 12. Registrar consulta.
        probar("registrar_consulta", () ->
                new ConsultaBOImpl().registrarConsultaSP(
                        DNI_CLIENTE,
                        MASCOTA,
                        fecha(),
                        HORA,
                        "Consulta de prueba",
                        "Diagnóstico de prueba",
                        "Tratamiento de prueba"
                )
        );

        // 13. Registrar cirugía.
        probar("registrar_cirugia", () ->
                new CirugiaBOImpl().registrarCirugiaSP(
                        DNI_CLIENTE,
                        MASCOTA,
                        fecha(),
                        HORA,
                        "Procedimiento quirúrgico de prueba",
                        "Reposo y cuidados de prueba"
                )
        );

        // 14. Registrar control.
        probar("registrar_control", () ->
                new ControlBOImpl().registrarControlSP(
                        DNI_CLIENTE,
                        MASCOTA,
                        fecha(),
                        HORA,
                        "Evolución de prueba",
                        "Indicaciones de prueba"
                )
        );

        // 15. Registrar receta.
        probar("registrar_receta", () ->
                new RecetaBOImpl().registrarRecetaSP(
                        DNI_CLIENTE,
                        MASCOTA,
                        fecha(),
                        HORA,
                        fecha(),
                        "Indicaciones de receta de prueba"
                )
        );

        // 16. Registrar vacunación.
        probar("registrar_vacunacion", () ->
                new VacunacionBOImpl().registrarVacunacionSP(
                        DNI_CLIENTE,
                        MASCOTA,
                        fecha(),
                        HORA,
                        fecha(),
                        Date.valueOf(FECHA.plusMonths(1)),
                        "Primera dosis"
                )
        );

        System.out.println("\n========================================");
        System.out.println(" FIN DE LAS PRUEBAS");
        System.out.println("Revisa los mensajes [OK] y [ERROR].");
        System.out.println("========================================");
    }
}
