package uniandes.edu.co.proyecto.repositorio;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import uniandes.edu.co.proyecto.model.UsuarioServicio;

public interface UsuarioServicioRepository extends JpaRepository<UsuarioServicio, Long> {
    

    @Query(value = "SELECT * FROM USUARIO_SERVICIO", nativeQuery = true)
    Collection<UsuarioServicio> darUsuarios();

    @Query(value = "SELECT * FROM USUARIO_SERVICIO WHERE id = :id", nativeQuery = true)
    UsuarioServicio darUsuario(@Param("id") Long id);

    @Query(value = "SELECT * FROM USUARIO_SERVICIO WHERE cedula = :cedula", nativeQuery = true)
    Optional<UsuarioServicio> buscarPorCedula(@Param("cedula") String cedula);

    @Query(value = "SELECT * FROM USUARIO_SERVICIO WHERE correo = :correo", nativeQuery = true)
    Optional<UsuarioServicio> buscarPorCorreo(@Param("correo") String correo);

    boolean existsByCedula(String cedula);

    boolean existsByCorreo(String correo);

    // RF2 - Registrar un usuario de servicios
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO USUARIO_SERVICIO (cedula, nombre, correo, celular, calificacion, num_tarjeta, nombre_tarjeta, vencimiento, codigo_seguridad) " +
                   "VALUES (:cedula, :nombre, :correo, :celular, :calificacion, :numTarjeta, :nombreTarjeta, :vencimiento, :codigoSeguridad)", nativeQuery = true)
    void insertarUsuario(@Param("cedula") String cedula,
                         @Param("nombre") String nombre,
                         @Param("correo") String correo,
                         @Param("celular") String celular,
                         @Param("calificacion") int calificacion,
                         @Param("numTarjeta") String numTarjeta,
                         @Param("nombreTarjeta") String nombreTarjeta,
                         @Param("vencimiento") String vencimiento,
                         @Param("codigoSeguridad") int codigoSeguridad);

    @Modifying
    @Transactional
    @Query(value = "UPDATE USUARIO_SERVICIO SET cedula = :cedula, nombre = :nombre, correo = :correo, celular = :celular, " +
                   "calificacion = :calificacion, num_tarjeta = :numTarjeta, nombre_tarjeta = :nombreTarjeta, vencimiento = :vencimiento, codigo_seguridad = :codigoSeguridad " +
                   "WHERE id = :id", nativeQuery = true)
    void actualizarUsuario(@Param("id") Long id,
                           @Param("cedula") String cedula,
                           @Param("nombre") String nombre,
                           @Param("correo") String correo,
                           @Param("celular") String celular,
                           @Param("calificacion") int calificacion,
                           @Param("numTarjeta") String numTarjeta,
                           @Param("nombreTarjeta") String nombreTarjeta,
                           @Param("vencimiento") String vencimiento,
                           @Param("codigoSeguridad") int codigoSeguridad);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM USUARIO_SERVICIO WHERE id = :id", nativeQuery = true)
    void eliminarUsuario(@Param("id") Long id);

    // RF8 - Verificar que el usuario tiene un medio de pago registrado
    @Query(value = "SELECT COUNT(*) FROM USUARIO_SERVICIO WHERE id = :id AND num_tarjeta IS NOT NULL", nativeQuery = true)
    int verificarMedioDePago(@Param("id") Long id);
}