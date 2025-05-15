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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import com.biblioteca.sistemagestion.excepciones.RecursoDuplicadoException;
import org.springframework.web.bind.annotation.PutMapping;
import com.biblioteca.sistemagestion.excepciones.UsuarioNoEncontradoException;
import org.springframework.web.bind.annotation.DeleteMapping;

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

    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioCreado = usuarioService.crearUsuario(usuario);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(usuarioCreado.getId())
                    .toUri();
            return ResponseEntity.created(location).body(usuarioCreado);
        } catch (RecursoDuplicadoException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDetails) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioDetails);
            return ResponseEntity.ok(usuarioActualizado); // HTTP 200 OK
        } catch (UsuarioNoEncontradoException e) {
            throw e;
        } catch (RecursoDuplicadoException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (UsuarioNoEncontradoException e) {
            throw e;
        }
    }

}