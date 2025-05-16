package com.biblioteca.sistemagestion.repositorios;

import com.biblioteca.sistemagestion.modelo.Prestamo;
import com.biblioteca.sistemagestion.modelo.Libro;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class PrestamoRepositoryImpl implements PrestamoRepository {

    private final Map<Long, Prestamo> prestamos = new HashMap<>();
    private final AtomicLong sequenceGenerator = new AtomicLong(1);

    @Override
    public Prestamo save(Prestamo prestamo) {
        Objects.requireNonNull(prestamo, "El préstamo no puede ser nulo.");
        if (prestamo.getId() == null) {
            prestamo.setId(sequenceGenerator.getAndIncrement());
        }
        prestamos.put(prestamo.getId(), prestamo);
        return prestamo;
    }

    @Override
    public Optional<Prestamo> findById(Long id) {
        Objects.requireNonNull(id, "El ID del préstamo no puede ser nulo.");
        return Optional.ofNullable(prestamos.get(id));
    }

    @Override
    public List<Prestamo> findAll() {
        return new ArrayList<>(prestamos.values());
    }

    @Override
    public List<Prestamo> findByUsuarioId(Long usuarioId) {
        Objects.requireNonNull(usuarioId, "El ID de usuario no puede ser nulo.");
        return prestamos.values().stream()
                .filter(prestamo -> prestamo.getUsuario() != null && usuarioId.equals(prestamo.getUsuario().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Prestamo> findByLibroId(Long libroId) {
        Objects.requireNonNull(libroId, "El ID del libro no puede ser nulo.");
        return prestamos.values().stream()
                .filter(prestamo -> prestamo.getLibro() != null && libroId.equals(prestamo.getLibro().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Prestamo> findActiveByLibroId(Long libroId) {
        Objects.requireNonNull(libroId, "El ID del libro no puede ser nulo.");
        return prestamos.values().stream()
                .filter(prestamo -> prestamo.getLibro() != null && libroId.equals(prestamo.getLibro().getId()))
                .findFirst(); // Devuelve el primero (o único) préstamo para ese libro ID
    }


    @Override
    public void deleteById(Long id) {
        Objects.requireNonNull(id, "El ID del préstamo no puede ser nulo para eliminar.");
        prestamos.remove(id);
    }
}