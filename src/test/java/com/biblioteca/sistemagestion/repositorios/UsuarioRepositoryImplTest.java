package com.biblioteca.sistemagestion.repositorios;

import com.biblioteca.sistemagestion.modelo.Usuario;
import com.biblioteca.sistemagestion.modelo.EstadoUsuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioRepositoryImplTest {

    private UsuarioRepository usuarioRepository;

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        usuarioRepository = new UsuarioRepositoryImpl();
        usuario1 = new Usuario("Ana Pérez", "ana.perez@example.com");
        usuario2 = new Usuario("Luis García", "luis.garcia@example.com");
    }

    @Test
    @DisplayName("Guardar un usuario nuevo asigna ID y lo almacena")
    void save_usuarioNuevo_asignaIdYAlmacena() {
        assertNull(usuario1.getId(), "El ID del usuario nuevo debe ser nulo antes de guardar.");

        Usuario usuarioGuardado = usuarioRepository.save(usuario1);

        assertNotNull(usuarioGuardado.getId(), "El ID del usuario no debería ser nulo después de guardar.");
        assertEquals(usuario1.getEmail(), usuarioGuardado.getEmail());
        Optional<Usuario> usuarioEncontradoOpt = usuarioRepository.findById(usuarioGuardado.getId());
        assertTrue(usuarioEncontradoOpt.isPresent(), "El usuario guardado debería encontrarse por ID.");
        assertEquals(usuarioGuardado, usuarioEncontradoOpt.get(), "El usuario encontrado no es el mismo que se guardó.");
    }

    @Test
    @DisplayName("Guardar un usuario existente actualiza sus datos")
    void save_usuarioExistente_actualizaDatos() {
        usuarioRepository.save(usuario1);
        String nuevoNombre = "Ana Pérez Modificado";
        usuario1.setNombre(nuevoNombre);

        Usuario usuarioActualizado = usuarioRepository.save(usuario1);

        assertEquals(usuario1.getId(), usuarioActualizado.getId());
        assertEquals(nuevoNombre, usuarioActualizado.getNombre());
        Optional<Usuario> usuarioEncontradoOpt = usuarioRepository.findById(usuario1.getId());
        assertTrue(usuarioEncontradoOpt.isPresent());
        assertEquals(nuevoNombre, usuarioEncontradoOpt.get().getNombre());
    }

    @Test
    @DisplayName("findById devuelve usuario existente")
    void findById_existente_devuelveUsuario() {
        usuarioRepository.save(usuario1);
        Long idExistente = usuario1.getId();

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idExistente);

        assertTrue(usuarioOpt.isPresent());
        assertEquals(usuario1, usuarioOpt.get());
    }

    @Test
    @DisplayName("findById devuelve Optional vacío para ID no existente")
    void findById_noExistente_devuelveOptionalVacio() {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(999L);
        assertTrue(usuarioOpt.isEmpty());
    }

    @Test
    @DisplayName("findByEmail devuelve usuario existente (ignorando mayúsculas)")
    void findByEmail_existente_devuelveUsuario() {
        usuarioRepository.save(usuario1);
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail("ANA.PEREZ@EXAMPLE.COM");
        assertTrue(usuarioOpt.isPresent());
        assertEquals(usuario1, usuarioOpt.get());
    }

    @Test
    @DisplayName("findByEmail devuelve Optional vacío para email no existente")
    void findByEmail_noExistente_devuelveOptionalVacio() {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail("noexiste@example.com");
        assertTrue(usuarioOpt.isEmpty());
    }

}