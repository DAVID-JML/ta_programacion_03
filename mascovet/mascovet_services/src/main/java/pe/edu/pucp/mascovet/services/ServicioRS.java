package pe.edu.pucp.mascovet.services;
import jakarta.ws.rs. Produces;
import jakarta.ws.rs. Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs. Path;
import jakarta.ws.rs. QueryParam;

@Path("ServicioRS")
public class ServicioRS {

    @GET
    @Produces("text/html")
    public String getHtml(@QueryParam("nombre") String nombre) {
        if (nombre == null || nombre.isBlank()) {
            nombre = "Invitado";
        }
            return "<html lang=\"es\">" +
                    "<head><title>Pagina de Saludo</title></head>" +
                    "<body><h1>Hola " + nombre + " !< /h1></body>" +
                    "</html>";


    }
}
