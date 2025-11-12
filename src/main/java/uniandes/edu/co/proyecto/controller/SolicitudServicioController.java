package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uniandes.edu.co.proyecto.dto.SolicitudServicioDTO;
import uniandes.edu.co.proyecto.service.SolicitudServicioService;

@RestController
@RequestMapping("/api/servicios")
public class SolicitudServicioController {

    @Autowired
    private SolicitudServicioService solicitudServicioService;

    @PostMapping("/solicitar")
    public ResponseEntity<?> solicitarServicio(@RequestBody SolicitudServicioDTO solicitud) {
        try {
            Long idServicio = solicitudServicioService.solicitarServicio(solicitud);
            return ResponseEntity.ok().body(
                "✓ Servicio solicitado exitosamente. ID del servicio: " + idServicio
            );
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("✗ Error al solicitar servicio: " + e.getMessage());
        }
    }

    @GetMapping("/solicitar")
    public ResponseEntity<String> testGet() {
    return ResponseEntity.ok("GET endpoint reached correctly");
    }
}