package com.biblioteca.sistemagestion.servicios;

import com.biblioteca.sistemagestion.modelo.Libro;
import com.biblioteca.sistemagestion.modelo.EstadoLibro;
import com.biblioteca.sistemagestion.repositorios.LibroRepository;
import com.biblioteca.sistemagestion.excepciones.RecursoDuplicadoException;
import com.biblioteca.sistemagestion.excepciones.LibroNoEncontradoException;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibroServiceImplTest {

    @Mock
    private LibroRepository libroRepositoryMock;

    @InjectMocks
    private LibroServiceImpl libroService;

    private Libro libroPrueba;
    private Libro libroExistente;

    @BeforeEach
    void setUp() {
        libroPrueba = new Libro("123-456", "Libro de Prueba", "Autor de Prueba");
        libroExistente = new Libro("789-012", "Libro Existente", "Autor Existente");
        libroExistente.setId(1L);
        libroExistente.setEstado(EstadoLibro.DISPONIBLE);
    }

    @Test
    @DisplayName("crearLibro guarda y devuelve el libro cuando el ISBN no existe")
    void crearLibro_cuandoIsbnNoExiste_guardaYDevuelveLibro() {
        when(libroRepositoryMock.findByIsbn(libroPrueba.getIsbn())).thenReturn(Optional.empty());
        when(libroRepositoryMock.save(any(Libro.class))).thenAnswer(invocation -> {
            Libro libroAGuardar = invocation.getArgument(0);
            if (libroAGuardar.getId() == null) {
                libroAGuardar.setId(System.currentTimeMillis());
            }
            return libroAGuardar;
        });

        Libro libroCreado = libroService.crearLibro(libroPrueba);

        assertNotNull(libroCreado, "El libro creado no debería ser nulo.");
        assertEquals(libroPrueba.getIsbn(), libroCreado.getIsbn());
        assertEquals(EstadoLibro.DISPONIBLE, libroCreado.getEstado(), "El estado por defecto debería ser DISPONIBLE.");
        assertNotNull(libroCreado.getId(), "El libro creado debería tener un ID asignado por el mock de save.");

        verify(libroRepositoryMock).findByIsbn(libroPrueba.getIsbn());
        verify(libroRepositoryMock).save(libroPrueba);
    }

    @Test
    @DisplayName("crearLibro lanza RecursoDuplicadoException si el ISBN ya existe")
    void crearLibro_cuandoIsbnExiste_lanzaRecursoDuplicadoException() {
        when(libroRepositoryMock.findByIsbn(libroPrueba.getIsbn())).thenReturn(Optional.of(libroPrueba));

        RecursoDuplicadoException exception = assertThrows(RecursoDuplicadoException.class, () -> {
            libroService.crearLibro(libroPrueba);
        });

        assertTrue(exception.getMessage().contains(libroPrueba.getIsbn()));

        verify(libroRepositoryMock, never()).save(any(Libro.class));
    }

    @Test
    @DisplayName("obtenerLibroPorId con ID existente devuelve Optional con Libro")
    void obtenerLibroPorId_cuandoLibroExiste_debeDevolverOptionalConLibro() {
        Long idExistente = 1L;
        when(libroRepositoryMock.findById(idExistente)).thenReturn(Optional.of(libroExistente));
        Optional<Libro> resultadoOpt = libroService.obtenerLibroPorId(idExistente);
        assertTrue(resultadoOpt.isPresent(), "El Optional no debería estar vacío para un ID existente.");
        assertEquals(libroExistente, resultadoOpt.get(), "El libro devuelto no es el esperado.");
        verify(libroRepositoryMock).findById(idExistente);
    }

    @Test
    @DisplayName("obtenerLibroPorId con ID no existente devuelve Optional vacío")
    void obtenerLibroPorId_cuandoLibroNoExiste_debeDevolverOptionalVacio() {
        Long idNoExistente = 999L;
        when(libroRepositoryMock.findById(idNoExistente)).thenReturn(Optional.empty());
        Optional<Libro> resultadoOpt = libroService.obtenerLibroPorId(idNoExistente);
        assertTrue(resultadoOpt.isEmpty(), "El Optional debería estar vacío para un ID no existente.");
        verify(libroRepositoryMock).findById(idNoExistente);
    }

    @Test
    @DisplayName("obtenerLibroPorIsbn con ISBN existente devuelve Optional con Libro")
    void obtenerLibroPorIsbn_cuandoLibroExiste_debeDevolverOptionalConLibro() {

        String isbnExistente = libroExistente.getIsbn();
        when(libroRepositoryMock.findByIsbn(isbnExistente)).thenReturn(Optional.of(libroExistente));

        Optional<Libro> resultadoOpt = libroService.obtenerLibroPorIsbn(isbnExistente);

        assertTrue(resultadoOpt.isPresent(), "El Optional no debería estar vacío para un ISBN existente.");
        assertEquals(libroExistente, resultadoOpt.get(), "El libro devuelto no es el esperado.");
        verify(libroRepositoryMock).findByIsbn(isbnExistente);
    }

    @Test
    @DisplayName("obtenerLibroPorIsbn con ISBN no existente devuelve Optional vacío")
    void obtenerLibroPorIsbn_cuandoLibroNoExiste_debeDevolverOptionalVacio() {
        String isbnNoExistente = "ISBN-INEXISTENTE-123";
        when(libroRepositoryMock.findByIsbn(isbnNoExistente)).thenReturn(Optional.empty());

        Optional<Libro> resultadoOpt = libroService.obtenerLibroPorIsbn(isbnNoExistente);

        assertTrue(resultadoOpt.isEmpty(), "El Optional debería estar vacío para un ISBN no existente.");
        verify(libroRepositoryMock).findByIsbn(isbnNoExistente);
    }

    @Test
    @DisplayName("obtenerTodosLosLibros cuando hay libros devuelve la lista correcta")
    void obtenerTodosLosLibros_cuandoHayLibros_debeDevolverLista() {
        List<Libro> listaEsperada = new ArrayList<>();
        listaEsperada.add(libroPrueba);
        listaEsperada.add(libroExistente);

        when(libroRepositoryMock.findAll()).thenReturn(listaEsperada);

        List<Libro> resultado = libroService.obtenerTodosLosLibros();

        assertNotNull(resultado, "La lista devuelta no debería ser nula.");
        assertEquals(2, resultado.size(), "El tamaño de la lista devuelta no es el esperado.");
        assertTrue(resultado.containsAll(listaEsperada) && listaEsperada.containsAll(resultado),
                "Las listas no contienen los mismos elementos.");
        verify(libroRepositoryMock).findAll();
    }

    @Test
    @DisplayName("obtenerTodosLosLibros cuando no hay libros devuelve lista vacía")
    void obtenerTodosLosLibros_cuandoNoHayLibros_debeDevolverListaVacia() {
        when(libroRepositoryMock.findAll()).thenReturn(Collections.emptyList());

        List<Libro> resultado = libroService.obtenerTodosLosLibros();

        assertNotNull(resultado, "La lista devuelta no debería ser nula.");
        assertTrue(resultado.isEmpty(), "La lista devuelta debería estar vacía.");
        verify(libroRepositoryMock).findAll();
    }

    @Test
    @DisplayName("actualizarLibro cuando el libro existe actualiza y devuelve el libro")
    void actualizarLibro_cuandoLibroExiste_actualizaYDevuelveLibro() {
        Long idExistente = libroExistente.getId();

        Libro libroConNuevosDatos = new Libro();
        libroConNuevosDatos.setTitulo("Título Súper Actualizado");
        libroConNuevosDatos.setAutor("Autor Renovado");
        libroConNuevosDatos.setEstado(EstadoLibro.EN_REPARACION);
        Libro libroEnRepo = new Libro(libroExistente.getIsbn(), libroExistente.getTitulo(), libroExistente.getAutor());
        libroEnRepo.setId(idExistente);
        libroEnRepo.setEstado(libroExistente.getEstado());
        when(libroRepositoryMock.findById(idExistente)).thenReturn(Optional.of(libroEnRepo));
        when(libroRepositoryMock.save(any(Libro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Libro libroResultado = assertDoesNotThrow(() ->
                        libroService.actualizarLibro(idExistente, libroConNuevosDatos),
                "actualizarLibro no debería lanzar excepción si el libro existe."
        );

        assertNotNull(libroResultado, "El libro actualizado no debería ser nulo.");
        assertEquals(idExistente, libroResultado.getId(), "El ID del libro actualizado no coincide (debería ser el original).");
        assertEquals("Título Súper Actualizado", libroResultado.getTitulo(), "El título no se actualizó correctamente.");
        assertEquals("Autor Renovado", libroResultado.getAutor(), "El autor no se actualizó correctamente.");
        assertEquals(EstadoLibro.EN_REPARACION, libroResultado.getEstado(), "El estado no se actualizó correctamente.");
        assertEquals(libroExistente.getIsbn(), libroResultado.getIsbn(), "El ISBN no debería haber cambiado.");

        verify(libroRepositoryMock).findById(idExistente);
        verify(libroRepositoryMock).save(libroEnRepo);
    }

    @Test
    @DisplayName("actualizarLibro cuando el libro NO existe lanza LibroNoEncontradoException")
    void actualizarLibro_cuandoLibroNoExiste_lanzaLibroNoEncontradoException() {
        Long idNoExistente = 999L;
        Libro libroDetalles = new Libro("ISBN-DUMMY", "Dummy Titulo", "Dummy Autor");
        when(libroRepositoryMock.findById(idNoExistente)).thenReturn(Optional.empty());

        LibroNoEncontradoException exception = assertThrows(LibroNoEncontradoException.class, () -> {
            libroService.actualizarLibro(idNoExistente, libroDetalles);
        }, "Debería lanzarse LibroNoEncontradoException si el libro a actualizar no existe.");

        assertTrue(exception.getMessage().contains(String.valueOf(idNoExistente)),
                "El mensaje de la excepción debería contener el ID del libro no encontrado.");

        verify(libroRepositoryMock).findById(idNoExistente);
        verify(libroRepositoryMock, never()).save(any(Libro.class));
    }

    @Test
    @DisplayName("eliminarLibro cuando el libro existe llama a deleteById del repositorio")
    void eliminarLibro_cuandoLibroExiste_llamaDeleteDelRepositorio() {
        Long idExistente = libroExistente.getId();
        when(libroRepositoryMock.existsById(idExistente)).thenReturn(true);
        assertDoesNotThrow(() -> {
            libroService.eliminarLibro(idExistente);
        }, "eliminarLibro no debería lanzar excepción si el libro existe.");

        verify(libroRepositoryMock).existsById(idExistente);
        verify(libroRepositoryMock).deleteById(idExistente);
    }

    @Test
    @DisplayName("eliminarLibro cuando el libro NO existe lanza LibroNoEncontradoException")
    void eliminarLibro_cuandoLibroNoExiste_lanzaLibroNoEncontradoException() {
        Long idNoExistente = 999L;
        when(libroRepositoryMock.existsById(idNoExistente)).thenReturn(false);
        LibroNoEncontradoException exception = assertThrows(LibroNoEncontradoException.class, () -> {
            libroService.eliminarLibro(idNoExistente);
        }, "Debería lanzarse LibroNoEncontradoException si el libro a eliminar no existe.");

        assertTrue(exception.getMessage().contains(String.valueOf(idNoExistente)),
                "El mensaje de la excepción debería contener el ID del libro no encontrado.");

        verify(libroRepositoryMock).existsById(idNoExistente);
        verify(libroRepositoryMock, never()).deleteById(anyLong());
    }


}