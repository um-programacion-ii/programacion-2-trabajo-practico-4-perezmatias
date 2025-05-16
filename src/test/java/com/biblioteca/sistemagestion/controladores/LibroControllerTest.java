package com.biblioteca.sistemagestion.controladores;

import com.biblioteca.sistemagestion.modelo.Libro;
import com.biblioteca.sistemagestion.modelo.EstadoLibro;
import com.biblioteca.sistemagestion.servicios.LibroService;
import com.biblioteca.sistemagestion.excepciones.LibroNoEncontradoException;
import com.biblioteca.sistemagestion.excepciones.RecursoDuplicadoException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;


@WebMvcTest(LibroController.class)
class LibroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibroService libroServiceMock;

    @Autowired
    private ObjectMapper objectMapper;

    private Libro libro1;
    private Libro libro2;

    @BeforeEach
    void setUp() {
        libro1 = new Libro("ISBN001", "Libro de Aventuras", "Autor A");
        libro1.setId(1L);
        libro1.setEstado(EstadoLibro.DISPONIBLE);

        libro2 = new Libro("ISBN002", "Libro de Ciencia", "Autor B");
        libro2.setId(2L);
        libro2.setEstado(EstadoLibro.PRESTADO);
    }

    @Test
    @DisplayName("GET /api/libros devuelve lista de libros y status 200")
    void obtenerTodosLosLibros_DevuelveListaDeLibros_Y_Status200() throws Exception {
        List<Libro> listaLibros = List.of(libro1, libro2);
        when(libroServiceMock.obtenerTodosLosLibros()).thenReturn(listaLibros);

        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].isbn", is(libro1.getIsbn())))
                .andExpect(jsonPath("$[1].isbn", is(libro2.getIsbn())));

        verify(libroServiceMock).obtenerTodosLosLibros();
    }

    @Test
    @DisplayName("GET /api/libros devuelve lista vacía y status 200 si no hay libros")
    void obtenerTodosLosLibros_DevuelveListaVacia_CuandoNoHayLibros() throws Exception {
        when(libroServiceMock.obtenerTodosLosLibros()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(libroServiceMock).obtenerTodosLosLibros();
    }

    @Test
    @DisplayName("GET /api/libros/{id} devuelve libro y status 200 cuando libro existe")
    void obtenerLibroPorId_CuandoLibroExiste_DevuelveLibroYStatus200() throws Exception {
        when(libroServiceMock.obtenerLibroPorId(1L)).thenReturn(Optional.of(libro1));

        mockMvc.perform(get("/api/libros/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.isbn", is(libro1.getIsbn())));

        verify(libroServiceMock).obtenerLibroPorId(1L);
    }

    @Test
    @DisplayName("GET /api/libros/{id} devuelve status 404 cuando libro no existe")
    void obtenerLibroPorId_CuandoLibroNoExiste_DevuelveStatus404() throws Exception {
        when(libroServiceMock.obtenerLibroPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/libros/999"))
                .andExpect(status().isNotFound());

        verify(libroServiceMock).obtenerLibroPorId(999L);
    }

    @Test
    @DisplayName("POST /api/libros crea libro y devuelve status 201")
    void crearLibro_ConDatosValidos_DevuelveLibroCreadoYStatus201() throws Exception {
        Libro libroACrear = new Libro("ISBN-NEW", "Nuevo Libro", "Nuevo Autor");
        Libro libroCreadoConId = new Libro("ISBN-NEW", "Nuevo Libro", "Nuevo Autor");
        libroCreadoConId.setId(3L);
        libroCreadoConId.setEstado(EstadoLibro.DISPONIBLE);

        when(libroServiceMock.crearLibro(Mockito.any(Libro.class))).thenReturn(libroCreadoConId);

        mockMvc.perform(post("/api/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(libroACrear)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.isbn", is("ISBN-NEW")))
                .andExpect(header().string("Location", containsString("/api/libros/3")));

        verify(libroServiceMock).crearLibro(Mockito.any(Libro.class));
    }

    @Test
    @DisplayName("POST /api/libros con ISBN duplicado devuelve status 409")
    void crearLibro_CuandoIsbnDuplicado_DevuelveStatus409() throws Exception {
        Libro libroConIsbnDuplicado = new Libro(libro1.getIsbn(), "Otro Libro", "Otro Autor");

        when(libroServiceMock.crearLibro(Mockito.any(Libro.class)))
                .thenThrow(new RecursoDuplicadoException("Ya existe un libro con el ISBN: " + libro1.getIsbn()));

        mockMvc.perform(post("/api/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(libroConIsbnDuplicado)))
                .andExpect(status().isConflict());

        verify(libroServiceMock).crearLibro(Mockito.any(Libro.class));
    }


    @Test
    @DisplayName("PUT /api/libros/{id} actualiza libro y devuelve status 200")
    void actualizarLibro_CuandoLibroExiste_DevuelveLibroActualizadoYStatus200() throws Exception {
        Long idExistente = 1L;
        Libro datosActualizacion = new Libro(libro1.getIsbn(), "Título Actualizado", "Autor Actualizado");
        datosActualizacion.setEstado(EstadoLibro.EN_REPARACION);

        Libro libroActualizado = new Libro(libro1.getIsbn(), "Título Actualizado", "Autor Actualizado");
        libroActualizado.setId(idExistente);
        libroActualizado.setEstado(EstadoLibro.EN_REPARACION);

        when(libroServiceMock.actualizarLibro(eq(idExistente), Mockito.any(Libro.class))).thenReturn(libroActualizado);

        mockMvc.perform(put("/api/libros/" + idExistente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datosActualizacion)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.titulo", is("Título Actualizado")));

        verify(libroServiceMock).actualizarLibro(eq(idExistente), Mockito.any(Libro.class));
    }

    @Test
    @DisplayName("PUT /api/libros/{id} devuelve status 404 cuando libro no existe")
    void actualizarLibro_CuandoLibroNoExiste_DevuelveStatus404() throws Exception {
        Long idNoExistente = 999L;
        Libro datosActualizacion = new Libro("ISBN-DUMMY", "Dummy", "Dummy");

        when(libroServiceMock.actualizarLibro(eq(idNoExistente), Mockito.any(Libro.class)))
                .thenThrow(new LibroNoEncontradoException(idNoExistente));

        mockMvc.perform(put("/api/libros/" + idNoExistente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datosActualizacion)))
                .andExpect(status().isNotFound());

        verify(libroServiceMock).actualizarLibro(eq(idNoExistente), Mockito.any(Libro.class));
    }

    @Test
    @DisplayName("DELETE /api/libros/{id} elimina libro y devuelve status 204")
    void eliminarLibro_CuandoLibroExiste_DevuelveStatus204() throws Exception {
        Long idExistente = 1L;
        doNothing().when(libroServiceMock).eliminarLibro(idExistente);

        mockMvc.perform(delete("/api/libros/" + idExistente))
                .andExpect(status().isNoContent());

        verify(libroServiceMock).eliminarLibro(idExistente);
    }

    @Test
    @DisplayName("DELETE /api/libros/{id} devuelve status 404 cuando libro no existe")
    void eliminarLibro_CuandoLibroNoExiste_DevuelveStatus404() throws Exception {
        Long idNoExistente = 999L;
        doThrow(new LibroNoEncontradoException(idNoExistente)).when(libroServiceMock).eliminarLibro(idNoExistente);

        mockMvc.perform(delete("/api/libros/" + idNoExistente))
                .andExpect(status().isNotFound());

        verify(libroServiceMock).eliminarLibro(idNoExistente);
    }
}