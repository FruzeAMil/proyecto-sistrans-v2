package uniandes.edu.co.proyecto.repositorio;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import uniandes.edu.co.proyecto.model.Viaje;

public interface ViajeRepository extends JpaRepository<Viaje, Long> {

    @Query(value = "SELECT * FROM VIAJE", nativeQuery = true)
    Collection<Viaje> darViajes();

    @Query(value = "SELECT * FROM VIAJE WHERE id = :id", nativeQuery = true)
    Viaje darViaje(@Param("id") Long id);

    @Query(value = "SELECT * FROM VIAJE WHERE id_servicio = :idServicio", nativeQuery = true)
    Collection<Viaje> darViajesPorServicio(@Param("idServicio") Long idServicio);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO VIAJE (horaInicio, horaFin, costo, id_servicio) VALUES (:horaInicio, :horaFin, :costo, :idServicio)", nativeQuery = true)
    void insertarViaje(@Param("horaInicio") String horaInicio,
                       @Param("horaFin") String horaFin,
                       @Param("costo") double costo,
                       @Param("idServicio") Long idServicio);

    @Modifying
    @Transactional
    @Query(value = "UPDATE VIAJE SET horaInicio = :horaInicio, horaFin = :horaFin, costo = :costo, id_servicio = :idServicio WHERE id = :id", nativeQuery = true)
    void actualizarViaje(@Param("id") Long id,
                         @Param("horaInicio") String horaInicio,
                         @Param("horaFin") String horaFin,
                         @Param("costo") double costo,
                         @Param("idServicio") Long idServicio);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM VIAJE WHERE id = :id", nativeQuery = true)
    void eliminarViaje(@Param("id") Long id);
}