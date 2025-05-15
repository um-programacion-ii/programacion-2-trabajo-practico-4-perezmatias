package com.biblioteca.sistemagestion.repositorios;

import com.biblioteca.sistemagestion.modelo.Libro;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class LibroRepositoryImpl implements LibroRepository {

    private final Map<Long, Libro> libros = new HashMap<>();

    private final AtomicLong sequenceGenerator = new AtomicLong(1);

    @Override
    public Libro save(Libro libro) {
        Objects.requireNonNull(libro, "El libro no puede ser nulo.");

        if (libro.getId() == null) {
            libro.setId(sequenceGenerator.getAndIncrement());
        }

        libros.put(libro.getId(), libro);
        return libro;
    }

    @Override
    public Optional<Libro> findById(Long id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo.");
        return Optional.ofNullable(libros.get(id));
    }

    @Override
    public Optional<Libro> findByIsbn(String isbn) {
        Objects.requireNonNull(isbn, "El ISBN no puede ser nulo.");
        return libros.values().stream()
                .filter(libro -> isbn.equals(libro.getIsbn()))
                .findFirst();
    }

    @Override
    public List<Libro> findAll() {
        return new ArrayList<>(libros.values());
    }

    @Override
    public void deleteById(Long id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo para eliminar.");
        libros.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo para verificar existencia.");
        return libros.containsKey(id);
    }
}