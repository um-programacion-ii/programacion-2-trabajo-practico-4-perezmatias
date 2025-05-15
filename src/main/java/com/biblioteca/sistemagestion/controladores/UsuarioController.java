package com.biblioteca.sistemagestion.controladores;

import com.biblioteca.sistemagestion.servicios.UsuarioService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Objects;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = Objects.requireNonNull(usuarioService, "UsuarioService no puede ser nulo.");
    }

}