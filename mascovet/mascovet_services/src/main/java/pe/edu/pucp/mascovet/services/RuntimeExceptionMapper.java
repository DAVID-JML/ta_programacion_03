package pe.edu.pucp.mascovet.services;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RuntimeExceptionMapper
        implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(RuntimeException ex) {

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(ex.getMessage())
                .type("text/plain")
                .build();
    }
}