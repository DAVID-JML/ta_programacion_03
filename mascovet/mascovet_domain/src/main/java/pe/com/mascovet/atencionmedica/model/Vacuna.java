package pe.com.mascovet.atencionmedica.model;

public class Vacuna {
    private int idVacuna;
    private Vacunacion vacunacion;
    private String nombre;
    private String descripcion;

    public Vacuna() {
    }

    public int getIdVacuna() {
        return idVacuna;
    }

    public void setIdVacuna(int idVacuna) {
        this.idVacuna = idVacuna;
    }

    public Vacunacion getVacunacion() {
        return vacunacion;
    }

    public void setVacunacion(Vacunacion vacunacion) {
        this.vacunacion = vacunacion;
    }

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
}
