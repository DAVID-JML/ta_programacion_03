package pe.com.mascovet.cita.model;

import pe.com.mascovet.atencionmedica.model.AtencionMedica;
import pe.com.mascovet.enums.model.EstadoCita;
import pe.com.mascovet.enums.model.TipoAtencion;
import pe.com.mascovet.mascota.model.Mascota;
import pe.com.mascovet.usuario.model.Cliente;
import pe.com.mascovet.usuario.model.Recepcionista;
import pe.com.mascovet.usuario.model.Veterinario;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Cita {
    private int idCita;
    private Mascota mascota;
    private Date fecha;
    private LocalTime hora;
    private EstadoCita estado;
    private TipoAtencion tipoAtencion;
    private String motivoCancelacion;
    private boolean activo = true;
    private Recepcionista recepcionista;
    private Veterinario veterinario;
    private final List<AtencionMedica> atencionesMedicas;

    public Cita() {
        estado = EstadoCita.RESERVADA;
        atencionesMedicas = new ArrayList<>();
    }

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return mascota == null ? null : mascota.getCliente();
    }

    public TipoAtencion getTipoAtencion() {
        return tipoAtencion;
    }

    public void setTipoAtencion(TipoAtencion tipoAtencion) {
        this.tipoAtencion = tipoAtencion;
    }

    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }

    public void setMotivoCancelacion(String motivoCancelacion) {
        this.motivoCancelacion = motivoCancelacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Recepcionista getRecepcionista() {
        return recepcionista;
    }

    public void setRecepcionista(Recepcionista recepcionista) {
        this.recepcionista = recepcionista;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public List<AtencionMedica> getAtencionesMedicas() {
        return atencionesMedicas;
    }

    public void confirmar() {
        estado = EstadoCita.CONFIRMADA;
    }

    public void cancelar() {
        estado = EstadoCita.CANCELADA;
    }

    public void agregarAtencionMedica(AtencionMedica atencionMedica) {
        atencionesMedicas.add(atencionMedica);
    }
}
