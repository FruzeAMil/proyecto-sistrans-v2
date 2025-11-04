package uniandes.edu.co.proyecto.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "SERVICIO")
public class Servicio {


    public Servicio() {;}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double distanciaKm;

    private Long idTarifa;

    private String tipoServicio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    private double costo;

    @ManyToOne
    @JoinColumn(name = "id_usuarioConductor", referencedColumnName = "id")
    private UsuarioConductor id_usuarioConductor;

    @ManyToOne
    @JoinColumn(name = "id_usuarioServicio", referencedColumnName = "id")
    private UsuarioServicio id_usuarioServicio;

    @ManyToOne
    @JoinColumn(name = "id_vehiculo", referencedColumnName = "id")
    private Vehiculo id_vehiculo;

    @OneToMany(mappedBy = "id_servicio")
    private List<Punto> puntos;

    @OneToMany(mappedBy = "id_servicio")
    private List<Revision> revisiones;

    @OneToMany(mappedBy = "id_servicio")
    private List<Viaje> viajes;

    public Servicio(String tipoServicio, LocalDate fecha, double costo, UsuarioServicio id_usuarioServicio,
            UsuarioConductor id_usuarioConductor, Vehiculo id_vehiculo) {
        this.tipoServicio = tipoServicio;
        this.fecha = fecha;
        this.costo = costo;
        this.id_usuarioServicio = id_usuarioServicio;
        this.id_usuarioConductor = id_usuarioConductor;
        this.id_vehiculo = id_vehiculo;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public UsuarioServicio getId_usuarioServicio() {
        return id_usuarioServicio;
    }

    public void setId_usuarioServicio(UsuarioServicio id_usuarioServicio) {
        this.id_usuarioServicio = id_usuarioServicio;
    }

    public UsuarioConductor getId_usuarioConductor() {
        return id_usuarioConductor;
    }

    public void setId_usuarioConductor(UsuarioConductor id_usuarioConductor) {
        this.id_usuarioConductor = id_usuarioConductor;
    }

    public Vehiculo getId_vehiculo() {
        return id_vehiculo;
    }

    public void setId_vehiculo(Vehiculo id_vehiculo) {
        this.id_vehiculo = id_vehiculo;
    }

    public List<Punto> getPuntos() {
        return puntos;
    }

    public void setPuntos(List<Punto> puntos) {
        this.puntos = puntos;
    }

    public List<Revision> getRevisiones() {
        return revisiones;
    }

    public void setRevisiones(List<Revision> revisiones) {
        this.revisiones = revisiones;
    }

    public List<Viaje> getViajes() {
        return viajes;
    }

    public void setViajes(List<Viaje> viajes) {
        this.viajes = viajes;
    }

    public Double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(Double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public Long getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(Long idTarifa) {
        this.idTarifa = idTarifa;
    }
}
