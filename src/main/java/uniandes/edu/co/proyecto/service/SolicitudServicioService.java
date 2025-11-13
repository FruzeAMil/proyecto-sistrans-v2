package uniandes.edu.co.proyecto.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import uniandes.edu.co.proyecto.dto.SolicitudServicioDTO;
import uniandes.edu.co.proyecto.model.UsuarioConductor;
import uniandes.edu.co.proyecto.model.UsuarioServicio;
import uniandes.edu.co.proyecto.repositorio.PuntoRepository;
import uniandes.edu.co.proyecto.repositorio.ServicioRepository;
import uniandes.edu.co.proyecto.repositorio.UsuarioConductorRepository;
import uniandes.edu.co.proyecto.repositorio.UsuarioServicioRepository;
import uniandes.edu.co.proyecto.repositorio.ViajeRepository;

@Service
public class SolicitudServicioService {

    @Autowired
    private UsuarioServicioRepository usuarioServicioRepository;

    @Autowired
    private UsuarioConductorRepository usuarioConductorRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private PuntoRepository puntoRepository;

    /**
     * RF8 - SOLICITAR UN SERVICIO
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public Long solicitarServicio(SolicitudServicioDTO solicitud) throws Exception {
        
        System.out.println("=== SOLICITUD DE SERVICIO ===");
        
        System.out.println("Verificando medio de pago del usuario " + solicitud.getIdUsuarioServicio());
        UsuarioServicio usuario = verificarMedioDePago(solicitud.getIdUsuarioServicio());
        System.out.println("Usuario verificado: " + usuario.getNombre());
        
        System.out.println("Buscando conductor disponible para servicio " + solicitud.getTipoServicio());
        UsuarioConductor conductor = buscarConductorDisponible(solicitud.getTipoServicio());
        System.out.println("Conductor encontrado: " + conductor.getNombre() + " (ID: " + conductor.getId() + ")");
        
        System.out.println("Calculando tarifa del servicio");
        double costo = calcularTarifa(
            solicitud.getLongitudPartida(), 
            solicitud.getLatitudPartida(),
            solicitud.getLongitudLlegada(), 
            solicitud.getLatitudLlegada(),
            solicitud.getTipoServicio()
        );
        System.out.println("Costo calculado: $" + costo);
        
        System.out.println("Actualizando estado del conductor a OCUPADO");
        actualizarEstadoConductor(conductor.getId(), "OCUPADO");
        System.out.println("Estado actualizado");
        
        System.out.println("Registrando servicio en base de datos");
        Long idServicio = registrarServicio(usuario, conductor, costo, solicitud);
        System.out.println("Servicio registrado con ID: " + idServicio);
        
        System.out.println("Registrando puntos de partida y llegada");
        registrarPuntos(idServicio, solicitud);
        System.out.println("Puntos registrados");
        
        System.out.println("Registrando inicio del viaje");
        registrarInicioViaje(idServicio, costo);
        System.out.println("Viaje iniciado");
        
        System.out.println("=== SOLICITUD DE SERVICIO COMPLETADA ===");
        return idServicio;
    }

private UsuarioServicio verificarMedioDePago(Long idUsuario) throws Exception {
    UsuarioServicio usuario = usuarioServicioRepository.darUsuario(idUsuario);
    
    if (usuario == null) {
        throw new Exception("Usuario no encontrado con ID: " + idUsuario);
    }
    
    if (usuario.getNumTarjeta() == null || usuario.getNumTarjeta().trim().isEmpty()) {
        throw new Exception("El usuario " + usuario.getNombre() + 
                          " no tiene un medio de pago registrado. Por favor registre una tarjeta.");
    }
    
    return usuario;
}


    private UsuarioConductor buscarConductorDisponible(String tipoServicio) throws Exception {
        String nivelVehiculo = tipoServicio.toUpperCase();
        
        UsuarioConductor conductor = usuarioConductorRepository
            .buscarConductorDisponible(nivelVehiculo)
            .orElseThrow(() -> new Exception(
                "No hay conductores disponibles para el servicio de nivel " + tipoServicio + 
                ". Por favor intente más tarde."
            ));
        
        // Verificar disponibilidad
        int disponible = usuarioConductorRepository.verificarDisponibilidad(conductor.getId());
        if (disponible == 0) {
            throw new Exception("El conductor seleccionado ya no está disponible. Por favor intente nuevamente.");
        }
        
        return conductor;
    }

    private double calcularTarifa(Double lon1, Double lat1, Double lon2, Double lat2, String tipoServicio) {
        double distanciaKm = calcularDistanciaHaversine(lat1, lon1, lat2, lon2);
        
        double tarifaPorKm;
        switch (tipoServicio.toUpperCase()) {
            case "LARGE":
                tarifaPorKm = 3500;
                break;
            case "COMFORT":
                tarifaPorKm = 2500;
                break;
            case "ESTANDAR":
            default:
                tarifaPorKm = 1500;
                break;
        }
        
        double tarifaMinima = 5000;
        double costoCalculado = distanciaKm * tarifaPorKm;
        
        return Math.max(costoCalculado, tarifaMinima);
    }


    private double calcularDistanciaHaversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }

    private void actualizarEstadoConductor(Long idConductor, String nuevoEstado) {
        usuarioConductorRepository.actualizarEstado(idConductor, nuevoEstado);
    }

    private Long registrarServicio(UsuarioServicio usuario, UsuarioConductor conductor, 
                                   double costo, SolicitudServicioDTO solicitud) throws Exception {
        
        LocalDate fechaActual = LocalDate.now();
        
        // Obtener el vehículo del conductor
        Long idVehiculo = conductor.getVehiculos() != null && !conductor.getVehiculos().isEmpty() 
            ? conductor.getVehiculos().get(0).getId() 
            : null;
        
        if (idVehiculo == null) {
            throw new Exception("El conductor " + conductor.getNombre() + 
                              " no tiene un vehículo asignado. No se puede procesar el servicio.");
        }
    
        Long idServicio = servicioRepository.obtenerUltimoIdInsertado();
        if (idServicio == null) {
        throw new Exception("Error al obtener el ID del servicio creado");
        }

        Long idTarifa = solicitud.getIdTarifa(); 
    
    // return idServicio;
        
        // Calcular distancia
        double distanciaKm = calcularDistanciaHaversine(
            solicitud.getLatitudPartida(), 
            solicitud.getLongitudPartida(),
            solicitud.getLatitudLlegada(), 
            solicitud.getLongitudLlegada()
        );
        
        // Insertar servicio
        servicioRepository.insertarServicio(
            distanciaKm,
            costo,
            solicitud.getTipoServicio(),
            fechaActual,
            idTarifa,
            conductor.getId(),
            usuario.getId(),
            idVehiculo
        );
        
        // // Obtener el ID del servicio recién insertado
        // Long idServicio = servicioRepository.obtenerUltimoIdInsertado();
        // if (idServicio == null) {
        //     throw new Exception("Error al obtener el ID del servicio creado");
        // }
        
        return idServicio;
    }

    private void registrarPuntos(Long idServicio, SolicitudServicioDTO solicitud) {
        // Punto de partida
        puntoRepository.insertarPunto(
            solicitud.getDireccionPartida(),
            solicitud.getLongitudPartida(),
            solicitud.getLatitudPartida(),
            1,
            idServicio,
            solicitud.getIdCiudad()
        );
        
        // Punto de llegada
        puntoRepository.insertarPunto(
            solicitud.getDireccionLlegada(),
            solicitud.getLongitudLlegada(),
            solicitud.getLatitudLlegada(),
            2,
            idServicio,
            solicitud.getIdCiudad()
        );
    }

    private void registrarInicioViaje(Long idServicio, double costo) {
        LocalDateTime horaActual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String horaInicio = horaActual.format(formatter);
        
        viajeRepository.insertarViaje(
            horaInicio,
            null,
            costo,
            idServicio
        );
    }
   @Transactional
    public void actualizarServicio(Long id, SolicitudServicioDTO solicitud) {
        Double distanciaKm = 10.5;
        Long idTarifa = 1L;
        String tipoServicio = solicitud.getTipoServicio();
        String fecha = LocalDate.now().toString();
        Double costo = 35000.0;
        Long idUsuarioConductor = 2L;
        Long idUsuarioServicio = solicitud.getIdUsuarioServicio();
        Long idVehiculo = 3L;

        servicioRepository.actualizarServicio(
            id,
            distanciaKm,
            idTarifa,
            tipoServicio,
            fecha,
            costo,
            idUsuarioConductor,
            idUsuarioServicio,
            idVehiculo
        );
    }

    @Transactional
    public void eliminarServicio(Long id) {
        servicioRepository.eliminarServicio(id);
    }

    // RFC1 - Histórico con READ_COMMITTED
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Collection<Object[]> consultarHistoricoReadCommitted(Long idUsuario) {
        System.out.println("Ejecutando RFC1 con READ_COMMITTED...");
        Collection<Object[]> primeraLectura = servicioRepository.consultarHistoricoPorUsuario(idUsuario);
        try {
            System.out.println("Esperando 30 segundos...");
            Thread.sleep(30000); // ⏳ Espera para observar efectos concurrentes
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Collection<Object[]> segundaLectura = servicioRepository.consultarHistoricoPorUsuario(idUsuario);
        return segundaLectura;
    }

    // RFC1 - Histórico con SERIALIZABLE
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Collection<Object[]> consultarHistoricoSerializable(Long idUsuario) {
        System.out.println("Ejecutando RFC1 con SERIALIZABLE...");
        Collection<Object[]> primeraLectura = servicioRepository.consultarHistoricoPorUsuario(idUsuario);
        try {
            System.out.println("Esperando 30 segundos...");
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Collection<Object[]> segundaLectura = servicioRepository.consultarHistoricoPorUsuario(idUsuario);
        return segundaLectura;
    }

}