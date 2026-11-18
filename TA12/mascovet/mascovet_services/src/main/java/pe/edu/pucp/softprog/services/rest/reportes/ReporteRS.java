package pe.edu.pucp.softprog.services.rest.reportes;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;

import pe.com.mascovet.config.DBManager;

import java.sql.Connection;
import java.sql.Date;
import java.util.HashMap;

@Path("ReporteRS")
public class ReporteRS {

    @Inject
    private ServletContext servletContext;

     
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

            String ruta = servletContext.getRealPath(
                    "/WEB-INF/reportes/ReporteCitasTipoAtencion.jasper");

            JasperReport reporte = (JasperReport)
                    JRLoader.loadObjectFromFile(ruta);

            HashMap parametros = new HashMap();
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
            ex.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
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

            String ruta = servletContext.getRealPath(
                    "/WEB-INF/reportes/ReporteCitasEspecialidad.jasper");
            String rutaReporteGrafico = servletContext.getRealPath(
                    "/WEB-INF/reportes/ReporteGraficoEspecialidades.jasper");

            JasperReport reporte = (JasperReport)
                    JRLoader.loadObjectFromFile(ruta);

            HashMap parametros = new HashMap();
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
            ex.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
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

            String ruta = servletContext.getRealPath(
                    "/WEB-INF/reportes/ReporteHistorialClinico.jasper");

            JasperReport reporte = (JasperReport)
                    JRLoader.loadObjectFromFile(ruta);

            HashMap parametros = new HashMap();
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
            ex.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
