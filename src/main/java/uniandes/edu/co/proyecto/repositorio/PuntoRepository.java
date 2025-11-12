package uniandes.edu.co.proyecto.repositorio;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import uniandes.edu.co.proyecto.model.Punto;

public interface PuntoRepository extends JpaRepository<Punto, Long> {

    @Query(value = "SELECT * FROM PUNTO", nativeQuery = true)
    Collection<Punto> darPuntos();

    @Query(value = "SELECT * FROM PUNTO WHERE id = :id", nativeQuery = true)
    Punto darPunto(@Param("id") Long id);

    @Query(value = "SELECT * FROM PUNTO WHERE id_ciudad = :idCiudad", nativeQuery = true)
    Collection<Punto> darPuntosPorCiudad(@Param("idCiudad") Integer idCiudad);

    @Query(value = "SELECT * FROM PUNTO WHERE id_ervicio = :idServicio", nativeQuery = true)
    Collection<Punto> darPuntosPorServicio(@Param("idServicio") Long idServicio);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO PUNTO (direccion, longitud, latitud, orden, id_servicio, id_ciudad) " +
                   "VALUES (:direccion, :longitud, :latitud, :orden, :idServicio, :idCiudad)", nativeQuery = true)
    void insertarPunto(@Param("direccion") String direccion,
                       @Param("longitud") Double longitud,
                       @Param("latitud") Double latitud,
                       @Param("orden") Integer orden,
                       @Param("idServicio") Long idServicio,
                       @Param("idCiudad") Long idCiudad);

    @Modifying
    @Transactional
    @Query(value = "UPDATE PUNTO SET direccion = :direccion, longitud = :longitud, latitud = :latitud, " +
                   "orden = :orden, id_servicio = :idServicio, id_ciudad = :idCiudad WHERE id = :id", nativeQuery = true)
    void actualizarPunto(@Param("id") Long id,
                         @Param("direccion") String direccion,
                         @Param("longitud") Double longitud,
                         @Param("latitud") Double latitud,
                         @Param("orden") Integer orden,
                         @Param("idServicio") Long idServicio,
                         @Param("idCiudad") Long idCiudad);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM PUNTO WHERE id = :id", nativeQuery = true)
    void eliminarPunto(@Param("id") Long id);
}