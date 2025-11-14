package uniandes.edu.co.proyecto.repositorio;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import uniandes.edu.co.proyecto.model.Vehiculo;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    @Query(value = "SELECT * FROM VEHICULO", nativeQuery = true)
    Collection<Vehiculo> darVehiculos();

    @Query(value = "SELECT * FROM VEHICULO WHERE id = :id", nativeQuery = true)
    Vehiculo darVehiculo(@Param("id") Long id);

    @Query(value = "SELECT * FROM VEHICULO WHERE id_usuarioConductor = :idUsuarioConductor", nativeQuery = true)
    Collection<Vehiculo> darVehiculosPorConductor(@Param("idUsuarioConductor") Long idUsuarioConductor);

    @Query(value = "SELECT * FROM VEHICULO WHERE placa = :placa", nativeQuery = true)
    Optional<Vehiculo> buscarPorPlaca(@Param("placa") String placa);

    boolean existsByPlaca(String placa);

    // RF4 - Registrar un vehículo para un usuario conductor
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO VEHICULO (placa, marca, modelo, capacidad_pasajeros, nivel, id_usuarioConductor) " +
                   "VALUES (:placa, :marca, :modelo, :capacidadPasajeros, :nivel, :idUsuarioConductor)", nativeQuery = true)
    void insertarVehiculo(@Param("placa") String placa,
                          @Param("marca") String marca,
                          @Param("modelo") String modelo,
                          @Param("capacidadPasajeros") int capacidadPasajeros,
                          @Param("nivel") String nivel,
                          @Param("idUsuarioConductor") Long idUsuarioConductor);

    @Modifying
    @Transactional
    @Query(value = "UPDATE VEHICULO SET placa = :placa, marca = :marca, modelo = :modelo, " +
                   "capacidad_pasajeros = :capacidadPasajeros, nivel = :nivel, id_usuarioConductor = :idUsuarioConductor " +
                   "WHERE id = :id", nativeQuery = true)
    void actualizarVehiculo(@Param("id") Long id,
                            @Param("placa") String placa,
                            @Param("marca") String marca,
                            @Param("modelo") String modelo,
                            @Param("capacidadPasajeros") int capacidadPasajeros,
                            @Param("nivel") String nivel,
                            @Param("idUsuarioConductor") Long idUsuarioConductor);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM VEHICULO WHERE id = :id", nativeQuery = true)
    void eliminarVehiculo(@Param("id") Long id);
}