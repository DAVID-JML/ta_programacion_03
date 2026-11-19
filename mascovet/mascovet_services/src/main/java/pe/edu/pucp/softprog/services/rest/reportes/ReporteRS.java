package pe.edu.pucp.softprog.services.rest.reportes;

import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;

import pe.com.mascovet.config.DBManager;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("ReporteRS")
public class ReporteRS {

    private static final Map<String, JasperReport> CACHE_REPORTES = new ConcurrentHashMap<>();

    @Context
    private ServletContext servletContext;

    private JasperReport obtenerReporte(String nombreBase) throws JRException {
        JasperReport enCache = CACHE_REPORTES.get(nombreBase);
        if (enCache != null) {
            return enCache;
        }

        String rutaJasper = servletContext.getRealPath("/WEB-INF/reportes/" + nombreBase + ".jasper");
        String rutaJrxml = servletContext.getRealPath("/WEB-INF/reportes/" + nombreBase + ".jrxml");

        JasperReport reporte;
        if (rutaJasper != null && new File(rutaJasper).exists()) {
            reporte = (JasperReport) JRLoader.loadObjectFromFile(rutaJasper);
        } else if (rutaJrxml != null && new File(rutaJrxml).exists()) {
            reporte = JasperCompileManager.compileReport(rutaJrxml);
        } else {
            throw new JRException("No se encontró el archivo de reporte: " + nombreBase);
        }

        CACHE_REPORTES.put(nombreBase, reporte);
        return reporte;
    }

    private Response respuestaError500(Exception ex) {
        ex.printStackTrace();
        Throwable causa = ex;
        while (causa.getCause() != null) {
            causa = causa.getCause();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type("text/plain; charset=UTF-8")
                .entity(causa.getClass().getName() + ": " + causa.getMessage())
                .build();
    }

    @GET
    @Path("citasPorTipoAtencion")
    @Produces("application/pdf")
    public Response generarReporteCitasPorTipo(@QueryParam("fechaInicio") String fechaInicio,
                                               @QueryParam("fechaFin") String fechaFin) {
        try {
            Date inicio = Date.valueOf(fechaInicio);
            Date fin = Date.valueOf(fechaFin);
            if (inicio.after(fin)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            JasperReport reporte = obtenerReporte("ReporteCitasTipoAtencion");

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("fechaInicio", inicio);
            parametros.put("fechaFin", fin);

            try (Connection con = DBManager.getInstance().getConnection()) {
                JasperPrint jp = JasperFillManager.fillReport(reporte, parametros, con);
                byte[] pdf = JasperExportManager.exportReportToPdf(jp);

                return Response.ok(pdf, "application/pdf")
                        .header("Content-Disposition", "attachment; filename=ReporteCitasTipoAtencion.pdf")
                        .build();
            }
        } catch (IllegalArgumentException | NullPointerException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception ex) {
            return respuestaError500(ex);
        }
    }

    @GET
    @Path("citasPorEspecialidad")
    @Produces("application/pdf")
    public Response generarReporteCitasPorEspecialidad(@QueryParam("fechaInicio") String fechaInicio,
                                                       @QueryParam("fechaFin") String fechaFin) {
        try {
            Date inicio = Date.valueOf(fechaInicio);
            Date fin = Date.valueOf(fechaFin);
            if (inicio.after(fin)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            JasperReport reporte = obtenerReporte("ReporteCitasEspecialidad");
            // Precarga/compila también el subreporte si solo estaba el .jrxml
            obtenerReporte("ReporteGraficoEspecialidades");

            String rutaReporteGrafico = servletContext.getRealPath(
                    "/WEB-INF/reportes/ReporteGraficoEspecialidades.jasper");
            if (rutaReporteGrafico == null) {
                File dirReportes = new File(servletContext.getRealPath("/WEB-INF/reportes"));
                rutaReporteGrafico = new File(dirReportes, "ReporteGraficoEspecialidades.jasper").getAbsolutePath();
            }

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("fechaInicio", inicio);
            parametros.put("fechaFin", fin);
            parametros.put("rutaSubreporteGrafico", rutaReporteGrafico);

            try (Connection con = DBManager.getInstance().getConnection()) {
                JasperPrint jp = JasperFillManager.fillReport(reporte, parametros, con);
                byte[] pdf = JasperExportManager.exportReportToPdf(jp);

                return Response.ok(pdf, "application/pdf")
                        .header("Content-Disposition", "attachment; filename=ReporteCitasEspecialidad.pdf")
                        .build();
            }
        } catch (IllegalArgumentException | NullPointerException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception ex) {
            return respuestaError500(ex);
        }
    }

    @GET
    @Path("historialClinico")
    @Produces("application/pdf")
    public Response generarReporteHistorialClinico(@QueryParam("idMascota") Integer idMascota,
                                                   @QueryParam("fechaInicio") String fechaInicio,
                                                   @QueryParam("fechaFin") String fechaFin) {
        try {
            if (idMascota == null || idMascota <= 0) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            Date inicio = fechaInicio == null || fechaInicio.isBlank() ? null : Date.valueOf(fechaInicio);
            Date fin = fechaFin == null || fechaFin.isBlank() ? null : Date.valueOf(fechaFin);
            if (inicio != null && fin != null && inicio.after(fin)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            JasperReport reporte = obtenerReporte("ReporteHistorialClinico");

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("idMascota", idMascota);
            parametros.put("fechaInicio", inicio);
            parametros.put("fechaFin", fin);

            try (Connection con = DBManager.getInstance().getConnection()) {
                JasperPrint jp = JasperFillManager.fillReport(reporte, parametros, con);
                byte[] pdf = JasperExportManager.exportReportToPdf(jp);

                return Response.ok(pdf, "application/pdf")
                        .header("Content-Disposition", "attachment; filename=ReporteHistorialClinico.pdf")
                        .build();
            }
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception ex) {
            return respuestaError500(ex);
        }
    }
}
