package com.biblioteca.sistemagestion.controladores;

import com.biblioteca.sistemagestion.servicios.UsuarioService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Objects;
import com.biblioteca.sistemagestion.modelo.Usuario;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = Objects.requireNonNull(usuarioService, "UsuarioService no puede ser nulo.");
    }

    @GetMapping
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioService.obtenerTodosLosUsuarios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioPorId(id);

        return usuarioOptional
                .map(usuarioEncontrado -> ResponseEntity.ok(usuarioEncontrado))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}