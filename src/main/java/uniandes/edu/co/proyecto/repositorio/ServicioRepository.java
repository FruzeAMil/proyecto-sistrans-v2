package uniandes.edu.co.proyecto.repositorio;

import java.time.LocalDate;
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
    @Query(value = "UPDATE SERVICIO SET distanciaKm = :distanciaKm, idTarifa = :idTarifa, tipoServicio = :tipoServicio, " +
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

    // RFC1 - historico por usuario
    @Query(value = """
        SELECT s.id, s.fecha, s.tipoServicio, s.costo,
            uc.nombre AS nombreConductor, us.nombre AS nombreUsuario,
            v.placa, v.marca, v.modelo, v.nivel
        FROM SERVICIO s
        JOIN USUARIO_CONDUCTOR uc ON s.id_usuarioConductor = uc.id
        JOIN USUARIO_SERVICIO us ON s.id_usuarioServicio = us.id
        JOIN VEHICULO v ON s.id_vehiculo = v.id
        WHERE s.id_usuarioServicio = :idUsuario
        """, nativeQuery = true)
    Collection<Object[]> consultarHistoricoPorUsuario(@Param("idUsuario") Long idUsuario);


    // RFC4 – utilización de servicios en ciudad en rango de fechas
    @Query(value =
        "SELECT c.nombre AS ciudad, t.tipoServicio, t.nivel, COUNT(s.idServicio) AS cantidad_servicios, " +
        "       ROUND(100.0 * COUNT(s.idServicio) / SUM(COUNT(s.idServicio)) OVER (), 2) AS porcentaje, " +
        "       SUM(s.costoTotal) AS valor_total " +
        "FROM Servicio s " +
        "INNER JOIN Vehiculo v ON s.idVehiculo = v.idVehiculo " +
        "INNER JOIN Ciudad c ON v.idCiudadExpedicion = c.idCiudad " +
        "INNER JOIN Tarifa t ON s.idTarifa = t.idTarifa " +
        "WHERE c.idCiudad = :idCiudad " +
        "  AND s.fechaInicio BETWEEN :fechaInicio AND :fechaFin " +
        "GROUP BY c.nombre, t.tipoServicio, t.nivel " +
        "ORDER BY cantidad_servicios DESC",
        nativeQuery = true)
    Collection<Object[]> darUsoServiciosPorCiudadYRango(@Param("idCiudad") Integer idCiudad,
                                                       @Param("fechaInicio") String fechaInicio,
                                                       @Param("fechaFin") String fechaFin);

    @Query(value = "SELECT MAX(id) FROM SERVICIO", nativeQuery = true)
    Long obtenerUltimoIdInsertado();

        @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO servicio 
        (distanciaKm, costo, tipoServicio, fecha, idTarifa, id_vehiculo, id_usuarioConductor, id_usuarioServicio) 
        VALUES (:distanciaKm, :costo, :tipoServicio, :fecha, :idTarifa, :idVehiculo, :idUsuarioConductor, :idUsuarioServicio)
        """, nativeQuery = true)
    void insertarServicio(
        Double distanciaKm,
        Double costo,
        String tipoServicio,
        LocalDate fecha,
        Long idTarifa,
        Long idVehiculo,
        Long idUsuarioConductor,
        Long idUsuarioServicio
    );
}
