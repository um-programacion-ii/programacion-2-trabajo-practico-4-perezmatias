package com.biblioteca.sistemagestion.controladores;

import com.biblioteca.sistemagestion.servicios.LibroService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.biblioteca.sistemagestion.modelo.Libro;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import com.biblioteca.sistemagestion.excepciones.RecursoDuplicadoException;
import org.springframework.web.bind.annotation.PutMapping;
import com.biblioteca.sistemagestion.excepciones.LibroNoEncontradoException;

import java.util.Objects;


@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = Objects.requireNonNull(libroService, "LibroService no puede ser nulo.");
    }

    @GetMapping
    public List<Libro> obtenerTodos() {

        return libroService.obtenerTodosLosLibros();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerLibroPorId(@PathVariable Long id) {
        Optional<Libro> libroOptional = libroService.obtenerLibroPorId(id);

        if (libroOptional.isPresent()) {
            return ResponseEntity.ok(libroOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping
    public ResponseEntity<?> crearLibro(@RequestBody Libro libro) {
        try {
            Libro libroCreado = libroService.crearLibro(libro);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(libroCreado.getId())
                    .toUri();

            return ResponseEntity.created(location).body(libroCreado);

        } catch (RecursoDuplicadoException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizarLibro(@PathVariable Long id, @RequestBody Libro libroDetails) {
        try {
            Libro libroActualizado = libroService.actualizarLibro(id, libroDetails);
            return ResponseEntity.ok(libroActualizado);
        } catch (LibroNoEncontradoException e) {
            throw e;
        } catch (RecursoDuplicadoException e) {
            throw e; // Relanzamos
        }

    }

}