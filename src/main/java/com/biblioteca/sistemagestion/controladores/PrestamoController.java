package com.biblioteca.sistemagestion.controladores;

import com.biblioteca.sistemagestion.servicios.PrestamoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Objects;
import com.biblioteca.sistemagestion.dtos.PrestamoRequestDTO;
import com.biblioteca.sistemagestion.modelo.Prestamo;
import com.biblioteca.sistemagestion.excepciones.LibroNoEncontradoException;
import com.biblioteca.sistemagestion.excepciones.UsuarioNoEncontradoException;
import com.biblioteca.sistemagestion.excepciones.RecursoNoDisponibleException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.PathVariable;
import com.biblioteca.sistemagestion.excepciones.PrestamoNoEncontradoException;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = Objects.requireNonNull(prestamoService, "PrestamoService no puede ser nulo.");
    }

    @PostMapping
    public ResponseEntity<Prestamo> realizarPrestamo(@RequestBody PrestamoRequestDTO requestDTO) {

        Prestamo prestamoCreado = prestamoService.realizarPrestamo(
                requestDTO.libroId(),
                requestDTO.usuarioId(),
                requestDTO.fechaDevolucionSugerida()
        );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(prestamoCreado.getId())
                .toUri();

        return ResponseEntity.created(location).body(prestamoCreado);
    }

    @PostMapping("/{prestamoId}/devolver")
    public ResponseEntity<Prestamo> registrarDevolucion(@PathVariable Long prestamoId) {
        try {
            Prestamo prestamoDevuelto = prestamoService.registrarDevolucion(prestamoId);
            return ResponseEntity.ok(prestamoDevuelto);
        } catch (PrestamoNoEncontradoException e) {
            throw e;
        } catch (IllegalStateException e) {
            throw e;
        }
    }

    @GetMapping
    public List<Prestamo> obtenerTodosLosPrestamos() {
        return prestamoService.obtenerTodosLosPrestamos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> obtenerPrestamoPorId(@PathVariable Long id) {
        Optional<Prestamo> prestamoOptional = prestamoService.obtenerPrestamoPorId(id);

        return prestamoOptional
                .map(prestamo -> ResponseEntity.ok(prestamo))
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