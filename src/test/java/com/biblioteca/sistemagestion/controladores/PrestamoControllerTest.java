package com.biblioteca.sistemagestion.controladores;

import com.biblioteca.sistemagestion.dtos.PrestamoRequestDTO;
import com.biblioteca.sistemagestion.modelo.Libro;
import com.biblioteca.sistemagestion.modelo.Usuario;
import com.biblioteca.sistemagestion.modelo.Prestamo;
import com.biblioteca.sistemagestion.modelo.EstadoLibro;
import com.biblioteca.sistemagestion.modelo.EstadoUsuario;
import com.biblioteca.sistemagestion.servicios.PrestamoService;
import com.biblioteca.sistemagestion.excepciones.LibroNoEncontradoException;
import com.biblioteca.sistemagestion.excepciones.UsuarioNoEncontradoException;
import com.biblioteca.sistemagestion.excepciones.RecursoNoDisponibleException;
import com.biblioteca.sistemagestion.excepciones.PrestamoNoEncontradoException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(PrestamoController.class)
class PrestamoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrestamoService prestamoServiceMock;

    @Autowired
    private ObjectMapper objectMapper;

    private Libro libro1;
    private Usuario usuario1;
    private Prestamo prestamo1;
    private PrestamoRequestDTO prestamoRequestValido;
    private LocalDate fechaDevolucion;
    private Long libroId = 1L;
    private Long usuarioId = 1L;
    private Long prestamoId = 100L;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();

        libro1 = new Libro("ISBN-P001", "Libro para Préstamo Test", "Autor P1 Test");
        libro1.setId(libroId);
        libro1.setEstado(EstadoLibro.DISPONIBLE);

        usuario1 = new Usuario("Usuario Préstamo Test", "uprestamotest@example.com");
        usuario1.setId(usuarioId);
        usuario1.setEstado(EstadoUsuario.ACTIVO);

        fechaDevolucion = LocalDate.now().plusDays(14);
        prestamoRequestValido = new PrestamoRequestDTO(libro1.getId(), usuario1.getId(), fechaDevolucion);

        prestamo1 = new Prestamo(prestamoId, libro1, usuario1, LocalDate.now().minusDays(1), fechaDevolucion);
    }

    @Test
    @DisplayName("POST /api/prestamos con datos válidos devuelve Prestamo y status 201")
    void realizarPrestamo_ConDatosValidos_DevuelvePrestamoYStatus201() throws Exception {
        when(prestamoServiceMock.realizarPrestamo(eq(libroId), eq(usuarioId), eq(fechaDevolucion)))
                .thenReturn(prestamo1);

        mockMvc.perform(post("/api/prestamos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prestamoRequestValido)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(prestamoId.intValue())))
                .andExpect(jsonPath("$.libro.id", is(libroId.intValue())))
                .andExpect(jsonPath("$.usuario.id", is(usuarioId.intValue())))
                .andExpect(header().string("Location", containsString("/api/prestamos/" + prestamoId)));

        verify(prestamoServiceMock).realizarPrestamo(eq(libroId), eq(usuarioId), eq(fechaDevolucion));
    }

    @Test
    @DisplayName("POST /api/prestamos con libro no encontrado devuelve status 404")
    void realizarPrestamo_CuandoLibroNoEncontrado_DevuelveStatus404() throws Exception {
        when(prestamoServiceMock.realizarPrestamo(eq(999L), eq(usuarioId), eq(fechaDevolucion)))
                .thenThrow(new LibroNoEncontradoException(999L));

        PrestamoRequestDTO requestConLibroInexistente = new PrestamoRequestDTO(999L, usuarioId, fechaDevolucion);

        mockMvc.perform(post("/api/prestamos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestConLibroInexistente)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/prestamos con usuario no encontrado devuelve status 404")
    void realizarPrestamo_CuandoUsuarioNoEncontrado_DevuelveStatus404() throws Exception {
        when(prestamoServiceMock.realizarPrestamo(eq(libroId), eq(999L), eq(fechaDevolucion)))
                .thenThrow(new UsuarioNoEncontradoException(999L));

        PrestamoRequestDTO requestConUsuarioInexistente = new PrestamoRequestDTO(libroId, 999L, fechaDevolucion);

        mockMvc.perform(post("/api/prestamos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestConUsuarioInexistente)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/prestamos con recurso no disponible devuelve status 400")
    void realizarPrestamo_CuandoRecursoNoDisponible_DevuelveStatus400() throws Exception {
        when(prestamoServiceMock.realizarPrestamo(eq(libroId), eq(usuarioId), eq(fechaDevolucion)))
                .thenThrow(new RecursoNoDisponibleException("El libro no está disponible."));

        mockMvc.perform(post("/api/prestamos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prestamoRequestValido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/prestamos/{id}/devolver para préstamo existente devuelve status 200")
    void registrarDevolucion_CuandoPrestamoExiste_DevuelvePrestamoYStatus200() throws Exception {
        when(prestamoServiceMock.registrarDevolucion(prestamoId)).thenReturn(prestamo1);

        mockMvc.perform(post("/api/prestamos/" + prestamoId + "/devolver"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(prestamoId.intValue())));

        verify(prestamoServiceMock).registrarDevolucion(prestamoId);
    }

    @Test
    @DisplayName("POST /api/prestamos/{id}/devolver para préstamo no existente devuelve status 404")
    void registrarDevolucion_CuandoPrestamoNoExiste_DevuelveStatus404() throws Exception {
        when(prestamoServiceMock.registrarDevolucion(999L))
                .thenThrow(new PrestamoNoEncontradoException(999L));

        mockMvc.perform(post("/api/prestamos/999/devolver"))
                .andExpect(status().isNotFound());

        verify(prestamoServiceMock).registrarDevolucion(999L);
    }

    @Test
    @DisplayName("GET /api/prestamos devuelve lista de préstamos y status 200")
    void obtenerTodosLosPrestamos_DevuelveLista() throws Exception {
        List<Prestamo> listaPrestamos = List.of(prestamo1);
        when(prestamoServiceMock.obtenerTodosLosPrestamos()).thenReturn(listaPrestamos);

        mockMvc.perform(get("/api/prestamos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(prestamo1.getId().intValue())));

        verify(prestamoServiceMock).obtenerTodosLosPrestamos();
    }

    @Test
    @DisplayName("GET /api/prestamos/{id} devuelve préstamo y status 200 cuando existe")
    void obtenerPrestamoPorId_CuandoExiste_DevuelvePrestamoYStatus200() throws Exception {
        when(prestamoServiceMock.obtenerPrestamoPorId(prestamoId)).thenReturn(Optional.of(prestamo1));

        mockMvc.perform(get("/api/prestamos/" + prestamoId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(prestamoId.intValue())));

        verify(prestamoServiceMock).obtenerPrestamoPorId(prestamoId);
    }

    @Test
    @DisplayName("GET /api/prestamos/{id} devuelve status 404 cuando no existe")
    void obtenerPrestamoPorId_CuandoNoExiste_DevuelveStatus404() throws Exception {
        when(prestamoServiceMock.obtenerPrestamoPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/prestamos/999"))
                .andExpect(status().isNotFound());

        verify(prestamoServiceMock).obtenerPrestamoPorId(999L);
    }

    @Test
    @DisplayName("GET /api/prestamos/usuario/{id} devuelve préstamos del usuario")
    void obtenerPrestamosPorUsuario_CuandoExisten_DevuelveListaYStatus200() throws Exception {
        List<Prestamo> listaPrestamos = List.of(prestamo1);
        when(prestamoServiceMock.obtenerPrestamosPorUsuario(usuarioId)).thenReturn(listaPrestamos);

        mockMvc.perform(get("/api/prestamos/usuario/" + usuarioId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].usuario.id", is(usuarioId.intValue())));

        verify(prestamoServiceMock).obtenerPrestamosPorUsuario(usuarioId);
    }

    @Test
    @DisplayName("GET /api/prestamos/libro/{id} devuelve préstamos del libro")
    void obtenerPrestamosPorLibro_CuandoExisten_DevuelveListaYStatus200() throws Exception {
        List<Prestamo> listaPrestamos = List.of(prestamo1);
        when(prestamoServiceMock.obtenerPrestamosPorLibro(libroId)).thenReturn(listaPrestamos);

        mockMvc.perform(get("/api/prestamos/libro/" + libroId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].libro.id", is(libroId.intValue())));

        verify(prestamoServiceMock).obtenerPrestamosPorLibro(libroId);
    }
}