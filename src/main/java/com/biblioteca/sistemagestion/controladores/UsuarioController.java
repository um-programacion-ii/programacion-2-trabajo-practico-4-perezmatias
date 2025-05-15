package com.biblioteca.sistemagestion.controladores;

import com.biblioteca.sistemagestion.modelo.Usuario;
import com.biblioteca.sistemagestion.servicios.UsuarioService;
import com.biblioteca.sistemagestion.excepciones.UsuarioNoEncontradoException;
import com.biblioteca.sistemagestion.excepciones.RecursoDuplicadoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
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
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario)
            throws RecursoDuplicadoException, IllegalArgumentException {
        Usuario usuarioCreado = usuarioService.crearUsuario(usuario);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(usuarioCreado.getId()).toUri();
        return ResponseEntity.created(location).body(usuarioCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDetails)
            throws UsuarioNoEncontradoException, RecursoDuplicadoException, IllegalArgumentException {
        Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioDetails);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id)
            throws UsuarioNoEncontradoException {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}