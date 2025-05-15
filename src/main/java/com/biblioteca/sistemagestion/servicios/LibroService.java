package com.biblioteca.sistemagestion.servicios;

import com.biblioteca.sistemagestion.modelo.Libro;
import com.biblioteca.sistemagestion.excepciones.LibroNoEncontradoException;

import java.util.List;
import java.util.Optional;

public interface LibroService {

    Libro crearLibro(Libro libro);

    Optional<Libro> obtenerLibroPorId(Long id);

    Optional<Libro> obtenerLibroPorIsbn(String isbn);

    List<Libro> obtenerTodosLosLibros();

    Libro actualizarLibro(Long id, Libro libroDetails) throws LibroNoEncontradoException;

    void eliminarLibro(Long id) throws LibroNoEncontradoException;
}