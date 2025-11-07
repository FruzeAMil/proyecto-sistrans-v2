package uniandes.edu.co.proyecto.repositorio;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import uniandes.edu.co.proyecto.model.UsuarioConductor;

public interface UsuarioConductorRepository extends JpaRepository<UsuarioConductor, Long> {

    @Query(value = "SELECT * FROM USUARIO_CONDUCTOR", nativeQuery = true)
    Collection<UsuarioConductor> darConductores();

    @Query(value = "SELECT * FROM USUARIO_CONDUCTOR WHERE id = :id", nativeQuery = true)
    UsuarioConductor darConductor(@Param("id") Long id);

    @Query(value = "SELECT * FROM USUARIO_CONDUCTOR WHERE cedula = :cedula", nativeQuery = true)
    Optional<UsuarioConductor> buscarPorCedula(@Param("cedula") String cedula);

    @Query(value = "SELECT * FROM USUARIO_CONDUCTOR WHERE correo = :correo", nativeQuery = true)
    Optional<UsuarioConductor> buscarPorCorreo(@Param("correo") String correo);

    boolean existsByCedula(String cedula);

    boolean existsByCorreo(String correo);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO USUARIO_CONDUCTOR (cedula, nombre, correo, celular, calificacion) " +
                   "VALUES (:cedula, :nombre, :correo, :celular, :calificacion)", nativeQuery = true)
    void insertarConductor(@Param("cedula") String cedula,
                           @Param("nombre") String nombre,
                           @Param("correo") String correo,
                           @Param("celular") String celular,
                           @Param("calificacion") int calificacion);

    @Modifying
    @Transactional
    @Query(value = "UPDATE USUARIO_CONDUCTOR SET cedula = :cedula, nombre = :nombre, correo = :correo, " +
                   "celular = :celular, calificacion = :calificacion WHERE id = :id", nativeQuery = true)
    void actualizarConductor(@Param("id") Long id,
                             @Param("cedula") String cedula,
                             @Param("nombre") String nombre,
                             @Param("correo") String correo,
                             @Param("celular") String celular,
                             @Param("calificacion") int calificacion);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM USUARIO_CONDUCTOR WHERE id = :id", nativeQuery = true)
    void eliminarConductor(@Param("id") Long id);

    /* RFC2 - top 20 conductores que más servicios han prestado */
    @Query(value =
        "SELECT uc.idUsuarioConductor, u.nombre, u.cedula, u.correo, u.celular, COUNT(s.idServicio) AS q_servicios " +
        "FROM Usuario_Conductor uc " +
        "INNER JOIN Usuario u ON uc.idUsuario = u.idUsuario " +
        "LEFT JOIN Servicio s ON s.idUsuarioConductor = uc.idUsuarioConductor " +
        "GROUP BY uc.idUsuarioConductor, u.nombre, u.cedula, u.correo, u.celular " +
        "ORDER BY q_servicios DESC " +
        "FETCH FIRST 20 ROWS ONLY",
        nativeQuery = true)
    Collection<Object[]> darTop20ConductoresMasServicios();
    
    /* RFC3 - ganancias por conductor por vehículo discriminado por servicio (filtrable por conductor) */
    @Query(value =
        "SELECT uc.idUsuarioConductor, v.idVehiculo, v.placa, v.marca, v.modelo, " +
        "       COUNT(s.idServicio) AS cantidad_servicios, SUM(s.costoTotal) AS valor_total_servicios, SUM(s.costoTotal) * 0.6 AS valor_neto_conductor " +
        "FROM Usuario_Conductor uc " +
        "INNER JOIN Vehiculo v ON v.idUsuarioConductor = uc.idUsuarioConductor " +
        "INNER JOIN Servicio s ON s.idVehiculo = v.idVehiculo AND s.idUsuarioConductor = uc.idUsuarioConductor " +
        "WHERE uc.idUsuarioConductor = :idConductor " +
        "GROUP BY uc.idUsuarioConductor, v.idVehiculo, v.placa, v.marca, v.modelo " +
        "ORDER BY uc.idUsuarioConductor, v.idVehiculo",
        nativeQuery = true)
    Collection<Object[]> darGananciasPorConductorVehiculoPorServicio(@Param("idConductor") Long idConductor);

}
