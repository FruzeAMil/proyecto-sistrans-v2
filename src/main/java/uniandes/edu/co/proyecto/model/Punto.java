package uniandes.edu.co.proyecto.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "PUNTO")
public class Punto {

    public Punto() {;}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String direccion;

    private Double longitud;

    private Double latitud;

    private Integer orden;

    @ManyToOne
    @JoinColumn(name = "id_ervicio", referencedColumnName = "id")
    private Servicio id_servicio;

    @ManyToOne
    @JoinColumn(name = "id_ciudad", referencedColumnName = "id")
    private Ciudad id_ciudad;

    public Punto(String direccion, Double longitud, Double latitud, Integer orden, Servicio id_servicio,
            Ciudad id_ciudad) {
        this.direccion = direccion;
        this.longitud = longitud;
        this.latitud = latitud;
        this.orden = orden;
        this.id_servicio = id_servicio;
        this.id_ciudad = id_ciudad;

    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Ciudad getCiudad() {
        return id_ciudad;
    }

    public void setCiudad(Ciudad id_ciudad) {
        this.id_ciudad = id_ciudad;
    }

    public Servicio getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(Servicio id_servicio) {
        this.id_servicio = id_servicio;
    }

    public Ciudad getId_ciudad() {
        return id_ciudad;
    }

    public void setId_ciudad(Ciudad id_ciudad) {
        this.id_ciudad = id_ciudad;
    }

}
