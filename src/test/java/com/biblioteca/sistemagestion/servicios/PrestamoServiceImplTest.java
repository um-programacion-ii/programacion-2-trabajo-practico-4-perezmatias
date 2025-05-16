package com.biblioteca.sistemagestion.servicios;

import com.biblioteca.sistemagestion.modelo.Libro;
import com.biblioteca.sistemagestion.modelo.Usuario;
import com.biblioteca.sistemagestion.modelo.Prestamo;
import com.biblioteca.sistemagestion.modelo.EstadoLibro;
import com.biblioteca.sistemagestion.modelo.EstadoUsuario;
import com.biblioteca.sistemagestion.repositorios.LibroRepository;
import com.biblioteca.sistemagestion.repositorios.UsuarioRepository;
import com.biblioteca.sistemagestion.repositorios.PrestamoRepository;
import com.biblioteca.sistemagestion.excepciones.LibroNoEncontradoException;
import com.biblioteca.sistemagestion.excepciones.UsuarioNoEncontradoException;
import com.biblioteca.sistemagestion.excepciones.RecursoNoDisponibleException;
import com.biblioteca.sistemagestion.excepciones.PrestamoNoEncontradoException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrestamoServiceImplTest {

    @Mock
    private LibroRepository libroRepositoryMock;
    @Mock
    private UsuarioRepository usuarioRepositoryMock;
    @Mock
    private PrestamoRepository prestamoRepositoryMock;

    @InjectMocks
    private PrestamoServiceImpl prestamoService;

    private Libro libroDisponible;
    private Libro libroYaPrestado;
    private Usuario usuarioActivo;
    private Prestamo prestamoExistente;
    private Long libroIdExistente = 1L;
    private Long libroIdPrestado = 2L;
    private Long usuarioIdExistente = 1L;
    private Long prestamoIdExistente = 100L;
    private LocalDate fechaDevolucionValida;

    @BeforeEach
    void setUp() {
        libroDisponible = new Libro("ISBN-001", "Libro Disponible", "Autor Uno");
        libroDisponible.setId(libroIdExistente);
        libroDisponible.setEstado(EstadoLibro.DISPONIBLE);

        libroYaPrestado = new Libro("ISBN-002", "Libro Ya Prestado", "Autor Dos");
        libroYaPrestado.setId(libroIdPrestado);
        libroYaPrestado.setEstado(EstadoLibro.PRESTADO);

        usuarioActivo = new Usuario("Usuario Préstamo", "usuario.prestamo@example.com");
        usuarioActivo.setId(usuarioIdExistente);
        usuarioActivo.setEstado(EstadoUsuario.ACTIVO);

        fechaDevolucionValida = LocalDate.now().plusDays(14);

        prestamoExistente = new Prestamo(libroYaPrestado, usuarioActivo, fechaDevolucionValida);
        prestamoExistente.setId(prestamoIdExistente);
    }

    @Test
    @DisplayName("realizarPrestamo exitoso")
    void realizarPrestamo_Exitoso() {
        when(libroRepositoryMock.findById(libroIdExistente)).thenReturn(Optional.of(libroDisponible));
        when(usuarioRepositoryMock.findById(usuarioIdExistente)).thenReturn(Optional.of(usuarioActivo));
        when(libroRepositoryMock.save(any(Libro.class))).thenReturn(libroDisponible);
        when(prestamoRepositoryMock.save(any(Prestamo.class))).thenAnswer(invocation -> {
            Prestamo p = invocation.getArgument(0);
            if (p.getId() == null) {
                p.setId(System.nanoTime());
            }
            return p;
        });

        Prestamo prestamo = prestamoService.realizarPrestamo(libroIdExistente, usuarioIdExistente, fechaDevolucionValida);

        assertNotNull(prestamo);
        assertEquals(libroDisponible, prestamo.getLibro());
        assertEquals(usuarioActivo, prestamo.getUsuario());
        assertEquals(fechaDevolucionValida, prestamo.getFechaDevolucion());
        assertEquals(EstadoLibro.PRESTADO, libroDisponible.getEstado());

        verify(libroRepositoryMock).findById(libroIdExistente);
        verify(usuarioRepositoryMock).findById(usuarioIdExistente);
        verify(libroRepositoryMock).save(libroDisponible);
        verify(prestamoRepositoryMock).save(any(Prestamo.class));
    }

    @Test
    @DisplayName("realizarPrestamo lanza LibroNoEncontradoException")
    void realizarPrestamo_CuandoLibroNoEncontrado_LanzaLibroNoEncontradoException() {
        when(libroRepositoryMock.findById(999L)).thenReturn(Optional.empty());

        assertThrows(LibroNoEncontradoException.class, () -> {
            prestamoService.realizarPrestamo(999L, usuarioIdExistente, fechaDevolucionValida);
        });
        verify(libroRepositoryMock).findById(999L);
        verifyNoInteractions(usuarioRepositoryMock);
        verifyNoInteractions(prestamoRepositoryMock);
    }

    @Test
    @DisplayName("realizarPrestamo lanza UsuarioNoEncontradoException")
    void realizarPrestamo_CuandoUsuarioNoEncontrado_LanzaUsuarioNoEncontradoException() {
        when(libroRepositoryMock.findById(libroIdExistente)).thenReturn(Optional.of(libroDisponible));
        when(usuarioRepositoryMock.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () -> {
            prestamoService.realizarPrestamo(libroIdExistente, 999L, fechaDevolucionValida);
        });
        verify(libroRepositoryMock).findById(libroIdExistente);
        verify(usuarioRepositoryMock).findById(999L);
        verifyNoInteractions(prestamoRepositoryMock);
    }

    @Test
    @DisplayName("realizarPrestamo lanza RecursoNoDisponibleException si libro no está disponible")
    void realizarPrestamo_CuandoLibroNoDisponible_LanzaRecursoNoDisponibleException() {
        when(libroRepositoryMock.findById(libroIdPrestado)).thenReturn(Optional.of(libroYaPrestado));
        when(usuarioRepositoryMock.findById(usuarioIdExistente)).thenReturn(Optional.of(usuarioActivo));

        assertThrows(RecursoNoDisponibleException.class, () -> {
            prestamoService.realizarPrestamo(libroIdPrestado, usuarioIdExistente, fechaDevolucionValida);
        });
        verify(libroRepositoryMock).findById(libroIdPrestado);
        verify(usuarioRepositoryMock).findById(usuarioIdExistente);
        verifyNoInteractions(prestamoRepositoryMock);
    }

    @Test
    @DisplayName("realizarPrestamo lanza IllegalArgumentException si fechaDevolucion es inválida")
    void realizarPrestamo_ConFechaDevolucionInvalida_LanzaIllegalArgumentException() {
        LocalDate fechaPasada = LocalDate.now().minusDays(1);
        when(libroRepositoryMock.findById(libroIdExistente)).thenReturn(Optional.of(libroDisponible));
        when(usuarioRepositoryMock.findById(usuarioIdExistente)).thenReturn(Optional.of(usuarioActivo));

        assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.realizarPrestamo(libroIdExistente, usuarioIdExistente, fechaPasada);
        });
    }

    @Test
    @DisplayName("registrarDevolucion exitoso")
    void registrarDevolucion_Exitoso() {
        when(prestamoRepositoryMock.findById(prestamoIdExistente)).thenReturn(Optional.of(prestamoExistente));
        when(libroRepositoryMock.save(any(Libro.class))).thenReturn(libroYaPrestado);
        doNothing().when(prestamoRepositoryMock).deleteById(prestamoIdExistente);

        Prestamo prestamoDevuelto = prestamoService.registrarDevolucion(prestamoIdExistente);

        assertNotNull(prestamoDevuelto);
        assertEquals(EstadoLibro.DISPONIBLE, libroYaPrestado.getEstado());
        verify(prestamoRepositoryMock).findById(prestamoIdExistente);
        verify(libroRepositoryMock).save(libroYaPrestado);
        verify(prestamoRepositoryMock).deleteById(prestamoIdExistente);
    }

    @Test
    @DisplayName("registrarDevolucion lanza PrestamoNoEncontradoException")
    void registrarDevolucion_CuandoPrestamoNoEncontrado_LanzaPrestamoNoEncontradoException() {
        when(prestamoRepositoryMock.findById(999L)).thenReturn(Optional.empty());

        assertThrows(PrestamoNoEncontradoException.class, () -> {
            prestamoService.registrarDevolucion(999L);
        });
        verify(prestamoRepositoryMock).findById(999L);
        verifyNoInteractions(libroRepositoryMock);
    }

    @Test
    @DisplayName("registrarDevolucion lanza IllegalStateException si préstamo no tiene libro")
    void registrarDevolucion_CuandoLibroDelPrestamoEsNulo_LanzaIllegalStateException() {
        Prestamo prestamoSinLibro = new Prestamo(null, usuarioActivo, fechaDevolucionValida);
        prestamoSinLibro.setId(prestamoIdExistente);
        Prestamo mockPrestamoConLibroNulo = mock(Prestamo.class);
        when(mockPrestamoConLibroNulo.getLibro()).thenReturn(null);
        when(prestamoRepositoryMock.findById(prestamoIdExistente)).thenReturn(Optional.of(mockPrestamoConLibroNulo));

        assertThrows(IllegalStateException.class, () -> {
            prestamoService.registrarDevolucion(prestamoIdExistente);
        });
        verify(prestamoRepositoryMock).findById(prestamoIdExistente);
    }


    @Test
    @DisplayName("obtenerPrestamoPorId cuando existe devuelve Optional con Prestamo")
    void obtenerPrestamoPorId_CuandoExiste_DevuelveOptionalConPrestamo() {
        when(prestamoRepositoryMock.findById(prestamoIdExistente)).thenReturn(Optional.of(prestamoExistente));
        Optional<Prestamo> resultado = prestamoService.obtenerPrestamoPorId(prestamoIdExistente);
        assertTrue(resultado.isPresent());
        assertEquals(prestamoExistente, resultado.get());
        verify(prestamoRepositoryMock).findById(prestamoIdExistente);
    }

    @Test
    @DisplayName("obtenerPrestamoPorId cuando NO existe devuelve Optional vacío")
    void obtenerPrestamoPorId_CuandoNoExiste_DevuelveOptionalVacio() {
        when(prestamoRepositoryMock.findById(999L)).thenReturn(Optional.empty());
        Optional<Prestamo> resultado = prestamoService.obtenerPrestamoPorId(999L);
        assertTrue(resultado.isEmpty());
        verify(prestamoRepositoryMock).findById(999L);
    }

    @Test
    @DisplayName("obtenerTodosLosPrestamos devuelve lista")
    void obtenerTodosLosPrestamos_DevuelveLista() {
        List<Prestamo> listaEsperada = List.of(prestamoExistente);
        when(prestamoRepositoryMock.findAll()).thenReturn(listaEsperada);
        List<Prestamo> resultado = prestamoService.obtenerTodosLosPrestamos();
        assertEquals(listaEsperada, resultado);
        verify(prestamoRepositoryMock).findAll();
    }

    @Test
    @DisplayName("obtenerTodosLosPrestamos devuelve lista vacía si no hay")
    void obtenerTodosLosPrestamos_CuandoNoHay_DevuelveListaVacia() {
        when(prestamoRepositoryMock.findAll()).thenReturn(Collections.emptyList());
        List<Prestamo> resultado = prestamoService.obtenerTodosLosPrestamos();
        assertTrue(resultado.isEmpty());
        verify(prestamoRepositoryMock).findAll();
    }

    @Test
    @DisplayName("obtenerPrestamosPorUsuario devuelve lista de préstamos del usuario")
    void obtenerPrestamosPorUsuario_CuandoExisten_DevuelveLista() {
        List<Prestamo> listaEsperada = List.of(prestamoExistente);
        when(prestamoRepositoryMock.findByUsuarioId(usuarioIdExistente)).thenReturn(listaEsperada);
        List<Prestamo> resultado = prestamoService.obtenerPrestamosPorUsuario(usuarioIdExistente);
        assertEquals(listaEsperada, resultado);
        verify(prestamoRepositoryMock).findByUsuarioId(usuarioIdExistente);
    }

    @Test
    @DisplayName("obtenerPrestamosPorLibro devuelve lista de préstamos del libro")
    void obtenerPrestamosPorLibro_CuandoExisten_DevuelveLista() {
        List<Prestamo> listaEsperada = List.of(prestamoExistente);
        when(prestamoRepositoryMock.findByLibroId(libroIdPrestado)).thenReturn(listaEsperada);
        List<Prestamo> resultado = prestamoService.obtenerPrestamosPorLibro(libroIdPrestado);
        assertEquals(listaEsperada, resultado);
        verify(prestamoRepositoryMock).findByLibroId(libroIdPrestado);
    }
}