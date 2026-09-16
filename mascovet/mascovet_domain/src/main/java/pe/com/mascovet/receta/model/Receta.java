package pe.com.mascovet.receta.model;

import pe.com.mascovet.atencionmedica.model.AtencionMedica;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Receta {
    private int idReceta;
    private AtencionMedica atencionMedica;
    private Date fecha;
    private String indicaciones;
    private final List<DetalleReceta> detallesReceta;

    public Receta() {
        detallesReceta = new ArrayList<>();
    }

    public int getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(int idReceta) {
        this.idReceta = idReceta;
    }

    public AtencionMedica getAtencionMedica() {
        return atencionMedica;
    }

    public void setAtencionMedica(AtencionMedica atencionMedica) {
        this.atencionMedica = atencionMedica;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public List<DetalleReceta> getDetallesReceta() {
        return detallesReceta;
    }

    public void agregarDetalleReceta(DetalleReceta detalleReceta) {
        detallesReceta.add(detalleReceta);
    }
}
