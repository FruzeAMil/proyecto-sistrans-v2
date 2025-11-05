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

    /**
     * RFC2 - Mostrar los 20 usuarios conductores que más servicios han prestado.
    */
    @Query(value =
        "SELECT * FROM ( " +
        "  SELECT conductor_id, conductor_nombre, conductor_cedula, conductor_correo, conductor_celular, servicios_count, ultima_fecha_servicio " +
        "  FROM VW_CONDUCTORES_SERVICIOS_COUNT " +
        "  ORDER BY servicios_count DESC, ultima_fecha_servicio DESC " +
        ") WHERE ROWNUM <= 20",
        nativeQuery = true)
    Collection<Object[]> darTop20ConductoresMasServicios();

    /**
     * RFC3 - Total de dinero obtenido por un conductor para cada uno de sus vehículos, discriminado por tipo de servicio.
     */
    @Query(value =
        "SELECT conductor_id, conductor_nombre, vehiculo_id, vehiculo_placa, tipo_servicio, servicios_count, ingreso_bruto, comision_alpescab, ingreso_neto, total_bruto_por_vehiculo, total_comision_por_vehiculo, total_neto_por_vehiculo " +
        "FROM VW_GANANCIAS_CONDUCTOR_VEHICULO_POR_SERVICIO " +
        "WHERE conductor_id = :idConductor " +
        "ORDER BY vehiculo_id, tipo_servicio",
        nativeQuery = true)
    Collection<Object[]> darGananciasPorConductorVehiculoPorServicio(@Param("idConductor") Long idConductor);}
