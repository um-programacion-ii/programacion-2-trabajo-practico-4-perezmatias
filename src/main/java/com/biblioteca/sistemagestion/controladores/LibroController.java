package com.biblioteca.sistemagestion.controladores;

import com.biblioteca.sistemagestion.servicios.LibroService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = Objects.requireNonNull(libroService, "LibroService no puede ser nulo.");
    }

}