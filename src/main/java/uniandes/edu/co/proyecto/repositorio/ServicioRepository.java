package uniandes.edu.co.proyecto.repositorio;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import uniandes.edu.co.proyecto.model.Servicio;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    @Query(value = "SELECT * FROM SERVICIO", nativeQuery = true)
    Collection<Servicio> darServicios();

    @Query(value = "SELECT * FROM SERVICIO WHERE id = :id", nativeQuery = true)
    Servicio darServicio(@Param("id") Long id);

    @Query(value = "SELECT * FROM SERVICIO WHERE id_usuarioServicio = :idUsuarioServicio", nativeQuery = true)
    Collection<Servicio> darServiciosPorUsuarioServicio(@Param("idUsuarioServicio") Long idUsuarioServicio);

    @Query(value = "SELECT * FROM SERVICIO WHERE id_usuarioConductor = :idUsuarioConductor", nativeQuery = true)
    Collection<Servicio> darServiciosPorUsuarioConductor(@Param("idUsuarioConductor") Long idUsuarioConductor);

    @Query(value = "SELECT * FROM SERVICIO WHERE fecha = :fecha", nativeQuery = true)
    Collection<Servicio> darServiciosPorFecha(@Param("fecha") String fecha);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO SERVICIO (distancia_km, id_tarifa, tipo_servicio, fecha, costo, id_usuarioConductor, id_usuarioServicio, id_vehiculo) " +
                   "VALUES (:distanciaKm, :idTarifa, :tipoServicio, :fecha, :costo, :idUsuarioConductor, :idUsuarioServicio, :idVehiculo)", nativeQuery = true)
    void insertarServicio(@Param("distanciaKm") Double distanciaKm,
                          @Param("idTarifa") Long idTarifa,
                          @Param("tipoServicio") String tipoServicio,
                          @Param("fecha") String fecha,
                          @Param("costo") Double costo,
                          @Param("idUsuarioConductor") Long idUsuarioConductor,
                          @Param("idUsuarioServicio") Long idUsuarioServicio,
                          @Param("idVehiculo") Long idVehiculo);

    @Modifying
    @Transactional
    @Query(value = "UPDATE SERVICIO SET distancia_km = :distanciaKm, id_tarifa = :idTarifa, tipo_servicio = :tipoServicio, " +
                   "fecha = :fecha, costo = :costo, id_usuarioConductor = :idUsuarioConductor, id_usuarioServicio = :idUsuarioServicio, id_vehiculo = :idVehiculo " +
                   "WHERE id = :id", nativeQuery = true)
    void actualizarServicio(@Param("id") Long id,
                            @Param("distanciaKm") Double distanciaKm,
                            @Param("idTarifa") Long idTarifa,
                            @Param("tipoServicio") String tipoServicio,
                            @Param("fecha") String fecha,
                            @Param("costo") Double costo,
                            @Param("idUsuarioConductor") Long idUsuarioConductor,
                            @Param("idUsuarioServicio") Long idUsuarioServicio,
                            @Param("idVehiculo") Long idVehiculo);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM SERVICIO WHERE id = :id", nativeQuery = true)
    void eliminarServicio(@Param("id") Long id);

     /**
     * RFC1 - Consultar el histórico de todos los servicios pedidos por un usuario de servicio.
     */
    @Query(value = "SELECT * FROM VW_HISTORICO_SERVICIOS_POR_USUARIO WHERE id_usuarioServicio = :idUsuarioServicio ORDER BY fecha DESC, hora_inicio DESC", nativeQuery = true)
    Collection<Object[]> darHistoricoServiciosPorUsuario(@Param("idUsuarioServicio") Long idUsuarioServicio);

    /**
     * RFC4 - Mostrar la utilización de servicios en una ciudad durante un rango de fechas.
     */
    @Query(value =
        "SELECT NVL(tarifa_nivel, 'SIN_TARIFA') AS nivel, " +
        "       COUNT(*) AS servicios_count, " +
        "       ROUND(COUNT(*) / SUM(COUNT(*)) OVER () * 100, 2) AS porcentaje " +
        "FROM ( " +
        "  SELECT fecha, tarifa_nivel FROM VW_USO_SERVICIOS_BASE " +
        "  WHERE ciudad_id = :idCiudad " +
        "    AND fecha BETWEEN TO_DATE(:fechaDesde, 'YYYY-MM-DD') AND TO_DATE(:fechaHasta, 'YYYY-MM-DD') " +
        ") sub " +
        "GROUP BY tarifa_nivel " +
        "ORDER BY servicios_count DESC",
        nativeQuery = true)
    Collection<Object[]> darUsoServiciosPorCiudadYRango(@Param("idCiudad") Integer idCiudad,
                                                       @Param("fechaDesde") String fechaDesde,
                                                       @Param("fechaHasta") String fechaHasta);
}
