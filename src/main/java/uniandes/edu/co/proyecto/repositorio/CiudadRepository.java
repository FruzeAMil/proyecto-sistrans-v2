package uniandes.edu.co.proyecto.repositorio;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import uniandes.edu.co.proyecto.model.Ciudad;

public interface CiudadRepository extends JpaRepository<Ciudad, Integer> {

    @Query(value = "SELECT * FROM CIUDAD", nativeQuery = true)
    Collection<Ciudad> darCiudades();

    @Query(value = "SELECT * FROM CIUDAD WHERE id = :id", nativeQuery = true)
    Ciudad darCiudad(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO CIUDAD (nombre) VALUES (:nombre)", nativeQuery = true)
    void insertarCiudad(@Param("nombre") String nombre);

    @Modifying
    @Transactional
    @Query(value = "UPDATE CIUDAD SET nombre = :nombre WHERE id = :id", nativeQuery = true)
    void actualizarCiudad(@Param("id") Integer id, @Param("nombre") String nombre);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM CIUDAD WHERE id = :id", nativeQuery = true)
    void eliminarCiudad(@Param("id") Integer id);
}