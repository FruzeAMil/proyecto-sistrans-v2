package uniandes.edu.co.proyecto.dto;

public class SolicitudServicioDTO {
    
    private Long idUsuarioServicio;
    private String direccionPartida;
    private Double longitudPartida;
    private Double latitudPartida;
    private String direccionLlegada;
    private Double longitudLlegada;
    private Double latitudLlegada;
    private Long idCiudad;
    private String tipoServicio;
    private Long idTarifa;
    
    public SolicitudServicioDTO() {}
    
    public SolicitudServicioDTO(Long idUsuarioServicio, String direccionPartida, Double longitudPartida,
                                Double latitudPartida, String direccionLlegada, Double longitudLlegada,
                                Double latitudLlegada, Long idCiudad, String tipoServicio) {
        this.idUsuarioServicio = idUsuarioServicio;
        this.direccionPartida = direccionPartida;
        this.longitudPartida = longitudPartida;
        this.latitudPartida = latitudPartida;
        this.direccionLlegada = direccionLlegada;
        this.longitudLlegada = longitudLlegada;
        this.latitudLlegada = latitudLlegada;
        this.idCiudad = idCiudad;
        this.tipoServicio = tipoServicio;
    }
    
    public Long getIdUsuarioServicio() { return idUsuarioServicio; }
    public void setIdUsuarioServicio(Long idUsuarioServicio) { this.idUsuarioServicio = idUsuarioServicio; }
    
    public String getDireccionPartida() { return direccionPartida; }
    public void setDireccionPartida(String direccionPartida) { this.direccionPartida = direccionPartida; }
    
    public Double getLongitudPartida() { return longitudPartida; }
    public void setLongitudPartida(Double longitudPartida) { this.longitudPartida = longitudPartida; }
    
    public Double getLatitudPartida() { return latitudPartida; }
    public void setLatitudPartida(Double latitudPartida) { this.latitudPartida = latitudPartida; }
    
    public String getDireccionLlegada() { return direccionLlegada; }
    public void setDireccionLlegada(String direccionLlegada) { this.direccionLlegada = direccionLlegada; }
    
    public Double getLongitudLlegada() { return longitudLlegada; }
    public void setLongitudLlegada(Double longitudLlegada) { this.longitudLlegada = longitudLlegada; }
    
    public Double getLatitudLlegada() { return latitudLlegada; }
    public void setLatitudLlegada(Double latitudLlegada) { this.latitudLlegada = latitudLlegada; }
    
    public Long getIdCiudad() { return idCiudad; }
    public void setIdCiudad(Long idCiudad) { this.idCiudad = idCiudad; }
    
    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }

    public Long getIdTarifa() { return idTarifa; }
    public void setIdTarifa(Long idTarifa) { this.idTarifa = idTarifa; }
}