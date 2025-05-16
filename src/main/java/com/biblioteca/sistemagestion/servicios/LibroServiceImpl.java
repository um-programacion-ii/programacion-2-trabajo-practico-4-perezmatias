package com.biblioteca.sistemagestion.servicios;

import com.biblioteca.sistemagestion.modelo.Libro;
import com.biblioteca.sistemagestion.modelo.EstadoLibro;
import com.biblioteca.sistemagestion.repositorios.LibroRepository;
import com.biblioteca.sistemagestion.excepciones.LibroNoEncontradoException;
import com.biblioteca.sistemagestion.excepciones.RecursoDuplicadoException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;

    public LibroServiceImpl(LibroRepository libroRepository) {
        this.libroRepository = Objects.requireNonNull(libroRepository, "LibroRepository no puede ser nulo.");
    }

    @Override
    public Libro crearLibro(Libro libro) throws RecursoDuplicadoException, IllegalArgumentException {
        Objects.requireNonNull(libro, "Los detalles del libro no pueden ser nulos.");
        if (libro.getId() != null) {
            throw new IllegalArgumentException("El ID debe ser nulo para un nuevo libro, es autogenerado.");
        }
        Objects.requireNonNull(libro.getIsbn(), "El ISBN no puede ser nulo.");
        Objects.requireNonNull(libro.getTitulo(), "El título no puede ser nulo.");
        Objects.requireNonNull(libro.getAutor(), "El autor no puede ser nulo.");
        if (libro.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("El ISBN no puede estar vacío.");
        }
        if (libro.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        if (libro.getAutor().trim().isEmpty()) {
            throw new IllegalArgumentException("El autor no puede estar vacío.");
        }
        if (libroRepository.findByIsbn(libro.getIsbn()).isPresent()) {
            throw new RecursoDuplicadoException("Ya existe un libro con el ISBN: " + libro.getIsbn());
        }
        if (libro.getEstado() == null) {
            libro.setEstado(EstadoLibro.DISPONIBLE);
        }
        return libroRepository.save(libro);
    }

    @Override
    public Optional<Libro> obtenerLibroPorId(Long id) {
        Objects.requireNonNull(id, "El ID del libro no puede ser nulo.");
        return libroRepository.findById(id);
    }

    @Override
    public Optional<Libro> obtenerLibroPorIsbn(String isbn) {
        Objects.requireNonNull(isbn, "El ISBN no puede ser nulo.");
        return libroRepository.findByIsbn(isbn);
    }

    @Override
    public List<Libro> obtenerTodosLosLibros() {
        return libroRepository.findAll();
    }

    @Override
    public Libro actualizarLibro(Long id, Libro libroDetails) throws LibroNoEncontradoException {
        Objects.requireNonNull(id, "El ID del libro a actualizar no puede ser nulo.");
        Objects.requireNonNull(libroDetails, "Los detalles del libro para actualizar no pueden ser nulos.");

        Libro libroExistente = libroRepository.findById(id)
                .orElseThrow(() -> new LibroNoEncontradoException(id));

        libroExistente.setTitulo(libroDetails.getTitulo());
        libroExistente.setAutor(libroDetails.getAutor());
        libroExistente.setEstado(libroDetails.getEstado());

        return libroRepository.save(libroExistente);
    }

    @Override
    public void eliminarLibro(Long id) throws LibroNoEncontradoException {
        Objects.requireNonNull(id, "El ID del libro a eliminar no puede ser nulo.");
        if (!libroRepository.existsById(id)) {
            throw new LibroNoEncontradoException(id);
        }
        libroRepository.deleteById(id);
    }
}