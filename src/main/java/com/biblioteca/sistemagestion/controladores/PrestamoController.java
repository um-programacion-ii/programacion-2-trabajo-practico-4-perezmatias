package com.biblioteca.sistemagestion.controladores;

import com.biblioteca.sistemagestion.servicios.PrestamoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Objects;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = Objects.requireNonNull(prestamoService, "PrestamoService no puede ser nulo.");
    }

}