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
    private Libro libroPrestado;
    private Usuario usuarioActivo;
    private Prestamo prestamoActivo;
    private Long libroId = 1L;
    private Long usuarioId = 1L;
    private Long prestamoId = 1L;
    private LocalDate fechaDevolucionSugerida;


    @BeforeEach
    void setUp() {
        libroDisponible = new Libro("ISBN-DISP", "Libro Disponible", "Autor Disp");
        libroDisponible.setId(libroId);
        libroDisponible.setEstado(EstadoLibro.DISPONIBLE);

        libroPrestado = new Libro("ISBN-PREST", "Libro Ya Prestado", "Autor Prest");
        libroPrestado.setId(2L);
        libroPrestado.setEstado(EstadoLibro.PRESTADO);

        usuarioActivo = new Usuario("Usuario Test Préstamo", "u.prestamo@example.com");
        usuarioActivo.setId(usuarioId);
        usuarioActivo.setEstado(EstadoUsuario.ACTIVO);

        fechaDevolucionSugerida = LocalDate.now().plusDays(14);

        prestamoActivo = new Prestamo(libroPrestado, usuarioActivo, LocalDate.now().minusDays(5), fechaDevolucionSugerida);
        prestamoActivo.setId(prestamoId);
    }

}