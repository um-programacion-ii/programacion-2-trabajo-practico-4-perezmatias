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

}