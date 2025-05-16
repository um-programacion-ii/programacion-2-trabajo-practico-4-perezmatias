package com.biblioteca.sistemagestion.repositorios;

import com.biblioteca.sistemagestion.modelo.Libro;
import java.util.List;
import java.util.Optional;

public interface LibroRepository {

    Libro save(Libro libro);

    Optional<Libro> findById(Long id);

    Optional<Libro> findByIsbn(String isbn);

    List<Libro> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

}