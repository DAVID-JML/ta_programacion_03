package pe.com.mascovet.main;

import pe.com.mascovet.atencionmedica.boi.ConsultaBOImpl;
import pe.com.mascovet.atencionmedica.boi.VacunacionBOImpl;
import pe.com.mascovet.atencionmedica.bo.IConsultaBO;
import pe.com.mascovet.atencionmedica.bo.IVacunacionBO;
import pe.com.mascovet.atencionmedica.model.Consulta;
import pe.com.mascovet.atencionmedica.model.Vacunacion;
import pe.com.mascovet.cita.boi.CitaBOImpl;
import pe.com.mascovet.cita.boi.HorarioAtencionBOImpl;
import pe.com.mascovet.cita.bo.IHorarioAtencionBO;
import pe.com.mascovet.cita.bo.ICitaBO;
import pe.com.mascovet.cita.model.Cita;
import pe.com.mascovet.cita.model.HorarioAtencion;
import pe.com.mascovet.enums.model.Dia;
import pe.com.mascovet.atencionmedica.model.Vacuna;
import pe.com.mascovet.receta.boi.RecetaBOImpl;
import pe.com.mascovet.receta.boi.DetalleRecetaBOImpl;
import pe.com.mascovet.receta.bo.IDetalleRecetaBO;
import pe.com.mascovet.receta.model.Receta;
import pe.com.mascovet.receta.model.DetalleReceta;
import pe.com.mascovet.enums.model.Especialidad;
import pe.com.mascovet.enums.model.Especie;
import pe.com.mascovet.enums.model.EstadoCita;
import pe.com.mascovet.enums.model.TipoAtencion;
import pe.com.mascovet.enums.model.Sexo;
import pe.com.mascovet.mascota.boi.MascotaBOImpl;
import pe.com.mascovet.mascota.bo.IMascotaBO;
import pe.com.mascovet.mascota.model.Mascota;
import pe.com.mascovet.receta.boi.MedicamentoBOImpl;
import pe.com.mascovet.receta.bo.IMedicamentoBO;
import pe.com.mascovet.receta.model.Medicamento;
import pe.com.mascovet.usuario.boi.ClienteBOImpl;
import pe.com.mascovet.usuario.boi.RecepcionistaBOImpl;
import pe.com.mascovet.usuario.boi.VeterinarioBOImpl;
import pe.com.mascovet.usuario.bo.IClienteBO;
import pe.com.mascovet.usuario.bo.IRecepcionistaBO;
import pe.com.mascovet.usuario.bo.IVeterinarioBO;
import pe.com.mascovet.usuario.model.Cliente;
import pe.com.mascovet.usuario.model.Recepcionista;
import pe.com.mascovet.usuario.model.Veterinario;

import java.sql.Date;
import java.time.LocalTime;
import java.time.LocalDate;

public class MainTest {

    public static void main(String[] args) {

        IClienteBO clienteBO = new ClienteBOImpl();
        IMascotaBO mascotaBO = new MascotaBOImpl();
        ICitaBO citaBO = new CitaBOImpl();
        IConsultaBO consultaBO = new ConsultaBOImpl();
        IMedicamentoBO medicamentoBO = new MedicamentoBOImpl();
        IVacunacionBO vacunacionBO = new VacunacionBOImpl();
        IVeterinarioBO veterinarioBO = new VeterinarioBOImpl();
        IRecepcionistaBO recepcionistaBO = new RecepcionistaBOImpl();
        IHorarioAtencionBO horarioBO = new HorarioAtencionBOImpl();
        IDetalleRecetaBO detalleBO = new DetalleRecetaBOImpl();

        try {

            String sufijo = String.valueOf(1000000 + (System.currentTimeMillis() % 9000000));
            System.out.println("====================================================");
            System.out.println("        PRUEBA DE 6 ENTIDADES - MASCOVET");
            System.out.println("====================================================");

            System.out.println("\n========== 1. CLIENTE (USUARIO) ==========");

            Cliente cliente1 = new Cliente(
                    0, "1" + sufijo, "Carlos", "Ramirez", "Paredes",
                    "cramirez" + sufijo, "clave123", true,
                    "carlos" + sufijo + "@gmail.com", "91" + sufijo
            );

            Cliente cliente2 = new Cliente(
                    0, "2" + sufijo, "Lucia", "Torres", "Cruz",
                    "ltorres" + sufijo, "clave123", true,
                    "lucia" + sufijo + "@gmail.com", "92" + sufijo
            );

            Cliente cliente3 = new Cliente(
                    0, "3" + sufijo, "Pedro", "Mendoza", "Rojas",
                    "pmendoza" + sufijo, "clave123", true,
                    "pedro" + sufijo + "@gmail.com", "93" + sufijo
            );

            int idCliente1 = clienteBO.insertar(cliente1);
            int idCliente2 = clienteBO.insertar(cliente2);
            int idCliente3 = clienteBO.insertar(cliente3);

            System.out.println("Clientes insertados: "
                    + idCliente1 + ", " + idCliente2 + ", " + idCliente3);

            Cliente clienteObtenido = clienteBO.obtenerPorId(idCliente1);

            System.out.println("\n--- obtenerPorId() CLIENTE ---");
            System.out.println("ID Usuario: " + clienteObtenido.getIdUsuario());
            System.out.println("DNI heredado de Usuario: " + clienteObtenido.getDni());
            System.out.println("Nombre heredado: " + clienteObtenido.getNombre());
            System.out.println("Apellido heredado: " + clienteObtenido.getApellidoPaterno());
            System.out.println("Nombre de usuario heredado: " + clienteObtenido.getNombreUsuario());
            System.out.println("Activo heredado: " + clienteObtenido.isActivo());
            System.out.println("Correo propio de Cliente: " + clienteObtenido.getCorreo());
            System.out.println("Telefono propio de Cliente: " + clienteObtenido.getTelefono());

            clienteObtenido.setTelefono("94" + sufijo);
            clienteBO.modificar(clienteObtenido);

            clienteObtenido = clienteBO.obtenerPorId(idCliente1);
            System.out.println("Telefono luego de modificar: " + clienteObtenido.getTelefono());

            System.out.println("\n--- listarTodos() CLIENTE: se deben ver 3 ---");
            for (Cliente c : clienteBO.listarTodos()) {
                System.out.println(
                        "ID=" + c.getIdUsuario()
                                + " | DNI=" + c.getDni()
                                + " | Nombre=" + c.getNombre() + " " + c.getApellidoPaterno()
                                + " | Usuario=" + c.getNombreUsuario()
                                + " | Correo=" + c.getCorreo()
                                + " | Telefono=" + c.getTelefono()
                                + " | Activo=" + c.isActivo()
                );
            }

            Veterinario veterinario = new Veterinario(
                    0, "4" + sufijo, "Andrea", "Salazar", "Vega",
                    "asalazar" + sufijo, "clave123", true,
                    "V" + sufijo, Especialidad.MEDICINA_GENERAL
            );
            veterinario.setCorreo("andrea@mascovet.pe");
            veterinario.setTelefono("999111333");

            for (Dia dia : Dia.values()) {
                veterinario.agregarHorarioAtencion(
                        new HorarioAtencion(dia, LocalTime.of(9, 0), LocalTime.of(17, 0)));
            }
            System.out.println("\n========== TRANSACCION 1: VETERINARIO CON HORARIOS ==========");
            veterinarioBO.insertarConHorarios(veterinario);
            int idVeterinario = veterinario.getIdUsuario();
            veterinario = veterinarioBO.obtenerPorId(idVeterinario);
            if (veterinario == null || idVeterinario <= 0)
                throw new RuntimeException("TRANSACCION 1 FALLO: no se registro el veterinario");

            int horariosRegistrados = 0;
            for (HorarioAtencion h : horarioBO.listarTodos()) {
                if (h.getVeterinario().getIdUsuario() == idVeterinario)
                    horariosRegistrados++;
            }
            if (horariosRegistrados != Dia.values().length)
                throw new RuntimeException("TRANSACCION 1 FALLO: se esperaban "
                        + Dia.values().length + " horarios y se encontraron " + horariosRegistrados);
            System.out.println("Veterinario registrado: ID=" + idVeterinario);
            System.out.println("Horarios registrados: " + horariosRegistrados);
            System.out.println("TRANSACCION 1: EXITOSA");

            Recepcionista recepcionista = new Recepcionista(
                    0, "5" + sufijo, "Maria", "Lopez", "Flores",
                    "mlopez" + sufijo, "clave123", true,
                    "MANANA"
            );
            recepcionista.setCorreo("maria@mascovet.pe");
            recepcionista.setTelefono("999111444");
            int idRecepcionista = recepcionistaBO.insertar(recepcionista);
            recepcionista = recepcionistaBO.obtenerPorId(idRecepcionista);

            System.out.println("\n========== 2. MASCOTA ==========");

            Mascota mascota1 = crearMascota(clienteObtenido, "Firulais",
                    "2022-05-10", Especie.PERRO, "Labrador", Sexo.MACHO);
            Mascota mascota2 = crearMascota(clienteObtenido, "Luna",
                    "2023-02-15", Especie.GATO, "Siames", Sexo.HEMBRA);
            Mascota mascota3 = crearMascota(clienteObtenido, "Toby",
                    "2021-11-20", Especie.PERRO, "Beagle", Sexo.MACHO);

            int idMascota1 = mascotaBO.insertar(mascota1);
            int idMascota2 = mascotaBO.insertar(mascota2);
            int idMascota3 = mascotaBO.insertar(mascota3);

            System.out.println("Mascotas insertadas: "
                    + idMascota1 + ", " + idMascota2 + ", " + idMascota3);

            Mascota mascotaObtenida = mascotaBO.obtenerPorId(idMascota1);
            System.out.println("\n--- obtenerPorId() MASCOTA ---");
            System.out.println("ID: " + mascotaObtenida.getIdMascota());
            System.out.println("Nombre: " + mascotaObtenida.getNombre());
            System.out.println("Especie: " + mascotaObtenida.getEspecie());
            System.out.println("Raza: " + mascotaObtenida.getRaza());
            System.out.println("ID Cliente relacionado: "
                    + mascotaObtenida.getCliente().getIdUsuario());

            mascotaObtenida.setRaza("Labrador Retriever");
            mascotaBO.modificar(mascotaObtenida);

            mascotaObtenida = mascotaBO.obtenerPorId(idMascota1);
            System.out.println("Raza luego de modificar: " + mascotaObtenida.getRaza());

            System.out.println("\n--- listarTodos() MASCOTA: se deben ver 3 ---");
            for (Mascota m : mascotaBO.listarTodos()) {
                System.out.println(
                        "ID=" + m.getIdMascota()
                                + " | Nombre=" + m.getNombre()
                                + " | Especie=" + m.getEspecie()
                                + " | Raza=" + m.getRaza()
                                + " | Sexo=" + m.getSexo()
                                + " | Cliente=" + m.getCliente().getIdUsuario()
                                + " | Activo=" + m.isActivo()
                );
            }

            System.out.println("\n========== 3. CITA ==========");

            Cita cita1 = crearCita(mascotaObtenida, clienteObtenido,
                    recepcionista, veterinario,
                    LocalDate.now().plusDays(7).toString(), 10, 30);

            Cita cita2 = crearCita(mascotaObtenida, clienteObtenido,
                    recepcionista, veterinario,
                    LocalDate.now().plusDays(8).toString(), 11, 0);

            Cita cita3 = crearCita(mascotaObtenida, clienteObtenido,
                    recepcionista, veterinario,
                    LocalDate.now().plusDays(9).toString(), 15, 30);

            cita2.setTipoAtencion(TipoAtencion.VACUNACION);
            int idCita1 = citaBO.insertar(cita1);
            int idCita2 = citaBO.insertar(cita2);
            int idCita3 = citaBO.insertar(cita3);

            System.out.println("Citas insertadas: "
                    + idCita1 + ", " + idCita2 + ", " + idCita3);

            Cita citaObtenida = citaBO.obtenerPorId(idCita1);

            System.out.println("\n--- obtenerPorId() CITA ---");
            System.out.println("ID Cita: " + citaObtenida.getIdCita());
            System.out.println("ID Mascota: " + citaObtenida.getMascota().getIdMascota());
            System.out.println("ID Cliente: " + citaObtenida.getCliente().getIdUsuario());
            System.out.println("ID Veterinario: " + citaObtenida.getVeterinario().getIdUsuario());
            System.out.println("ID Recepcionista: "
                    + citaObtenida.getRecepcionista().getIdUsuario());
            System.out.println("Estado: " + citaObtenida.getEstado());

            citaObtenida.setEstado(EstadoCita.CONFIRMADA);
            citaBO.modificar(citaObtenida);
            citaObtenida = citaBO.obtenerPorId(idCita1);
            System.out.println("Estado luego de modificar: " + citaObtenida.getEstado());

            System.out.println("\n--- listarTodos() CITA: se deben ver 3 ---");
            for (Cita c : citaBO.listarTodos()) {
                System.out.println(
                        "ID=" + c.getIdCita()
                                + " | Mascota=" + c.getMascota().getIdMascota()
                                + " | Veterinario=" + c.getVeterinario().getIdUsuario()
                                + " | Fecha=" + c.getFecha()
                                + " | Hora=" + c.getHora()
                                + " | Estado=" + c.getEstado()
                );
            }

            System.out.println("\n========== 4. CONSULTA ==========");

            Consulta consulta = new Consulta();

            consulta.setCita(citaObtenida);
            consulta.setObservaciones("Paciente con falta de apetito");
            consulta.setHoraInicio(LocalTime.of(10, 30));
            consulta.setHoraFin(LocalTime.of(11, 0));
            consulta.setPesoActual(25.5);
            consulta.setAlergias("Ninguna");
            consulta.setMotivoConsulta("Falta de apetito");
            consulta.setDiagnostico("Gastritis leve");
            consulta.setTratamiento("Dieta blanda y control");

            int idConsulta = consultaBO.insertar(consulta);
            System.out.println("ID de Atencion/Consulta generado: " + idConsulta);

            Consulta consultaObtenida = consultaBO.obtenerPorId(idConsulta);

            System.out.println("\n--- obtenerPorId() CONSULTA ---");
            System.out.println("ID Atencion heredado: " + consultaObtenida.getIdAtencion());
            System.out.println("Observaciones heredadas: " + consultaObtenida.getObservaciones());
            System.out.println("Hora inicio heredada: " + consultaObtenida.getHoraInicio());
            System.out.println("Hora fin heredada: " + consultaObtenida.getHoraFin());
            System.out.println("Peso heredado: " + consultaObtenida.getPesoActual());
            System.out.println("Alergias heredadas: " + consultaObtenida.getAlergias());
            System.out.println("Motivo propio: " + consultaObtenida.getMotivoConsulta());
            System.out.println("Diagnostico propio: " + consultaObtenida.getDiagnostico());
            System.out.println("Tratamiento propio: " + consultaObtenida.getTratamiento());

            consultaObtenida.setDiagnostico("Gastritis leve en recuperacion");
            consultaBO.modificar(consultaObtenida);

            consultaObtenida = consultaBO.obtenerPorId(idConsulta);
            System.out.println("Diagnostico luego de modificar: "
                    + consultaObtenida.getDiagnostico());

            System.out.println("\n--- listarTodos() CONSULTA ---");
            for (Consulta c : consultaBO.listarTodos()) {
                System.out.println(
                        "ID=" + c.getIdAtencion()
                                + " | Observaciones=" + c.getObservaciones()
                                + " | Motivo=" + c.getMotivoConsulta()
                                + " | Diagnostico=" + c.getDiagnostico()
                );
            }

            System.out.println("\n========== 5. MEDICAMENTO ==========");

            Medicamento medicamento1 = crearMedicamento(
                    "Amoxicilina", "Antibiotico de uso veterinario", 35.50);
            Medicamento medicamento2 = crearMedicamento(
                    "Meloxicam", "Antiinflamatorio de uso veterinario", 28.00);
            Medicamento medicamento3 = crearMedicamento(
                    "Omeprazol", "Protector gastrico", 18.50);

            int idMedicamento1 = medicamentoBO.insertar(medicamento1);
            int idMedicamento2 = medicamentoBO.insertar(medicamento2);
            int idMedicamento3 = medicamentoBO.insertar(medicamento3);

            System.out.println("Medicamentos insertados: "
                    + idMedicamento1 + ", " + idMedicamento2 + ", " + idMedicamento3);

            Medicamento medicamentoObtenido =
                    medicamentoBO.obtenerPorId(idMedicamento1);

            System.out.println("\n--- obtenerPorId() MEDICAMENTO ---");
            System.out.println("ID: " + medicamentoObtenido.getIdMedicamento());
            System.out.println("Nombre: " + medicamentoObtenido.getNombre());
            System.out.println("Descripcion: " + medicamentoObtenido.getDescripcion());
            System.out.println("Monto: " + medicamentoObtenido.getMonto());

            medicamentoObtenido.setMonto(40.00);
            medicamentoBO.modificar(medicamentoObtenido);
            medicamentoObtenido = medicamentoBO.obtenerPorId(idMedicamento1);
            System.out.println("Monto luego de modificar: " + medicamentoObtenido.getMonto());

            System.out.println("\n--- listarTodos() MEDICAMENTO: se deben ver 3 ---");
            for (Medicamento m : medicamentoBO.listarTodos()) {
                System.out.println(
                        "ID=" + m.getIdMedicamento()
                                + " | Nombre=" + m.getNombre()
                                + " | Descripcion=" + m.getDescripcion()
                                + " | Monto=" + m.getMonto()
                );
            }

            System.out.println("\n========== 6. VACUNACION ==========");

            Cita citaParaVacunacion = citaBO.obtenerPorId(idCita2);
            citaParaVacunacion.setEstado(EstadoCita.CONFIRMADA);
            citaBO.modificar(citaParaVacunacion);
            citaParaVacunacion = citaBO.obtenerPorId(idCita2);

            Vacunacion vacunacion = new Vacunacion();

            vacunacion.setCita(citaParaVacunacion);
            vacunacion.setObservaciones("Vacunacion preventiva anual");
            vacunacion.setHoraInicio(LocalTime.of(11, 0));
            vacunacion.setHoraFin(LocalTime.of(11, 20));
            vacunacion.setPesoActual(25.5);
            vacunacion.setAlergias("Ninguna");
            vacunacion.setFechaAplicacion(Date.valueOf(LocalDate.now().plusDays(8)));
            vacunacion.setFechaProximaDosis(Date.valueOf(LocalDate.now().plusYears(1)));
            vacunacion.setDosis("1 dosis");

            Vacuna vacuna = new Vacuna();
            vacuna.setNombre("Antirrabica de prueba");
            vacuna.setDescripcion("Vacuna del catálogo de prueba");
            vacunacion.agregarVacuna(vacuna);
            System.out.println("\n========== TRANSACCION 2: VACUNACION CON VACUNAS ==========");
            vacunacionBO.insertarConVacunas(vacunacion);
            int idVacunacion = vacunacion.getIdAtencion();
            Vacunacion vacunacionObtenida = vacunacionBO.obtenerPorId(idVacunacion);
            if (idVacunacion <= 0 || vacunacionObtenida == null)
                throw new RuntimeException("TRANSACCION 2 FALLO: no se registro la vacunacion");

            int vacunasAsociadas = 0;
            for (Vacuna v : vacunacionObtenida.getVacunas()) {
                if (v.getIdVacuna() == vacuna.getIdVacuna())
                    vacunasAsociadas++;
            }
            if (vacuna.getIdVacuna() <= 0 || vacunasAsociadas != 1
                    || vacunacionObtenida.getVacunas().size() != vacunacion.getVacunas().size())
                throw new RuntimeException("TRANSACCION 2 FALLO: no se guardaron las vacunas asociadas");
            System.out.println("Vacunacion registrada: ID=" + idVacunacion);
            System.out.println("Vacuna del catalogo: ID=" + vacuna.getIdVacuna());
            System.out.println("Vacunas asociadas: " + vacunacionObtenida.getVacunas().size());
            System.out.println("TRANSACCION 2: EXITOSA");

            System.out.println("\n--- obtenerPorId() VACUNACION ---");
            System.out.println("ID Atencion heredado: " + vacunacionObtenida.getIdAtencion());
            System.out.println("Observaciones heredadas: " + vacunacionObtenida.getObservaciones());
            System.out.println("Peso heredado: " + vacunacionObtenida.getPesoActual());
            System.out.println("Fecha aplicacion: " + vacunacionObtenida.getFechaAplicacion());
            System.out.println("Fecha proxima dosis: " + vacunacionObtenida.getFechaProximaDosis());
            System.out.println("Dosis: " + vacunacionObtenida.getDosis());

            vacunacionObtenida.setDosis("1 dosis completa");
            vacunacionBO.modificar(vacunacionObtenida);
            vacunacionObtenida = vacunacionBO.obtenerPorId(idVacunacion);

            System.out.println("Dosis luego de modificar: "
                    + vacunacionObtenida.getDosis());

            System.out.println("\n--- listarTodos() VACUNACION ---");
            for (Vacunacion v : vacunacionBO.listarTodos()) {
                System.out.println(
                        "ID=" + v.getIdAtencion()
                                + " | Observaciones=" + v.getObservaciones()
                                + " | Fecha aplicacion=" + v.getFechaAplicacion()
                                + " | Dosis=" + v.getDosis()
                );
            }

            RecetaBOImpl recetaBO = new RecetaBOImpl();
            Receta receta = new Receta();
            receta.setAtencionMedica(consultaObtenida);
            receta.setFecha(Date.valueOf(LocalDate.now()));
            receta.setIndicaciones("Administrar medicamento después de las comidas");
            DetalleReceta detalle = new DetalleReceta();
            detalle.setMedicamento(medicamentoObtenido);
            detalle.setDosis("5 ml");
            detalle.setFrecuencia("Cada 12 horas");
            detalle.setDuracion("5 días");
            receta.agregarDetalleReceta(detalle);
            System.out.println("\n========== TRANSACCION 3: RECETA CON DETALLES ==========");
            recetaBO.insertarConDetalles(receta);
            if (receta.getIdReceta() <= 0 || recetaBO.obtenerPorId(receta.getIdReceta()) == null)
                throw new RuntimeException("TRANSACCION 3 FALLO: no se registro la receta");

            int detallesRegistrados = 0;
            for (DetalleReceta d : detalleBO.listarTodos()) {
                if (d.getReceta().getIdReceta() == receta.getIdReceta())
                    detallesRegistrados++;
            }
            if (detallesRegistrados != receta.getDetallesReceta().size())
                throw new RuntimeException("TRANSACCION 3 FALLO: faltan detalles de la receta");
            System.out.println("Receta registrada: ID=" + receta.getIdReceta());
            System.out.println("Detalles registrados: " + detallesRegistrados);
            System.out.println("TRANSACCION 3: EXITOSA");

            // La cita 3 debe confirmarse antes de registrar una atención de prueba.
            Cita citaParaRollback = citaBO.obtenerPorId(idCita3);
            citaParaRollback.setEstado(EstadoCita.CONFIRMADA);
            citaBO.modificar(citaParaRollback);

            Consulta consultaRollback = new Consulta();
            consultaRollback.setCita(citaBO.obtenerPorId(idCita3));
            consultaRollback.setHoraInicio(LocalTime.of(15, 30));
            consultaRollback.setHoraFin(LocalTime.of(16, 0));
            consultaRollback.setPesoActual(25);
            consultaRollback.setMotivoConsulta("Revisión de prueba de rollback");
            consultaRollback.setDiagnostico("Prueba");
            consultaRollback.setTratamiento("Prueba");
            consultaBO.insertar(consultaRollback);
            Receta recetaFallida = new Receta();
            recetaFallida.setAtencionMedica(consultaRollback);
            recetaFallida.setFecha(Date.valueOf(LocalDate.now()));
            recetaFallida.setIndicaciones("Prueba de rollback");
            DetalleReceta detalleValido = new DetalleReceta();
            detalleValido.setMedicamento(medicamentoObtenido);
            detalleValido.setDosis("1 dosis");
            detalleValido.setFrecuencia("Diaria");
            detalleValido.setDuracion("2 días");
            DetalleReceta detalleInvalido = new DetalleReceta();
            Medicamento inexistente = new Medicamento();
            inexistente.setIdMedicamento(999999999);
            detalleInvalido.setMedicamento(inexistente);
            detalleInvalido.setDosis("1 dosis");
            detalleInvalido.setFrecuencia("Diaria");
            detalleInvalido.setDuracion("2 días");
            recetaFallida.agregarDetalleReceta(detalleValido);
            recetaFallida.agregarDetalleReceta(detalleInvalido);
            System.out.println("\n========== PRUEBA DE ROLLBACK ==========");
            int recetasAntes = recetaBO.listarTodos().size();
            int detallesAntes = detalleBO.listarTodos().size();
            boolean rollbackProvocado = false;
            try {
                recetaBO.insertarConDetalles(recetaFallida);
            } catch (RuntimeException ex) {
                rollbackProvocado = true;
                System.out.println("Rollback provocado: " + ex.getMessage());
            }
            if (!rollbackProvocado)
                throw new RuntimeException("ERROR: la operación fallida no provocó rollback");
            int recetasDespues = recetaBO.listarTodos().size();
            int detallesDespues = detalleBO.listarTodos().size();
            if (recetasAntes != recetasDespues || detallesAntes != detallesDespues)
                throw new RuntimeException("ROLLBACK FALLO: quedaron registros parciales");
            if (recetaFallida.getIdReceta() > 0
                    && recetaBO.obtenerPorId(recetaFallida.getIdReceta()) != null)
                throw new RuntimeException("ROLLBACK FALLO: la receta fallida sigue registrada");
            System.out.println("Recetas antes/despues: " + recetasAntes + "/" + recetasDespues);
            System.out.println("Detalles antes/despues: " + detallesAntes + "/" + detallesDespues);
            System.out.println("ROLLBACK COMPROBADO: sin receta ni detalles parciales");

            System.out.println("\n====================================================");
            System.out.println("                 PRUEBA ELIMINAR");
            System.out.println("====================================================");

            consultaBO.eliminar(idConsulta);
            System.out.println("Consulta inactivada.");

            vacunacionBO.eliminar(idVacunacion);
            System.out.println("Vacunacion inactivada.");

            citaBO.eliminar(idCita1);
            citaBO.eliminar(idCita2);
            citaBO.eliminar(idCita3);
            System.out.println("3 citas inactivadas.");

            medicamentoBO.eliminar(idMedicamento1);
            medicamentoBO.eliminar(idMedicamento2);
            medicamentoBO.eliminar(idMedicamento3);
            System.out.println("3 medicamentos inactivados.");

            mascotaBO.eliminar(idMascota1);
            mascotaBO.eliminar(idMascota2);
            mascotaBO.eliminar(idMascota3);
            System.out.println("3 mascotas inactivadas.");

            Mascota mascotaInactiva = mascotaBO.obtenerPorId(idMascota1);
            System.out.println("Mascota inactiva excluida de la búsqueda: "
                    + (mascotaInactiva == null || !mascotaInactiva.isActivo()));

            clienteBO.eliminar(idCliente1);
            clienteBO.eliminar(idCliente2);
            clienteBO.eliminar(idCliente3);
            System.out.println("3 clientes inactivados.");

            Cliente clienteInactivo = clienteBO.obtenerPorId(idCliente1);
            System.out.println("Cliente inactivo excluido de la búsqueda: "
                    + (clienteInactivo == null || !clienteInactivo.isActivo()));

            recepcionistaBO.eliminar(idRecepcionista);
            veterinarioBO.eliminar(idVeterinario);

            System.out.println("\n====================================================");
            System.out.println("       TODAS LAS PRUEBAS TERMINARON CORRECTAMENTE");
            System.out.println("====================================================");

        } catch (Exception ex) {
            System.out.println("\nERROR DURANTE LAS PRUEBAS: " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException("MainTest no terminó correctamente", ex);
        }
    }

    private static Mascota crearMascota(Cliente cliente, String nombre,
                                        String fechaNacimiento, Especie especie,
                                        String raza, Sexo sexo) {
        Mascota mascota = new Mascota();
        mascota.setCliente(cliente);
        mascota.setNombre(nombre);
        mascota.setFechaNacimiento(Date.valueOf(fechaNacimiento));
        mascota.setEspecie(especie);
        mascota.setRaza(raza);
        mascota.setSexo(sexo);
        mascota.setActivo(true);
        return mascota;
    }

    private static Cita crearCita(Mascota mascota, Cliente cliente,
                                  Recepcionista recepcionista,
                                  Veterinario veterinario,
                                  String fecha, int hora, int minuto) {
        Cita cita = new Cita();
        cita.setTipoAtencion(TipoAtencion.CONSULTA);
        cita.setMascota(mascota);

        cita.setRecepcionista(recepcionista);
        cita.setVeterinario(veterinario);
        cita.setFecha(Date.valueOf(fecha));
        cita.setHora(LocalTime.of(hora, minuto));
        cita.setEstado(EstadoCita.RESERVADA);
        return cita;
    }

    private static Medicamento crearMedicamento(String nombre,
                                                String descripcion,
                                                double monto) {
        Medicamento medicamento = new Medicamento();
        medicamento.setNombre(nombre);
        medicamento.setDescripcion(descripcion);
        medicamento.setMonto(monto);
        medicamento.setFechaVencimiento(Date.valueOf(LocalDate.now().plusYears(2)));
        return medicamento;
    }
}
