package com.biblioteca.sistemagestion.servicios;

import com.biblioteca.sistemagestion.modelo.Usuario;
import com.biblioteca.sistemagestion.modelo.EstadoUsuario;
import com.biblioteca.sistemagestion.repositorios.UsuarioRepository;
import com.biblioteca.sistemagestion.excepciones.UsuarioNoEncontradoException;
import com.biblioteca.sistemagestion.excepciones.RecursoDuplicadoException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = Objects.requireNonNull(usuarioRepository, "UsuarioRepository no puede ser nulo.");
    }

    @Override
    public Usuario crearUsuario(Usuario usuario) throws RecursoDuplicadoException, IllegalArgumentException {
        Objects.requireNonNull(usuario, "Los detalles del usuario no pueden ser nulos.");
        Objects.requireNonNull(usuario.getNombre(), "El nombre del usuario no puede ser nulo.");
        Objects.requireNonNull(usuario.getEmail(), "El email del usuario no puede ser nulo.");
        if (usuario.getId() != null) {
            throw new IllegalArgumentException("El ID debe ser nulo para un nuevo usuario.");
        }
        if (usuario.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del usuario no puede estar vacío.");
        }
        if (usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email del usuario no puede estar vacío.");
        }
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RecursoDuplicadoException("El email '" + usuario.getEmail() + "' ya está registrado.");
        }
        if (usuario.getEstado() == null) {
            usuario.setEstado(EstadoUsuario.ACTIVO);
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        Objects.requireNonNull(id, "El ID del usuario no puede ser nulo.");
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        Objects.requireNonNull(email, "El email no puede ser nulo.");
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuarioDetails) throws UsuarioNoEncontradoException {
        Objects.requireNonNull(id, "El ID del usuario a actualizar no puede ser nulo.");
        Objects.requireNonNull(usuarioDetails, "Los detalles del usuario para actualizar no pueden ser nulos.");

        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));

        if (usuarioDetails.getEmail() != null && !usuarioDetails.getEmail().equalsIgnoreCase(usuarioExistente.getEmail())) {
            Optional<Usuario> usuarioConNuevoEmail = usuarioRepository.findByEmail(usuarioDetails.getEmail());
            if (usuarioConNuevoEmail.isPresent() && !usuarioConNuevoEmail.get().getId().equals(id)) {
                throw new RecursoDuplicadoException("El nuevo email '" + usuarioDetails.getEmail() + "' ya está en uso por otro usuario.");
            }
            usuarioExistente.setEmail(usuarioDetails.getEmail());
        }

        if (usuarioDetails.getNombre() != null) {
            usuarioExistente.setNombre(usuarioDetails.getNombre());
        }
        if (usuarioDetails.getEstado() != null) {
            usuarioExistente.setEstado(usuarioDetails.getEstado());
        }

        return usuarioRepository.save(usuarioExistente);
    }

    @Override
    public void eliminarUsuario(Long id) throws UsuarioNoEncontradoException {
        Objects.requireNonNull(id, "El ID del usuario a eliminar no puede ser nulo.");
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNoEncontradoException(id);
        }
        usuarioRepository.deleteById(id);
    }
}