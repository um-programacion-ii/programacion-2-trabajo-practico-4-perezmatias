package com.biblioteca.sistemagestion.servicios;

import com.biblioteca.sistemagestion.modelo.Usuario;
import com.biblioteca.sistemagestion.modelo.EstadoUsuario;
import com.biblioteca.sistemagestion.repositorios.UsuarioRepository;
import com.biblioteca.sistemagestion.excepciones.UsuarioNoEncontradoException;
import com.biblioteca.sistemagestion.excepciones.RecursoDuplicadoException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepositoryMock;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuarioPrueba;
    private Usuario usuarioExistente;

    @BeforeEach
    void setUp() {
        usuarioPrueba = new Usuario("Usuario Nuevo Test", "nuevo.test@example.com");

        usuarioExistente = new Usuario("Usuario Existente Test", "existente.test@example.com");
        usuarioExistente.setId(1L);
        usuarioExistente.setEstado(EstadoUsuario.ACTIVO);
    }

    @Test
    @DisplayName("crearUsuario guarda y devuelve usuario cuando email no existe")
    void crearUsuario_cuandoEmailNoExiste_guardaYDevuelveUsuario() {
        when(usuarioRepositoryMock.findByEmail(usuarioPrueba.getEmail())).thenReturn(Optional.empty());
        when(usuarioRepositoryMock.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            if (u.getId() == null) {
                u.setId(System.nanoTime()); // Simula asignación de ID
            }
            return u;
        });

        Usuario usuarioCreado = usuarioService.crearUsuario(usuarioPrueba);

        assertNotNull(usuarioCreado);
        assertEquals(usuarioPrueba.getEmail(), usuarioCreado.getEmail());
        assertEquals(EstadoUsuario.ACTIVO, usuarioCreado.getEstado());
        assertNotNull(usuarioCreado.getId());
        verify(usuarioRepositoryMock).findByEmail(usuarioPrueba.getEmail());
        verify(usuarioRepositoryMock).save(usuarioPrueba);
    }

    @Test
    @DisplayName("crearUsuario lanza RecursoDuplicadoException si email ya existe")
    void crearUsuario_cuandoEmailExiste_lanzaRecursoDuplicadoException() {
        when(usuarioRepositoryMock.findByEmail(usuarioPrueba.getEmail())).thenReturn(Optional.of(usuarioExistente));

        RecursoDuplicadoException exception = assertThrows(RecursoDuplicadoException.class, () -> {
            usuarioService.crearUsuario(usuarioPrueba);
        });

        assertTrue(exception.getMessage().contains(usuarioPrueba.getEmail()));
        verify(usuarioRepositoryMock).findByEmail(usuarioPrueba.getEmail());
        verify(usuarioRepositoryMock, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("crearUsuario lanza IllegalArgumentException si nombre es vacío")
    void crearUsuario_conNombreVacio_lanzaIllegalArgumentException() {
        Usuario usuarioNombreVacio = new Usuario(" ", "vacio@example.com");
        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.crearUsuario(usuarioNombreVacio);
        });
    }

    @Test
    @DisplayName("crearUsuario lanza IllegalArgumentException si email es vacío")
    void crearUsuario_conEmailVacio_lanzaIllegalArgumentException() {
        Usuario usuarioEmailVacio = new Usuario("NombreValido", " ");
        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.crearUsuario(usuarioEmailVacio);
        });
    }

    @Test
    @DisplayName("obtenerUsuarioPorId con ID existente devuelve Optional con Usuario")
    void obtenerUsuarioPorId_cuandoUsuarioExiste_debeDevolverOptionalConUsuario() {
        when(usuarioRepositoryMock.findById(1L)).thenReturn(Optional.of(usuarioExistente));

        Optional<Usuario> resultadoOpt = usuarioService.obtenerUsuarioPorId(1L);

        assertTrue(resultadoOpt.isPresent());
        assertEquals(usuarioExistente, resultadoOpt.get());
        verify(usuarioRepositoryMock).findById(1L);
    }

    @Test
    @DisplayName("obtenerUsuarioPorId con ID no existente devuelve Optional vacío")
    void obtenerUsuarioPorId_cuandoUsuarioNoExiste_debeDevolverOptionalVacio() {
        when(usuarioRepositoryMock.findById(999L)).thenReturn(Optional.empty());

        Optional<Usuario> resultadoOpt = usuarioService.obtenerUsuarioPorId(999L);

        assertTrue(resultadoOpt.isEmpty());
        verify(usuarioRepositoryMock).findById(999L);
    }

    @Test
    @DisplayName("obtenerUsuarioPorEmail con email existente devuelve Optional con Usuario")
    void obtenerUsuarioPorEmail_cuandoEmailExiste_debeDevolverOptionalConUsuario() {
        when(usuarioRepositoryMock.findByEmail("existente.test@example.com")).thenReturn(Optional.of(usuarioExistente));

        Optional<Usuario> resultadoOpt = usuarioService.obtenerUsuarioPorEmail("existente.test@example.com");

        assertTrue(resultadoOpt.isPresent());
        assertEquals(usuarioExistente, resultadoOpt.get());
        verify(usuarioRepositoryMock).findByEmail("existente.test@example.com");
    }

    @Test
    @DisplayName("obtenerUsuarioPorEmail con email no existente devuelve Optional vacío")
    void obtenerUsuarioPorEmail_cuandoEmailNoExiste_debeDevolverOptionalVacio() {
        when(usuarioRepositoryMock.findByEmail("noexiste@example.com")).thenReturn(Optional.empty());

        Optional<Usuario> resultadoOpt = usuarioService.obtenerUsuarioPorEmail("noexiste@example.com");

        assertTrue(resultadoOpt.isEmpty());
        verify(usuarioRepositoryMock).findByEmail("noexiste@example.com");
    }

    @Test
    @DisplayName("obtenerTodosLosUsuarios cuando hay usuarios devuelve la lista correcta")
    void obtenerTodosLosUsuarios_cuandoHayUsuarios_debeDevolverLista() {
        List<Usuario> listaEsperada = List.of(usuarioExistente, usuarioPrueba);
        when(usuarioRepositoryMock.findAll()).thenReturn(listaEsperada);

        List<Usuario> resultado = usuarioService.obtenerTodosLosUsuarios();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.containsAll(listaEsperada));
        verify(usuarioRepositoryMock).findAll();
    }

    @Test
    @DisplayName("obtenerTodosLosUsuarios cuando no hay usuarios devuelve lista vacía")
    void obtenerTodosLosUsuarios_cuandoNoHayUsuarios_debeDevolverListaVacia() {
        when(usuarioRepositoryMock.findAll()).thenReturn(Collections.emptyList());

        List<Usuario> resultado = usuarioService.obtenerTodosLosUsuarios();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(usuarioRepositoryMock).findAll();
    }

    @Test
    @DisplayName("actualizarUsuario cuando el usuario existe actualiza y devuelve el usuario")
    void actualizarUsuario_cuandoUsuarioExiste_actualizaYDevuelveUsuario() {
        Long idExistente = usuarioExistente.getId();
        Usuario datosActualizacion = new Usuario("Nombre Actualizado", "actualizado@example.com");
        datosActualizacion.setEstado(EstadoUsuario.SUSPENDIDO);

        when(usuarioRepositoryMock.findById(idExistente)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepositoryMock.findByEmail(datosActualizacion.getEmail())).thenReturn(Optional.empty()); // Nuevo email no existe
        when(usuarioRepositoryMock.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario usuarioResultado = assertDoesNotThrow(() ->
                usuarioService.actualizarUsuario(idExistente, datosActualizacion)
        );

        assertNotNull(usuarioResultado);
        assertEquals(idExistente, usuarioResultado.getId());
        assertEquals("Nombre Actualizado", usuarioResultado.getNombre());
        assertEquals("actualizado@example.com", usuarioResultado.getEmail());
        assertEquals(EstadoUsuario.SUSPENDIDO, usuarioResultado.getEstado());

        verify(usuarioRepositoryMock).findById(idExistente);
        verify(usuarioRepositoryMock).findByEmail(datosActualizacion.getEmail());
        verify(usuarioRepositoryMock).save(usuarioExistente);
    }

    @Test
    @DisplayName("actualizarUsuario cuando el usuario NO existe lanza UsuarioNoEncontradoException")
    void actualizarUsuario_cuandoUsuarioNoExiste_lanzaUsuarioNoEncontradoException() {
        Long idNoExistente = 999L;
        Usuario datosActualizacion = new Usuario("Dummy", "dummy@example.com");
        when(usuarioRepositoryMock.findById(idNoExistente)).thenReturn(Optional.empty());

        UsuarioNoEncontradoException exception = assertThrows(UsuarioNoEncontradoException.class, () -> {
            usuarioService.actualizarUsuario(idNoExistente, datosActualizacion);
        });

        assertTrue(exception.getMessage().contains(String.valueOf(idNoExistente)));
        verify(usuarioRepositoryMock).findById(idNoExistente);
        verify(usuarioRepositoryMock, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("actualizarUsuario con email duplicado de OTRO usuario lanza RecursoDuplicadoException")
    void actualizarUsuario_conEmailDuplicadoDeOtroUsuario_lanzaRecursoDuplicadoException() {
        Long idUsuarioAActualizar = usuarioExistente.getId(); // ID: 1L, Email: existente.test@example.com
        Usuario otroUsuarioConEmail = new Usuario("Otro User", "nuevo.email@example.com");
        otroUsuarioConEmail.setId(2L); // ID diferente

        Usuario datosActualizacion = new Usuario(usuarioExistente.getNombre(), "nuevo.email@example.com"); // Intentando usar el email de otroUsuarioConEmail

        when(usuarioRepositoryMock.findById(idUsuarioAActualizar)).thenReturn(Optional.of(usuarioExistente));
        // Simulamos que el nuevo email ya está tomado por 'otroUsuarioConEmail'
        when(usuarioRepositoryMock.findByEmail(datosActualizacion.getEmail())).thenReturn(Optional.of(otroUsuarioConEmail));

        RecursoDuplicadoException exception = assertThrows(RecursoDuplicadoException.class, () -> {
            usuarioService.actualizarUsuario(idUsuarioAActualizar, datosActualizacion);
        });
        assertTrue(exception.getMessage().contains(datosActualizacion.getEmail()));
        verify(usuarioRepositoryMock).findById(idUsuarioAActualizar);
        verify(usuarioRepositoryMock).findByEmail(datosActualizacion.getEmail());
        verify(usuarioRepositoryMock, never()).save(any(Usuario.class));
    }


    @Test
    @DisplayName("eliminarUsuario cuando el usuario existe llama a deleteById del repositorio")
    void eliminarUsuario_cuandoUsuarioExiste_llamaDeleteDelRepositorio() {
        Long idExistente = usuarioExistente.getId();
        when(usuarioRepositoryMock.existsById(idExistente)).thenReturn(true);
        doNothing().when(usuarioRepositoryMock).deleteById(idExistente); // Para métodos void

        assertDoesNotThrow(() -> {
            usuarioService.eliminarUsuario(idExistente);
        });

        verify(usuarioRepositoryMock).existsById(idExistente);
        verify(usuarioRepositoryMock).deleteById(idExistente);
    }

    @Test
    @DisplayName("eliminarUsuario cuando el usuario NO existe lanza UsuarioNoEncontradoException")
    void eliminarUsuario_cuandoUsuarioNoExiste_lanzaUsuarioNoEncontradoException() {
        Long idNoExistente = 999L;
        when(usuarioRepositoryMock.existsById(idNoExistente)).thenReturn(false);

        UsuarioNoEncontradoException exception = assertThrows(UsuarioNoEncontradoException.class, () -> {
            usuarioService.eliminarUsuario(idNoExistente);
        });

        assertTrue(exception.getMessage().contains(String.valueOf(idNoExistente)));
        verify(usuarioRepositoryMock).existsById(idNoExistente);
        verify(usuarioRepositoryMock, never()).deleteById(anyLong());
    }
}