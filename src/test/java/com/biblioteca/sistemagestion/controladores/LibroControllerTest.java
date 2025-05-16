package com.biblioteca.sistemagestion.controladores;

import com.biblioteca.sistemagestion.modelo.Libro;
import com.biblioteca.sistemagestion.modelo.EstadoLibro;
import com.biblioteca.sistemagestion.servicios.LibroService;
import com.biblioteca.sistemagestion.excepciones.LibroNoEncontradoException;
import com.biblioteca.sistemagestion.excepciones.RecursoDuplicadoException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*; // Para jsonPath
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

}