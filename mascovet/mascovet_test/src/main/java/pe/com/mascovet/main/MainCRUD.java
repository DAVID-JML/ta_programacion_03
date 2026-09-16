package pe.com.mascovet.main;


import pe.com.mascovet.atencionmedica.model.*;
import pe.com.mascovet.cita.model.Cita;
import pe.com.mascovet.cita.model.HorarioAtencion;
import pe.com.mascovet.enums.model.*;
import pe.com.mascovet.receta.model.DetalleReceta;
import pe.com.mascovet.receta.model.Medicamento;
import pe.com.mascovet.receta.model.Receta;
import pe.com.mascovet.usuario.model.Cliente;
import pe.com.mascovet.usuario.model.Recepcionista;
import pe.com.mascovet.usuario.model.Administrador;
import pe.com.mascovet.usuario.model.Veterinario;
import pe.com.mascovet.mascota.model.Mascota;

import java.time.LocalTime;
import java.util.Date;


public class MainCRUD {
    public static void main(String[] args) {
        Cliente cliente = new Cliente(
                1,
                "74851236",
                "Carlos",
                "Ramirez",
                "cramirez",
                "clave123",
                true,
                "carlos@correo.com",
                "987654321");

        Veterinario veterinario = new Veterinario();
        veterinario.setIdUsuario(2);
        veterinario.setDni("45678912");
        veterinario.setNombre("Andrea");
        veterinario.setApellido("Salazar");
        veterinario.setNombreUsuario("asalazar");
        veterinario.setContrasena("vet123");
        veterinario.setActivo(true);
        veterinario.setNumeroColegiatura("CMVP-4587");
        veterinario.setEspecialidad(Especialidad.MEDICINA_GENERAL);

        Recepcionista recepcionista = new Recepcionista(
                3,
                "70981234",
                "Maria",
                "Lopez",
                "mlopez",
                "recep123",
                true,
                "MAÑANA");

        Administrador administrador = new Administrador(
                4,
                "70112233",
                "Luis",
                "Torres",
                "ltorres",
                "admin123",
                true,
                "Administrador general");

        Mascota mascota = new Mascota();
        mascota.setIdMascota(10);
        mascota.setNombre("Luna");
        mascota.setFechaNacimiento(new Date());
        mascota.setEspecie(Especie.PERRO);
        mascota.setRaza("Labrador");
        mascota.setSexo(Sexo.HEMBRA);
        mascota.setCliente(cliente);
        cliente.agregarMascota(mascota);

        HorarioAtencion horario = new HorarioAtencion();
        horario.setIdHorario(100);
        horario.setDia(Dia.LUNES);
        horario.setHoraInicio(LocalTime.of(9, 0));
        horario.setHoraFin(LocalTime.of(13, 0));
        horario.setVeterinario(veterinario);
        veterinario.agregarHorarioAtencion(horario);

        Cita cita1 = new Cita();
        cita1.setIdCita(200);
        cita1.setFecha(new Date());
        cita1.setHora(LocalTime.of(10, 30));
        cita1.setEstado(EstadoCita.CONFIRMADA);
        cita1.setCliente(cliente);
        cita1.setMascota(mascota);
        cita1.setRecepcionista(recepcionista);
        cita1.setVeterinario(veterinario);

        cliente.agregarCita(cita1);
        mascota.agregarCita(cita1);
        recepcionista.agregarCita(cita1);
        veterinario.agregarCita(cita1);

        Cita cita2 = new Cita();
        cita2.setIdCita(201);
        cita2.setFecha(new Date());
        cita2.setHora(LocalTime.of(12, 0));
        cita2.setEstado(EstadoCita.RESERVADA);
        cita2.setCliente(cliente);
        cita2.setMascota(mascota);
        cita2.setRecepcionista(recepcionista);
        cita2.setVeterinario(veterinario);

        cliente.agregarCita(cita2);
        mascota.agregarCita(cita2);
        recepcionista.agregarCita(cita2);
        veterinario.agregarCita(cita2);


        Consulta consulta = new Consulta(
                300,
                "Control en siete dias",
                LocalTime.of(10, 30),
                LocalTime.of(11, 0),
                mascota,
                cita1,
                "Falta de apetito",
                "Gastritis leve",
                "Dieta blanda");
        mascota.agregarAtencionMedica(consulta);
        cita1.agregarAtencionMedica(consulta);


        Control control = new Control();
        control.setIdAtencion(301);
        control.setObservaciones("Paciente estable");
        control.setHoraInicio(LocalTime.of(11, 0));
        control.setHoraFin(LocalTime.of(11, 20));
        control.setMascota(mascota);
        control.setCita(cita1);
        control.setEvolucion("Evolucion favorable");
        control.setIndicaciones("Continuar tratamiento");
        mascota.agregarAtencionMedica(control);
        cita1.agregarAtencionMedica(control);

        Cirugia cirugia = new Cirugia(
                302,
                "Sin complicaciones",
                LocalTime.of(8, 0),
                LocalTime.of(9, 30),
                mascota,
                cita1,
                "Esterilizacion",
                "Reposo y control");
        mascota.agregarAtencionMedica(cirugia);
        cita1.agregarAtencionMedica(cirugia);

        Vacunacion vacunacion = new Vacunacion(
                303,
                "Vacunación anual",
                LocalTime.of(9, 30),
                LocalTime.of(9, 45),
                mascota,
                cita1,
                new Date(),
                new Date(),
                "1 ml");
        mascota.agregarAtencionMedica(vacunacion);
        cita1.agregarAtencionMedica(vacunacion);

        Vacuna vacuna = new Vacuna();
        vacuna.setIdVacuna(400);
        vacuna.setNombre("Antirrabica");
        vacuna.setDescripcion("Vacuna preventiva contra la rabia");
        vacuna.setVacunacion(vacunacion);
        vacunacion.agregarVacuna(vacuna);

        Receta receta = new Receta();
        receta.setIdReceta(500);
        receta.setFecha(new Date());
        receta.setIndicaciones("Administrar despues de los alimentos");
        receta.setAtencionMedica(consulta);
        consulta.setReceta(receta);

        DetalleReceta detalleReceta = new DetalleReceta();
        detalleReceta.setIdDetalleReceta(600);
        detalleReceta.setDosis("5 ml");
        detalleReceta.setFrecuencia("Cada 12 horas");
        detalleReceta.setDuracion("5 dias");
        detalleReceta.setMontoTotal(18.50);
        detalleReceta.setReceta(receta);
        receta.agregarDetalleReceta(detalleReceta);

        Medicamento medicamento = new Medicamento();
        medicamento.setIdMedicamento(700);
        medicamento.setNombre("Protector gastrico");
        medicamento.setDescripcion("Medicamento veterinario de uso oral");
        medicamento.setMonto(18.50);
        medicamento.setDetalleReceta(detalleReceta);
        detalleReceta.agregarMedicamento(medicamento);

        System.out.println("===============================================");
        System.out.println("           PRUEBA DE DOMINIO - MASCOVET");
        System.out.println("===============================================");

        System.out.println("\nCLIENTE");
        System.out.println("Nombre: " + cliente.getNombreCompleto());
        System.out.println("Correo: " + cliente.getCorreo());
        System.out.println("Mascotas registradas: " + cliente.getMascotas().size());
        System.out.println("Citas registradas: " + cliente.getCitas().size());

        System.out.println("\nMASCOTA");
        System.out.println("Nombre: " + mascota.getNombre());
        System.out.println("Especie: " + mascota.getEspecie());
        System.out.println("Dueno: " + mascota.getCliente().getNombreCompleto());
        System.out.println("Cantidad de citas: " + mascota.getCitas().size());
        System.out.println("Atenciones medicas: " + mascota.getAtencionesMedicas().size());

        System.out.println("\nVETERINARIO");
        System.out.println("Nombre: " + veterinario.getNombreCompleto());
        System.out.println("Colegiatura: " + veterinario.getNumeroColegiatura());
        System.out.println("Especialidad: " + veterinario.getEspecialidad());
        System.out.println("Horarios registrados: " + veterinario.getHorariosAtencion().size());
        System.out.println("Cantidad de citas: " + veterinario.getCitas().size());

        System.out.println("\nCITAS");
        for (Cita cita : mascota.getCitas()) {
            System.out.println("Cita " + cita.getIdCita()
                    + " - " + cita.getEstado()
                    + " - " + cita.getHora()
                    + " - Veterinario: " + cita.getVeterinario().getNombreCompleto());
        }

        System.out.println("\nATENCION MEDICA");
        System.out.println("Consulta: " + consulta.getMotivoConsulta());
        System.out.println("Diagnostico: " + consulta.getDiagnostico());
        System.out.println("Control: " + control.getEvolucion());
        System.out.println("Cirugia: " + cirugia.getProcedimiento());
        System.out.println("Vacunas aplicadas: " + vacunacion.getVacunas().size());

        System.out.println("\nRECETA");
        System.out.println("Indicaciones: " + receta.getIndicaciones());
        System.out.println("Detalles: " + receta.getDetallesReceta().size());
        System.out.println("Medicamento: " + detalleReceta.getMedicamentos().get(0).getNombre());
        
    }
}
