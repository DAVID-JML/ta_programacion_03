package pe.com.mascovet.main;

import pe.com.mascovet.atencionmedica.model.*;
import pe.com.mascovet.enums.model.*;

import pe.com.mascovet.usuario.bo.AdministradorBOImpl;
import pe.com.mascovet.usuario.bo.ClienteBOImpl;
import pe.com.mascovet.usuario.bo.RecepcionistaBOImpl;
import pe.com.mascovet.usuario.bo.VeterinarioBOImpl;
import pe.com.mascovet.usuario.boi.IAdministradorBO;
import pe.com.mascovet.usuario.boi.IClienteBO;
import pe.com.mascovet.usuario.boi.IRecepcionistaBO;
import pe.com.mascovet.usuario.boi.IVeterinarioBO;
import pe.com.mascovet.usuario.model.Cliente;
import pe.com.mascovet.usuario.model.Recepcionista;
import pe.com.mascovet.usuario.model.Administrador;
import pe.com.mascovet.usuario.model.Veterinario;

import java.util.List;


public class MainTest {
    public static void main(String[] args) {
        String sufijo = String.format("%05d", System.currentTimeMillis() % 100000);

        System.out.println("====================================================");
        System.out.println("      PRUEBA CRUD A TRAVES DE BUSINESS LOGIC");
        System.out.println("====================================================");
        System.out.println("Se prueban 4 entidades: Cliente, Veterinario, Recepcionista y Administrador.");
        System.out.println("Los 4 usuarios usan la misma contraseña para evidenciar que contrasena NO es UNIQUE.");

        try {
            probarCliente(sufijo);
            probarVeterinario(sufijo);
            probarRecepcionista(sufijo);
            probarAdministrador(sufijo);

            System.out.println("\n====================================================");
            System.out.println("LAS 4 PRUEBAS CRUD FINALIZARON CORRECTAMENTE");
            System.out.println("====================================================");
        } catch (RuntimeException ex) {
            System.out.println("\nERROR DURANTE LAS PRUEBAS CRUD: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static void probarCliente(String sufijo) {
        System.out.println("\n---------------- CLIENTE ----------------");
        IClienteBO clienteBO = new ClienteBOImpl();
        Cliente cliente = new Cliente(0, "7000" + sufijo, "Carlos", "Ramirez",
                "cliente_" + sufijo, "clave123", true,
                "cliente" + sufijo + "@correo.com", "98765" + sufijo);

        int id = clienteBO.insertar(cliente);
        System.out.println("Insertado. ID: " + id);
        Cliente encontrado = clienteBO.obtenerPorId(id);
        System.out.println("ObtenerPorId: " + encontrado.getNombreCompleto() + " - " + encontrado.getCorreo());

        encontrado.setTelefono("99999" + sufijo);
        int modificados = clienteBO.modificar(encontrado);
        System.out.println("Modificar. Resultado: " + modificados);
        Cliente clienteModificado = clienteBO.obtenerPorId(id);
        System.out.println("Verificacion modificacion. Telefono: " + clienteModificado.getTelefono());

        System.out.println("ListarTodos:");
        List<Cliente> clientes = clienteBO.listarTodos();
        for (Cliente item : clientes) {
            System.out.println("  ID: " + item.getIdUsuario()
                    + " | DNI: " + item.getDni()
                    + " | Nombre: " + item.getNombreCompleto()
                    + " | Correo: " + item.getCorreo()
                    + " | Telefono: " + item.getTelefono());
        }
        System.out.println("Cantidad total: " + clientes.size());

        int eliminados = clienteBO.eliminar(id);
        System.out.println("Eliminar. Resultado: " + eliminados);
        Cliente clienteEliminado = clienteBO.obtenerPorId(id);
        System.out.println("Verificacion baja logica. Activo: " + clienteEliminado.isActivo());
    }

    private static void probarVeterinario(String sufijo) {
        System.out.println("\n-------------- VETERINARIO --------------");
        IVeterinarioBO veterinarioBO = new VeterinarioBOImpl();
        Veterinario veterinario = new Veterinario(0, "7001" + sufijo, "Andrea", "Salazar",
                "veterinario_" + sufijo, "clave123", true,
                "CMVP-" + sufijo, Especialidad.MEDICINA_GENERAL);

        int id = veterinarioBO.insertar(veterinario);
        System.out.println("Insertado. ID: " + id);
        Veterinario encontrado = veterinarioBO.obtenerPorId(id);
        System.out.println("ObtenerPorId: " + encontrado.getNombreCompleto() + " - " + encontrado.getEspecialidad());

        encontrado.setEspecialidad(Especialidad.CIRUGIA);
        int modificados = veterinarioBO.modificar(encontrado);
        System.out.println("Modificar. Resultado: " + modificados);
        Veterinario veterinarioModificado = veterinarioBO.obtenerPorId(id);
        System.out.println("Verificacion modificacion. Especialidad: " + veterinarioModificado.getEspecialidad());

        System.out.println("ListarTodos:");
        List<Veterinario> veterinarios = veterinarioBO.listarTodos();
        for (Veterinario item : veterinarios) {
            System.out.println("  ID: " + item.getIdUsuario()
                    + " | DNI: " + item.getDni()
                    + " | Nombre: " + item.getNombreCompleto()
                    + " | Colegiatura: " + item.getNumeroColegiatura()
                    + " | Especialidad: " + item.getEspecialidad());
        }
        System.out.println("Cantidad total: " + veterinarios.size());

        int eliminados = veterinarioBO.eliminar(id);
        System.out.println("Eliminar. Resultado: " + eliminados);
        Veterinario veterinarioEliminado = veterinarioBO.obtenerPorId(id);
        System.out.println("Verificacion baja logica. Activo: " + veterinarioEliminado.isActivo());
    }

    private static void probarRecepcionista(String sufijo) {
        System.out.println("\n------------- RECEPCIONISTA -------------");
        IRecepcionistaBO recepcionistaBO = new RecepcionistaBOImpl();
        Recepcionista recepcionista = new Recepcionista(0, "7002" + sufijo, "Maria", "Lopez",
                "recepcionista_" + sufijo, "clave123", true, "MANANA");

        int id = recepcionistaBO.insertar(recepcionista);
        System.out.println("Insertado. ID: " + id);
        Recepcionista encontrado = recepcionistaBO.obtenerPorId(id);
        System.out.println("ObtenerPorId: " + encontrado.getNombreCompleto() + " - turno " + encontrado.getTurno());

        encontrado.setTurno("TARDE");
        int modificados = recepcionistaBO.modificar(encontrado);
        System.out.println("Modificar. Resultado: " + modificados);
        Recepcionista recepcionistaModificado = recepcionistaBO.obtenerPorId(id);
        System.out.println("Verificacion modificacion. Turno: " + recepcionistaModificado.getTurno());

        System.out.println("ListarTodos:");
        List<Recepcionista> recepcionistas = recepcionistaBO.listarTodos();
        for (Recepcionista item : recepcionistas) {
            System.out.println("  ID: " + item.getIdUsuario()
                    + " | DNI: " + item.getDni()
                    + " | Nombre: " + item.getNombreCompleto()
                    + " | Turno: " + item.getTurno());
        }
        System.out.println("Cantidad total: " + recepcionistas.size());

        int eliminados = recepcionistaBO.eliminar(id);
        System.out.println("Eliminar. Resultado: " + eliminados);
        Recepcionista recepcionistaEliminado = recepcionistaBO.obtenerPorId(id);
        System.out.println("Verificacion baja logica. Activo: " + recepcionistaEliminado.isActivo());
    }

    private static void probarAdministrador(String sufijo) {
        System.out.println("\n------------- ADMINISTRADOR -------------");
        IAdministradorBO administradorBO = new AdministradorBOImpl();
        Administrador administrador = new Administrador(0, "7003" + sufijo, "Lucia", "Torres",
                "administrador_" + sufijo, "clave123", true, "SUPERVISOR");

        int id = administradorBO.insertar(administrador);
        System.out.println("Insertado. ID: " + id);
        Administrador encontrado = administradorBO.obtenerPorId(id);
        System.out.println("ObtenerPorId: " + encontrado.getNombreCompleto() + " - cargo " + encontrado.getCargo());

        encontrado.setCargo("ADMINISTRADOR GENERAL");
        int modificados = administradorBO.modificar(encontrado);
        System.out.println("Modificar. Resultado: " + modificados);
        Administrador administradorModificado = administradorBO.obtenerPorId(id);
        System.out.println("Verificacion modificacion. Cargo: " + administradorModificado.getCargo());

        System.out.println("ListarTodos:");
        List<Administrador> administradores = administradorBO.listarTodos();
        for (Administrador item : administradores) {
            System.out.println("  ID: " + item.getIdUsuario()
                    + " | DNI: " + item.getDni()
                    + " | Nombre: " + item.getNombreCompleto()
                    + " | Cargo: " + item.getCargo());
        }
        System.out.println("Cantidad total: " + administradores.size());

        int eliminados = administradorBO.eliminar(id);
        System.out.println("Eliminar. Resultado: " + eliminados);
        Administrador administradorEliminado = administradorBO.obtenerPorId(id);
        System.out.println("Verificacion baja logica. Activo: " + administradorEliminado.isActivo());
    }
}
