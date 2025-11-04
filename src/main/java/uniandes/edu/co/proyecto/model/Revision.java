package uniandes.edu.co.proyecto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "REVISION")
public class Revision {

    public Revision() {;}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private int calificacion;

    @Column(nullable = false, length = 200)
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "id_servicio", referencedColumnName = "id")
    private Servicio id_servicio;

    //@ManyToOne
    //@JoinColumn(name = "id_usuarioConductorRevisado", referencedColumnName = "id", nullable = true)
    //private UsuarioConductor id_usuarioConductorRevisado;

    //@ManyToOne
    //@JoinColumn(name = "id_usuarioServicioRevisado", referencedColumnName = "id", nullable = true)
    //private UsuarioConductor id_usuarioServicioRevisado;

    public Revision(int calificacion, String comentario, Servicio id_servicio,
            UsuarioConductor id_usuarioConductorRevisado, UsuarioConductor id_usuarioServicioRevisado) {
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.id_servicio = id_servicio;
        //this.id_usuarioConductorRevisado = id_usuarioConductorRevisado;
        //this.id_usuarioServicioRevisado = id_usuarioServicioRevisado;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Servicio getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(Servicio id_servicio) {
        this.id_servicio = id_servicio;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    //public UsuarioConductor getId_usuarioConductorRevisado() {
    //    return id_usuarioConductorRevisado;
    //}

    //public void setId_usuarioConductorRevisado(UsuarioConductor id_usuarioConductorRevisado) {
    //    this.id_usuarioConductorRevisado = id_usuarioConductorRevisado;
    //}

    //public UsuarioConductor getId_usuarioServicioRevisado() {
    //    return id_usuarioServicioRevisado;
    //}
    
    //public void setId_usuarioServicioRevisado(UsuarioConductor id_usuarioServicioRevisado) {
    //    this.id_usuarioServicioRevisado = id_usuarioServicioRevisado;
    //}
}
