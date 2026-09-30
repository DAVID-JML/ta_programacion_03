package pe.com.mascovet.receta.model;

public class Medicamento {
    private int idMedicamento;
    //private DetalleReceta detalleReceta;
    private String nombre;
    private String descripcion;
    private double monto;

    public Medicamento() {
    }

    public int getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(int idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

//    public DetalleReceta getDetalleReceta() {
//        return detalleReceta;
//    }
//
//    public void setDetalleReceta(DetalleReceta detalleReceta) {
//        this.detalleReceta = detalleReceta;
//    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}
