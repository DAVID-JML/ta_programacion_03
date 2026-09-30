package pe.com.mascovet.mascota.model;

import pe.com.mascovet.enums.model.Especie;
import pe.com.mascovet.enums.model.Sexo;
import pe.com.mascovet.usuario.model.Cliente;
import pe.com.mascovet.atencionmedica.model.AtencionMedica;
import pe.com.mascovet.cita.model.Cita;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Mascota {

    private int idMascota;
    private Cliente cliente;
    private String nombre;
    private Date fechaNacimiento;
    private Especie especie;
    private String raza;
    private Sexo sexo;
    private final List<Cita> citas;
    private final List<AtencionMedica> atencionesMedicas;
    private boolean isActivo;

    public Mascota() {
        this.isActivo = true;
        this.citas = new ArrayList<>();
        this.atencionesMedicas = new ArrayList<>();
    }

    public boolean isActivo() {
        return isActivo;
    }

    public void setActivo(boolean activo) {
        isActivo = activo;
    }

    public int getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public List<AtencionMedica> getAtencionesMedicas() {
        return atencionesMedicas;
    }

    public void agregarCita(Cita cita) {
        citas.add(cita);
    }

    public void agregarAtencionMedica(AtencionMedica atencionMedica) {
        atencionesMedicas.add(atencionMedica);
    }

}
