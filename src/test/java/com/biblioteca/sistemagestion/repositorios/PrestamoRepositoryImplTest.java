package com.biblioteca.sistemagestion.repositorios;

import com.biblioteca.sistemagestion.modelo.Libro;
import com.biblioteca.sistemagestion.modelo.Usuario;
import com.biblioteca.sistemagestion.modelo.Prestamo;
import com.biblioteca.sistemagestion.modelo.EstadoLibro;
import com.biblioteca.sistemagestion.modelo.EstadoUsuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PrestamoRepositoryImplTest {

    private PrestamoRepository prestamoRepository;

    private Libro libro1, libro2;
    private Usuario usuario1, usuario2;
    private Prestamo prestamo1_u1_l1;

    @BeforeEach
    void setUp() {
        prestamoRepository = new PrestamoRepositoryImpl();

        libro1 = new Libro("ISBN-001", "Libro Uno Para Prestamo", "Autor L1");
        libro1.setId(1L);
        libro1.setEstado(EstadoLibro.DISPONIBLE);

        libro2 = new Libro("ISBN-002", "Libro Dos Para Prestamo", "Autor L2");
        libro2.setId(2L);
        libro2.setEstado(EstadoLibro.DISPONIBLE);

        usuario1 = new Usuario("Usuario Uno Prestamo", "u1p@example.com");
        usuario1.setId(1L);
        usuario1.setEstado(EstadoUsuario.ACTIVO);


        usuario2 = new Usuario("Usuario Dos Prestamo", "u2p@example.com");
        usuario2.setId(2L);
        usuario2.setEstado(EstadoUsuario.ACTIVO);

        prestamo1_u1_l1 = new Prestamo(libro1, usuario1, LocalDate.now().plusDays(14));
    }

    @Test
    @DisplayName("Guardar un préstamo nuevo asigna ID y lo almacena")
    void save_prestamoNuevo_asignaIdYAlmacena() {
        assertNull(prestamo1_u1_l1.getId(), "El ID del préstamo nuevo debe ser nulo antes de guardar.");

        Prestamo prestamoGuardado = prestamoRepository.save(prestamo1_u1_l1);

        assertNotNull(prestamoGuardado.getId(), "El ID del préstamo no debería ser nulo después de guardar.");
        assertEquals(prestamo1_u1_l1.getLibro().getIsbn(), prestamoGuardado.getLibro().getIsbn());
        Optional<Prestamo> prestamoEncontradoOpt = prestamoRepository.findById(prestamoGuardado.getId());
        assertTrue(prestamoEncontradoOpt.isPresent(), "El préstamo guardado debería encontrarse por ID.");
        assertEquals(prestamoGuardado, prestamoEncontradoOpt.get());
    }

    @Test
    @DisplayName("Guardar un préstamo existente actualiza sus datos (ej. fechaDevolucion)")
    void save_prestamoExistente_actualizaDatos() {
        prestamoRepository.save(prestamo1_u1_l1);
        LocalDate nuevaFechaDevolucion = LocalDate.now().plusDays(20);
        prestamo1_u1_l1.setFechaDevolucion(nuevaFechaDevolucion);

        Prestamo prestamoActualizado = prestamoRepository.save(prestamo1_u1_l1);

        assertEquals(prestamo1_u1_l1.getId(), prestamoActualizado.getId());
        assertEquals(nuevaFechaDevolucion, prestamoActualizado.getFechaDevolucion());
    }


    @Test
    @DisplayName("findById devuelve préstamo existente")
    void findById_existente_devuelvePrestamo() {
        prestamoRepository.save(prestamo1_u1_l1);
        Long idExistente = prestamo1_u1_l1.getId();

        Optional<Prestamo> prestamoOpt = prestamoRepository.findById(idExistente);

        assertTrue(prestamoOpt.isPresent());
        assertEquals(prestamo1_u1_l1, prestamoOpt.get());
    }

    @Test
    @DisplayName("findById devuelve Optional vacío para ID no existente")
    void findById_noExistente_devuelveOptionalVacio() {
        Optional<Prestamo> prestamoOpt = prestamoRepository.findById(999L);
        assertTrue(prestamoOpt.isEmpty());
    }

}