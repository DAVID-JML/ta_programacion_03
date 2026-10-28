package pe.com.mascovet.main;

import pe.com.mascovet.cita.boi.*;
import pe.com.mascovet.cita.model.*;
import pe.com.mascovet.usuario.boi.*;
import pe.com.mascovet.usuario.model.*;
import pe.com.mascovet.mascota.boi.MascotaBOImpl;
import pe.com.mascovet.mascota.model.Mascota;
import pe.com.mascovet.atencionmedica.boi.*;
import pe.com.mascovet.atencionmedica.model.*;
import pe.com.mascovet.receta.boi.*;
import pe.com.mascovet.receta.model.*;
import pe.com.mascovet.enums.model.*;
import pe.com.mascovet.config.*;
import java.sql.*;
import java.time.*;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


public class PruebasNuevas {
    private final CitaBOImpl citas = new CitaBOImpl();
    private final HorarioAtencionBOImpl horarios = new HorarioAtencionBOImpl();
    private final LocalDate lunes = LocalDate.now().plusWeeks(2)
            .with(java.time.temporal.TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
    private Cliente cliente;
    private Veterinario vet, otroVet;
    private Mascota mascota;
    private int comprobaciones;

    public static void main(String[] args) {
        ejecutar();
    }

    public static void ejecutar() {
        PruebasNuevas prueba = new PruebasNuevas();
        System.out.println("=== PRUEBAS NUEVAS MASCOVET ===");
        try {
            prueba.preparar();
            prueba.probarRF011();
            prueba.probarRF017();
            prueba.probarRF020();
            prueba.probarEliminacion();
            prueba.probarTransacciones();
            System.out.println("COMPLETADO: " + prueba.comprobaciones
                    + " comprobaciones correctas.");
        } catch (Exception | AssertionError ex) {
            System.err.println("FALLO: " + ex.getMessage());
            throw new AssertionError("La suite no terminó correctamente", ex);
        } finally {
            TransactionContext.rollback();
            TransactionContext.close();
        }
    }

    private void verificar(boolean condicion, String mensaje) {
        if (!condicion) throw new AssertionError(mensaje);
        comprobaciones++;
        System.out.println("[OK] " + mensaje);
    }

    private void rechazar(Runnable operacion, String fragmento) {
        try {
            operacion.run();
        } catch (RuntimeException ex) {
            StringBuilder mensajes = new StringBuilder();
            for (Throwable t = ex; t != null; t = t.getCause()) {
                mensajes.append(t.getMessage()).append(' ');
            }
            verificar(mensajes.toString().toLowerCase().contains(fragmento.toLowerCase()),
                    "Rechazo esperado: " + fragmento + " | recibido: " + mensajes);
            return;
        }
        throw new AssertionError("Debió rechazarse la operación: " + fragmento);
    }

    // Consultas independientes del BO: permiten comprobar persistencia real.
    private long numero(String sql, Object... parametros) throws SQLException {
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < parametros.length; i++) ps.setObject(i + 1, parametros[i]);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new AssertionError("Consulta sin resultado: " + sql);
                return rs.getLong(1);
            }
        }
    }

    private void preparar() throws SQLException {
        // Falla antes de crear datos si no se aplicó el ALTER TABLE.
        numero("SELECT COUNT(*) FROM CITA WHERE activo = TRUE");
        numero("SELECT COUNT(*) FROM VACUNACION_VACUNA");
        String marca = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        cliente = new Cliente(0, dni(), "Prueba", "Cliente", "pc_" + marca,
                "clave123", true, marca + "@example.com", telefonoUnico());
        verificar(new ClienteBOImpl().insertar(cliente) > 0, "Crear cliente de pruebas");
        vet = veterinario("pv_" + marca);
        otroVet = veterinario("po_" + marca);
        mascota = new Mascota();
        mascota.setCliente(cliente);
        mascota.setNombre("Mascota prueba " + marca);
        mascota.setFechaNacimiento(Date.valueOf(LocalDate.now().minusYears(2)));
        mascota.setEspecie(Especie.PERRO);
        mascota.setRaza("Mestizo");
        mascota.setSexo(Sexo.MACHO);
        verificar(new MascotaBOImpl().insertar(mascota) > 0, "Crear mascota de pruebas");
        System.out.println("Datos creados: cliente=" + cliente.getIdUsuario()
                + ", veterinarios=" + vet.getIdUsuario() + "/" + otroVet.getIdUsuario()
                + ", mascota=" + mascota.getIdMascota() + ", lunes=" + lunes);
    }

    private String dni() throws SQLException {
        String valor;
        do {
            valor = String.valueOf(ThreadLocalRandom.current().nextInt(10000000, 100000000));
        } while (numero("SELECT COUNT(*) FROM USUARIO WHERE dni = ?", valor) > 0);
        return valor;
    }

    private String telefonoUnico() throws SQLException {
        String telefono;
        do {
            telefono = "9" + ThreadLocalRandom.current().nextInt(10000000, 100000000);
        } while (numero("SELECT COUNT(*) FROM CLIENTE WHERE telefono=?", telefono) > 0);
        return telefono;
    }

    private Veterinario veterinario(String nombre) throws SQLException {
        Veterinario v = new Veterinario(0, dni(), "Prueba", "Veterinario", nombre,
                "clave123", true, "T" + nombre, Especialidad.MEDICINA_GENERAL);
        verificar(new VeterinarioBOImpl().insertar(v) > 0, "Crear " + nombre);
        return v;
    }

    private HorarioAtencion horario(Veterinario v, Dia dia, String inicio, String fin) {
        HorarioAtencion h = new HorarioAtencion(dia, LocalTime.parse(inicio), LocalTime.parse(fin));
        h.setVeterinario(v);
        return h;
    }

    private void probarRF011() throws SQLException {
        System.out.println("--- RF011 ---");
        HorarioAtencion base = horario(vet, Dia.LUNES, "09:00", "12:00");
        verificar(horarios.insertar(base) > 0, "Insertar horario base");
        for (String[] intervalo : new String[][] {
                {"08:00", "10:00"}, {"10:00", "11:00"}, {"11:00", "13:00"},
                {"08:00", "13:00"}, {"09:00", "12:00"}}) {
            rechazar(() -> horarios.insertar(horario(vet, Dia.LUNES,
                    intervalo[0], intervalo[1])), "superpone");
        }
        verificar(numero("SELECT COUNT(*) FROM HORARIO_ATENCION WHERE id_veterinario=? AND dia='LUNES'",
                vet.getIdUsuario()) == 1, "Los cruces rechazados no se guardaron");
        verificar(horarios.modificar(base) > 0, "Modificar sin compararse consigo mismo");
        HorarioAtencion consecutivo = horario(vet, Dia.LUNES, "12:00", "14:00");
        verificar(horarios.insertar(consecutivo) > 0, "Permitir horarios consecutivos");
        consecutivo.setHoraInicio(LocalTime.of(11, 0));
        rechazar(() -> horarios.modificar(consecutivo), "superpone");
        verificar(numero("SELECT HOUR(hora_inicio) FROM HORARIO_ATENCION WHERE id_horario=?",
                consecutivo.getIdHorario()) == 12, "Modificar con cruce conserva el horario guardado");
        verificar(horarios.insertar(horario(otroVet, Dia.LUNES, "09:00", "14:00")) > 0,
                "Permitir iguales horas para otro veterinario");
        verificar(horarios.insertar(horario(vet, Dia.MARTES, "09:00", "14:00")) > 0,
                "Permitir iguales horas en otro día");
        rechazar(() -> horarios.insertar(horario(vet, Dia.MIERCOLES, "12:00", "09:00")),
                "anterior");
    }

    private Cita nueva(Veterinario v, LocalDate fecha, String hora) {
        Cita c = new Cita();
        c.setMascota(mascota);
        c.setCliente(cliente);
        c.setVeterinario(v);
        c.setFecha(Date.valueOf(fecha));
        c.setHora(LocalTime.parse(hora));
        return c;
    }

    private Cita reservar(String hora) {
        Cita c = nueva(vet, lunes, hora);
        verificar(citas.insertar(c) > 0, "Reservar cita " + c.getIdCita() + " a las " + hora);
        return c;
    }

    private void estado(Cita c, EstadoCita nuevo) {
        c.setEstado(nuevo);
        verificar(citas.modificar(c) == 1, "Cambiar cita " + c.getIdCita() + " a " + nuevo);
        verificar(citas.obtenerPorId(c.getIdCita()).getEstado() == nuevo,
                "Estado persistido: " + nuevo);
    }

    private void probarRF017() throws SQLException {
        System.out.println("--- RF017 ---");
        Cita c = reservar("09:00");
        rechazar(() -> citas.insertar(nueva(vet, lunes, "09:00")), "cita activa");
        verificar(numero("SELECT COUNT(*) FROM CITA WHERE id_veterinario=? AND fecha=? AND hora=? AND activo=TRUE",
                        vet.getIdUsuario(), Date.valueOf(lunes), "09:00:00") == 1,
                "No se persistió la cita duplicada");
        verificar(citas.modificar(c) == 1, "Modificar cita sin bloquearse consigo misma");
        Cita otra = reservar("10:00");
        otra.setHora(LocalTime.of(9, 0));
        rechazar(() -> citas.modificar(otra), "cita activa");
        verificar(citas.obtenerPorId(otra.getIdCita()).getHora().equals(LocalTime.of(10, 0)),
                "Reprogramación rechazada conserva la hora original");
        otra.setHora(LocalTime.of(11, 0));
        verificar(citas.modificar(otra) == 1, "Reprogramar a una hora libre");
        rechazar(() -> citas.insertar(nueva(vet, lunes, "08:00")), "no atiende");
        rechazar(() -> citas.insertar(nueva(vet, lunes, "14:00")), "no atiende");
        rechazar(() -> citas.insertar(nueva(vet, lunes.plusDays(2), "10:00")), "no atiende");
        verificar(citas.insertar(nueva(otroVet, lunes, "09:00")) > 0,
                "Misma fecha y hora para otro veterinario");
        verificar(citas.insertar(nueva(vet, lunes.plusDays(1), "09:00")) > 0,
                "Misma hora en otra fecha");
        estado(c, EstadoCita.CANCELADA);
        reservar("09:00");
    }

    private void datosAtencion(AtencionMedica a, Cita c) {
        a.setCita(c);
        a.setMascota(mascota);
        a.setHoraInicio(c.getHora());
        a.setHoraFin(c.getHora().plusMinutes(20));
        a.setPesoActual(8.0);
        a.setObservaciones("PRUEBA INTEGRACION");
        a.setAlergias("Ninguna registrada");
    }

    private Consulta consulta(Cita c) {
        Consulta a = new Consulta();
        datosAtencion(a, c);
        a.setMotivoConsulta("Control de prueba");
        a.setDiagnostico("Diagnostico de prueba");
        a.setTratamiento("Tratamiento de prueba");
        return a;
    }

    private void probarRF020() throws SQLException {
        System.out.println("--- RF020 ---");
        Cita c = reservar("10:00");
        verificar(citas.obtenerPorId(c.getIdCita()).getEstado() == EstadoCita.RESERVADA,
                "Nueva cita persistida como RESERVADA");
        c.setEstado(EstadoCita.ATENDIDA);
        rechazar(() -> citas.modificar(c), "registrarse una atención");
        c.setEstado(EstadoCita.RESERVADA);
        estado(c, EstadoCita.CONFIRMADA);
        Consulta a = consulta(c);
        verificar(new ConsultaBOImpl().insertar(a) > 0, "Registrar consulta");
        verificar(citas.obtenerPorId(c.getIdCita()).getEstado() == EstadoCita.ATENDIDA,
                "Consulta actualiza automáticamente a ATENDIDA");
        c.setEstado(EstadoCita.CANCELADA);
        rechazar(() -> citas.modificar(c), "No se puede cambiar");
        verificar(citas.obtenerPorId(c.getIdCita()).getEstado() == EstadoCita.ATENDIDA,
                "ATENDIDA no se cancela");
        Cita cancelada = reservar("11:30");
        estado(cancelada, EstadoCita.CANCELADA);
        cancelada.setEstado(EstadoCita.CONFIRMADA);
        rechazar(() -> citas.modificar(cancelada), "No se puede cambiar");
        rechazar(() -> new ConsultaBOImpl().insertar(consulta(cancelada)), "confirmada");
        verificar(numero("SELECT COUNT(*) FROM ATENCION_MEDICA WHERE id_cita=?",
                cancelada.getIdCita()) == 0, "No se registra atención para cita cancelada");
        Cita reservada = reservar("11:45");
        rechazar(() -> new ConsultaBOImpl().insertar(consulta(reservada)), "confirmada");
        verificar(numero("SELECT COUNT(*) FROM ATENCION_MEDICA WHERE id_cita=?",
                reservada.getIdCita()) == 0, "No se registra atención sin confirmación");
        probarOtrosTipos();
    }

    private Cita confirmada(String hora) {
        Cita c = reservar(hora);
        estado(c, EstadoCita.CONFIRMADA);
        return c;
    }

    private void atendida(Cita c) {
        verificar(citas.obtenerPorId(c.getIdCita()).getEstado() == EstadoCita.ATENDIDA,
                "Cita " + c.getIdCita() + " quedó ATENDIDA");
    }

    private void probarOtrosTipos() {
        Cita c = confirmada("12:00");
        Control control = new Control();
        datosAtencion(control, c);
        control.setEvolucion("Evolucion de prueba");
        control.setIndicaciones("Indicaciones de prueba");
        verificar(new ControlBOImpl().insertar(control) > 0, "Insertar control");
        atendida(c);
        c = confirmada("12:30");
        Cirugia cirugia = new Cirugia();
        datosAtencion(cirugia, c);
        cirugia.setProcedimiento("Procedimiento de prueba");
        cirugia.setIndicacionesPostOperatorias("Indicaciones de prueba");
        verificar(new CirugiaBOImpl().insertar(cirugia) > 0, "Insertar cirugía");
        atendida(c);
        c = confirmada("13:00");
        verificar(new VacunacionBOImpl().insertar(vacunacion(c)) > 0, "Insertar vacunación simple");
        atendida(c);
    }

    private void probarEliminacion() throws SQLException {
        System.out.println("--- ELIMINACION LOGICA ---");
        Cita c = reservar("13:30");
        verificar(citas.eliminar(c.getIdCita()) == 1, "Eliminar lógicamente");
        verificar(numero("SELECT COUNT(*) FROM CITA WHERE id_cita=? AND activo=FALSE AND estado='RESERVADA'",
                c.getIdCita()) == 1, "La fila existe, está inactiva y conserva su estado");
        verificar(citas.obtenerPorId(c.getIdCita()) == null, "ObtenerPorId excluye eliminadas");
        verificar(citas.listarTodos().stream().noneMatch(x -> x.getIdCita() == c.getIdCita()),
                "ListarTodos excluye eliminadas");
        rechazar(() -> citas.eliminar(c.getIdCita()), "ya fue eliminada");
        rechazar(() -> citas.modificar(c), "No existe la cita");
        rechazar(() -> new ConsultaBOImpl().insertar(consulta(c)), "eliminada");
        reservar("13:30");
        Cita conHistoria = citas.listarTodos().stream().filter(x ->
                x.getVeterinario() != null && x.getVeterinario().getIdUsuario() == vet.getIdUsuario()
                        && x.getEstado() == EstadoCita.ATENDIDA).findFirst().orElseThrow();
        long antes = numero("SELECT COUNT(*) FROM ATENCION_MEDICA WHERE id_cita=?", conHistoria.getIdCita());
        citas.eliminar(conHistoria.getIdCita());
        verificar(antes > 0 && numero("SELECT COUNT(*) FROM ATENCION_MEDICA WHERE id_cita=?",
                conHistoria.getIdCita()) == antes, "Eliminar cita conserva sus atenciones médicas");
    }

    private Vacunacion vacunacion(Cita c) {
        Vacunacion v = new Vacunacion();
        datosAtencion(v, c);
        v.setFechaAplicacion(Date.valueOf(lunes));
        v.setFechaProximaDosis(Date.valueOf(lunes.plusMonths(1)));
        v.setDosis("Dosis de prueba");
        return v;
    }

    private Vacuna vacuna() {
        Vacuna v = new Vacuna();
        v.setNombre("Vacuna prueba");
        v.setDescripcion("Descripcion de prueba");
        return v;
    }

    private Receta receta(Consulta a, Medicamento medicamento) {
        Receta r = new Receta();
        r.setAtencionMedica(a);
        r.setFecha(Date.valueOf(lunes));
        r.setIndicaciones("Indicaciones de prueba");
        r.agregarDetalleReceta(detalle(medicamento));
        r.agregarDetalleReceta(detalle(medicamento));
        return r;
    }

    private DetalleReceta detalle(Medicamento medicamento) {
        DetalleReceta d = new DetalleReceta();
        d.setMedicamento(medicamento);
        d.setDosis("Dosis prueba");
        d.setFrecuencia("Frecuencia prueba");
        d.setDuracion("Duracion prueba");
        d.setMontoTotal(10.0);
        return d;
    }

    private void probarTransacciones() throws SQLException {
        System.out.println("--- TRANSACCIONES COMPUESTAS Y ROLLBACK ---");
        Cita c = confirmada("10:30");
        Consulta a = consulta(c);
        verificar(new ConsultaBOImpl().insertar(a) > 0, "Crear atención para recetas");
        Medicamento m = new Medicamento();
        m.setNombre("Medicamento prueba " + UUID.randomUUID().toString().substring(0, 8));
        m.setDescripcion("Descripcion de prueba");
        m.setMonto(10.0);
        verificar(new MedicamentoBOImpl().insertar(m) > 0, "Crear medicamento de prueba");
        Receta r = receta(a, m);
        verificar(new RecetaBOImpl().insertarConDetalles(r) > 0, "insertarConDetalles devuelve éxito");
        verificar(numero("SELECT COUNT(*) FROM RECETA WHERE id_receta=?", r.getIdReceta()) == 1,
                "Se persistió la cabecera de receta");
        verificar(numero("SELECT COUNT(*) FROM DETALLE_RECETA WHERE id_receta=?", r.getIdReceta()) == 2,
                "Se persistieron los dos detalles");

        // Fallo controlado SOLO en el test: valida normalmente, falla al guardar el segundo detalle.
        Consulta atencionRollback = consulta(c);
        verificar(new ConsultaBOImpl().insertar(atencionRollback) > 0,
                "Crear atención independiente para rollback de receta");
        Receta fallida = receta(atencionRollback, m);
        DetalleReceta segundo = new DetalleReceta() {
            @Override public String getDosis() {
                if (TransactionContext.getCurrentConnection() != null)
                    throw new RuntimeException("FALLO_INYECTADO_DETALLE");
                return super.getDosis();
            }
        };
        segundo.setMedicamento(m);
        segundo.setDosis("Dosis prueba");
        segundo.setFrecuencia("Frecuencia prueba");
        segundo.setDuracion("Duracion prueba");
        fallida.getDetallesReceta().set(1, segundo);
        rechazar(() -> new RecetaBOImpl().insertarConDetalles(fallida), "FALLO_INYECTADO_DETALLE");
        verificar(fallida.getIdReceta() > 0 && fallida.getDetallesReceta().get(0).getIdDetalleReceta() > 0,
                "El fallo ocurrió después de insertar cabecera y primer detalle");
        verificar(numero("SELECT COUNT(*) FROM RECETA WHERE id_receta=?", fallida.getIdReceta()) == 0,
                "Rollback revierte la cabecera de receta");
        verificar(numero("SELECT COUNT(*) FROM DETALLE_RECETA WHERE id_receta=?", fallida.getIdReceta()) == 0,
                "Rollback revierte los detalles");

        Cita cv = confirmada("10:45");
        Vacunacion v = vacunacion(cv);
        v.agregarVacuna(vacuna());
        v.agregarVacuna(vacuna());
        verificar(new VacunacionBOImpl().insertarConVacunas(v) > 0, "insertarConVacunas devuelve éxito");
        verificar(numero("SELECT COUNT(*) FROM ATENCION_MEDICA WHERE id_atencion=?", v.getIdAtencion()) == 1,
                "Se persistió la atención base");
        verificar(numero("SELECT COUNT(*) FROM VACUNACION WHERE id_atencion=?", v.getIdAtencion()) == 1,
                "Se persistió la vacunación");
        verificar(numero("SELECT COUNT(*) FROM VACUNACION_VACUNA WHERE id_atencion=?", v.getIdAtencion()) == 2,
                "Se persistieron las dos asociaciones de vacunas");
        verificar(numero("SELECT COUNT(*) FROM VACUNA WHERE id_vacuna IN (?, ?)",
                        v.getVacunas().get(0).getIdVacuna(), v.getVacunas().get(1).getIdVacuna()) == 2,
                "Se persistieron las dos vacunas nuevas del catalogo");
        atendida(cv);

        Cita reutilizada = confirmada("11:20");
        Vacunacion reutilizar = vacunacion(reutilizada);
        Vacuna delCatalogo = new Vacuna();
        delCatalogo.setIdVacuna(v.getVacunas().get(0).getIdVacuna());
        reutilizar.agregarVacuna(delCatalogo);
        verificar(new VacunacionBOImpl().insertarConVacunas(reutilizar) > 0,
                "Reutilizar vacuna existente del catalogo");
        verificar(numero("SELECT COUNT(*) FROM VACUNACION_VACUNA WHERE id_atencion=? AND id_vacuna=?",
                        reutilizar.getIdAtencion(), delCatalogo.getIdVacuna()) == 1,
                "La vacuna existente se asocio a otra vacunacion");
        atendida(reutilizada);

        Cita cr = confirmada("11:15");
        Vacunacion vr = vacunacion(cr);
        vr.agregarVacuna(vacuna());
        Vacuna falla = new Vacuna() {
            @Override public String getNombre() {
                if (TransactionContext.getCurrentConnection() != null)
                    throw new RuntimeException("FALLO_INYECTADO_VACUNA");
                return super.getNombre();
            }
        };
        falla.setNombre("Vacuna prueba");
        falla.setDescripcion("Descripcion de prueba");
        vr.agregarVacuna(falla);
        rechazar(() -> new VacunacionBOImpl().insertarConVacunas(vr), "FALLO_INYECTADO_VACUNA");
        verificar(vr.getIdAtencion() > 0 && vr.getVacunas().get(0).getIdVacuna() > 0,
                "El fallo ocurrió después de insertar atención, vacunación y primera vacuna");
        verificar(numero("SELECT COUNT(*) FROM ATENCION_MEDICA WHERE id_atencion=?", vr.getIdAtencion()) == 0,
                "Rollback revierte atención base");
        verificar(numero("SELECT COUNT(*) FROM VACUNACION WHERE id_atencion=?", vr.getIdAtencion()) == 0,
                "Rollback revierte vacunación");
        verificar(numero("SELECT COUNT(*) FROM VACUNACION_VACUNA WHERE id_atencion=?", vr.getIdAtencion()) == 0,
                "Rollback revierte las asociaciones de vacunas");
        verificar(numero("SELECT COUNT(*) FROM VACUNA WHERE id_vacuna=?",
                        vr.getVacunas().get(0).getIdVacuna()) == 0,
                "Rollback revierte la vacuna nueva del catalogo");
        verificar(numero("SELECT COUNT(*) FROM VACUNA WHERE id_vacuna=?",
                        delCatalogo.getIdVacuna()) == 1,
                "Rollback conserva vacunas confirmadas previamente");
        verificar(citas.obtenerPorId(cr.getIdCita()).getEstado() == EstadoCita.CONFIRMADA,
                "Rollback restaura el estado CONFIRMADA de la cita");
        verificar(TransactionContext.getCurrentConnection() == null, "Se cerró el contexto transaccional");
    }
}
