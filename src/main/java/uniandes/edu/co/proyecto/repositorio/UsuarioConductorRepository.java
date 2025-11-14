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

    // RF3 - Registrar un usuario conductor
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
    @Query(value = """
        SELECT 
            uc.id AS id_conductor,
            uc.nombre,
            uc.cedula,
            uc.correo,
            COUNT(s.id) AS total_servicios
        FROM USUARIO_CONDUCTOR uc
        JOIN SERVICIO s ON uc.id = s.id_usuarioConductor
        GROUP BY uc.id, uc.nombre, uc.cedula, uc.correo
        ORDER BY total_servicios DESC
        FETCH FIRST 20 ROWS ONLY
        """, nativeQuery = true)
    Collection<Object[]> darTop20ConductoresMasServicios();
    
    /* RFC3 - ganancias por conductor por vehículo discriminado por servicio */
    @Query(value = """
        SELECT 
            uc.id AS id_conductor,
            uc.nombre AS nombre_conductor,
            v.id AS id_vehiculo,
            v.placa,
            v.marca,
            v.modelo,
            COUNT(s.id) AS total_servicios,
            SUM(s.costo) AS total_bruto,
            SUM(s.costo * 0.9) AS total_neto
        FROM SERVICIO s
        JOIN VEHICULO v ON s.id_vehiculo = v.id
        JOIN USUARIO_CONDUCTOR uc ON v.id_usuarioConductor = uc.id
        GROUP BY uc.id, uc.nombre, v.id, v.placa, v.marca, v.modelo
        ORDER BY uc.id, v.id
        """, nativeQuery = true)
    Collection<Object[]> darGananciasPorConductorVehiculoPorServicio();

    // RF8 - Buscar conductor disponible cerca de una ubicación para asignar servicio
    @Query(value = "SELECT uc.* FROM USUARIO_CONDUCTOR uc " +
                   "INNER JOIN VEHICULO v ON uc.id = v.id_usuarioConductor " +
                   "WHERE uc.estado = 'DISPONIBLE' " +
                   "AND v.nivel = :nivelServicio " +
                   "AND ROWNUM = 1 " +
                   "ORDER BY DBMS_RANDOM.VALUE", 
           nativeQuery = true)
    Optional<UsuarioConductor> buscarConductorDisponible(@Param("nivelServicio") String nivelServicio);

    // RF8 - Actualizar estado del conductor (DISPONIBLE/OCUPADO)
    @Modifying
    @Transactional
    @Query(value = "UPDATE USUARIO_CONDUCTOR SET estado = :estado WHERE id = :id", nativeQuery = true)
    void actualizarEstado(@Param("id") Long id, @Param("estado") String estado);
    
    // RF8 - Verificar si el conductor está disponible
    @Query(value = "SELECT COUNT(*) FROM USUARIO_CONDUCTOR WHERE id = :id AND estado = 'DISPONIBLE'", 
           nativeQuery = true)
    int verificarDisponibilidad(@Param("id") Long id);
}
