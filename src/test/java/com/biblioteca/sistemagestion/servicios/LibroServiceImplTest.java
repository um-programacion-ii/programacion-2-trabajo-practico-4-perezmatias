package com.biblioteca.sistemagestion.servicios;

import com.biblioteca.sistemagestion.modelo.Libro;
import com.biblioteca.sistemagestion.modelo.EstadoLibro;
import com.biblioteca.sistemagestion.repositorios.LibroRepository;
import com.biblioteca.sistemagestion.excepciones.RecursoDuplicadoException;
import com.biblioteca.sistemagestion.excepciones.LibroNoEncontradoException;


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


}