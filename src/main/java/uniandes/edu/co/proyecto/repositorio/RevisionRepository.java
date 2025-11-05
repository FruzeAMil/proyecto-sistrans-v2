package uniandes.edu.co.proyecto.repositorio;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import uniandes.edu.co.proyecto.model.Revision;

public interface RevisionRepository extends JpaRepository<Revision, Long> {

    @Query(value = "SELECT * FROM REVISION", nativeQuery = true)
    Collection<Revision> darRevisiones();

    @Query(value = "SELECT * FROM REVISION WHERE id = :id", nativeQuery = true)
    Revision darRevision(@Param("id") Long id);

    @Query(value = "SELECT * FROM REVISION WHERE id_servicio = :idServicio", nativeQuery = true)
    Collection<Revision> darRevisionesPorServicio(@Param("idServicio") Long idServicio);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO REVISION (calificacion, comentario, id_servicio) VALUES (:calificacion, :comentario, :idServicio)", nativeQuery = true)
    void insertarRevision(@Param("calificacion") int calificacion,
                          @Param("comentario") String comentario,
                          @Param("idServicio") Long idServicio);

    @Modifying
    @Transactional
    @Query(value = "UPDATE REVISION SET calificacion = :calificacion, comentario = :comentario WHERE id = :id", nativeQuery = true)
    void actualizarRevision(@Param("id") Long id,
                             @Param("calificacion") int calificacion,
                             @Param("comentario") String comentario);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM REVISION WHERE id = :id", nativeQuery = true)
    void eliminarRevision(@Param("id") Long id);
}