package com.biblioteca.sistemagestion.controladores;

import com.biblioteca.sistemagestion.dtos.PrestamoRequestDTO;
import com.biblioteca.sistemagestion.modelo.Prestamo;
import com.biblioteca.sistemagestion.servicios.PrestamoService;
import com.biblioteca.sistemagestion.excepciones.LibroNoEncontradoException;
import com.biblioteca.sistemagestion.excepciones.UsuarioNoEncontradoException;
import com.biblioteca.sistemagestion.excepciones.RecursoNoDisponibleException;
import com.biblioteca.sistemagestion.excepciones.PrestamoNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = Objects.requireNonNull(prestamoService, "PrestamoService no puede ser nulo.");
    }

    @PostMapping
    public ResponseEntity<Prestamo> realizarPrestamo(@RequestBody PrestamoRequestDTO requestDTO)
            throws LibroNoEncontradoException, UsuarioNoEncontradoException, RecursoNoDisponibleException, IllegalArgumentException {
        Prestamo prestamoCreado = prestamoService.realizarPrestamo(
                requestDTO.libroId(),
                requestDTO.usuarioId(),
                requestDTO.fechaDevolucionSugerida()
        );
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(prestamoCreado.getId()).toUri();
        return ResponseEntity.created(location).body(prestamoCreado);
    }

    @PostMapping("/{prestamoId}/devolver")
    public ResponseEntity<Prestamo> registrarDevolucion(@PathVariable Long prestamoId)
            throws PrestamoNoEncontradoException, IllegalStateException {
        Prestamo prestamoDevuelto = prestamoService.registrarDevolucion(prestamoId);
        return ResponseEntity.ok(prestamoDevuelto);
    }

    @GetMapping
    public List<Prestamo> obtenerTodosLosPrestamos() {
        return prestamoService.obtenerTodosLosPrestamos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> obtenerPrestamoPorId(@PathVariable Long id) {
        Optional<Prestamo> prestamoOptional = prestamoService.obtenerPrestamoPorId(id);
        return prestamoOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Prestamo>> obtenerPrestamosPorUsuario(@PathVariable Long usuarioId) {
        List<Prestamo> prestamos = prestamoService.obtenerPrestamosPorUsuario(usuarioId);
        return ResponseEntity.ok(prestamos);
    }

    @GetMapping("/libro/{libroId}")
    public ResponseEntity<List<Prestamo>> obtenerPrestamosPorLibro(@PathVariable Long libroId) {
        List<Prestamo> prestamos = prestamoService.obtenerPrestamosPorLibro(libroId);
        return ResponseEntity.ok(prestamos);
    }
}