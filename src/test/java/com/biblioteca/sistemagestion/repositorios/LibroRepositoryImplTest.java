package com.biblioteca.sistemagestion.repositorios;

import com.biblioteca.sistemagestion.modelo.Libro;
import com.biblioteca.sistemagestion.modelo.EstadoLibro;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LibroRepositoryImplTest {

    private LibroRepository libroRepository;

    private Libro libro1;
    private Libro libro2;

    @BeforeEach
    void setUp() {
        libroRepository = new LibroRepositoryImpl();
        libro1 = new Libro("978-111", "Libro de Test 1", "Autor Test 1");
        libro2 = new Libro("978-222", "Libro de Test 2", "Autor Test 2");
    }

    @Test
    @DisplayName("Guardar un libro nuevo asigna ID y lo almacena")
    void save_libroNuevo_asignaIdYAlmacena() {
        assertNull(libro1.getId(), "El ID del libro nuevo debe ser nulo antes de guardar.");

        Libro libroGuardado = libroRepository.save(libro1);

        assertNotNull(libroGuardado.getId(), "El ID del libro no debería ser nulo después de guardar.");
        assertEquals(libro1.getIsbn(), libroGuardado.getIsbn());
        Optional<Libro> libroEncontradoOpt = libroRepository.findById(libroGuardado.getId());
        assertTrue(libroEncontradoOpt.isPresent(), "El libro guardado debería encontrarse por ID.");
        assertEquals(libroGuardado, libroEncontradoOpt.get(), "El libro encontrado no es el mismo que se guardó.");
    }

    @Test
    @DisplayName("Guardar un libro existente actualiza sus datos")
    void save_libroExistente_actualizaDatos() {
        libroRepository.save(libro1);
        String nuevoTitulo = "Nuevo Título para Libro 1";
        libro1.setTitulo(nuevoTitulo); // Asumimos que Libro tiene setters para esto

        Libro libroActualizado = libroRepository.save(libro1);

        assertEquals(libro1.getId(), libroActualizado.getId(), "El ID no debería cambiar al actualizar.");
        assertEquals(nuevoTitulo, libroActualizado.getTitulo(), "El título debería haberse actualizado.");
        Optional<Libro> libroEncontradoOpt = libroRepository.findById(libro1.getId());
        assertTrue(libroEncontradoOpt.isPresent());
        assertEquals(nuevoTitulo, libroEncontradoOpt.get().getTitulo(), "El título en el repositorio no se actualizó.");
    }

    @Test
    @DisplayName("findById devuelve libro existente")
    void findById_existente_devuelveLibro() {
        libroRepository.save(libro1);
        Long idExistente = libro1.getId();

        Optional<Libro> libroOpt = libroRepository.findById(idExistente);

        assertTrue(libroOpt.isPresent(), "Debería encontrar el libro por un ID existente.");
        assertEquals(libro1, libroOpt.get(), "El libro encontrado no es el esperado.");
    }

    @Test
    @DisplayName("findById devuelve Optional vacío para ID no existente")
    void findById_noExistente_devuelveOptionalVacio() {
        Long idNoExistente = 999L;
        Optional<Libro> libroOpt = libroRepository.findById(idNoExistente);
        assertTrue(libroOpt.isEmpty(), "No debería encontrar libro con ID inexistente.");
    }

    @Test
    @DisplayName("findByIsbn devuelve libro existente")
    void findByIsbn_existente_devuelveLibro() {
        libroRepository.save(libro1);
        Optional<Libro> libroOpt = libroRepository.findByIsbn(libro1.getIsbn());
        assertTrue(libroOpt.isPresent());
        assertEquals(libro1, libroOpt.get());
    }

    @Test
    @DisplayName("findByIsbn devuelve Optional vacío para ISBN no existente")
    void findByIsbn_noExistente_devuelveOptionalVacio() {
        Optional<Libro> libroOpt = libroRepository.findByIsbn("ISBN-FALSO-123");
        assertTrue(libroOpt.isEmpty());
    }

    @Test
    @DisplayName("findAll devuelve todos los libros guardados")
    void findAll_conLibros_devuelveTodos() {
        libroRepository.save(libro1);
        libroRepository.save(libro2);

        List<Libro> todos = libroRepository.findAll();
        assertEquals(2, todos.size());
        assertTrue(todos.contains(libro1));
        assertTrue(todos.contains(libro2));
    }

    @Test
    @DisplayName("findAll devuelve lista vacía si no hay libros")
    void findAll_sinLibros_devuelveListaVacia() {
        List<Libro> todos = libroRepository.findAll();
        assertTrue(todos.isEmpty());
    }

    @Test
    @DisplayName("deleteById elimina el libro y existsById confirma")
    void deleteById_y_existsById_funcionan() {
        libroRepository.save(libro1);
        Long idAEliminar = libro1.getId();
        assertTrue(libroRepository.existsById(idAEliminar), "El libro debería existir antes de eliminarlo.");

        libroRepository.deleteById(idAEliminar);

        assertFalse(libroRepository.existsById(idAEliminar), "El libro no debería existir después de eliminarlo.");
        assertTrue(libroRepository.findById(idAEliminar).isEmpty(), "findById debería devolver vacío para libro eliminado.");
    }

    @Test
    @DisplayName("deleteById con ID no existente no lanza error")
    void deleteById_noExistente_noLanzaError() {
        assertDoesNotThrow(() -> {
            libroRepository.deleteById(999L);
        });
    }

    @Test
    @DisplayName("existsById devuelve false para ID no existente")
    void existsById_noExistente_devuelveFalse() {
        assertFalse(libroRepository.existsById(999L));
    }
}