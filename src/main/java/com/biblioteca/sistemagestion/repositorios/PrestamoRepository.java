package com.biblioteca.sistemagestion.repositorios;

import com.biblioteca.sistemagestion.modelo.Prestamo;
import java.util.List;
import java.util.Optional;

public interface PrestamoRepository {

    Prestamo save(Prestamo prestamo);

    Optional<Prestamo> findById(Long id);

    List<Prestamo> findAll();

    List<Prestamo> findByUsuarioId(Long usuarioId);

    List<Prestamo> findByLibroId(Long libroId);

    Optional<Prestamo> findActiveByLibroId(Long libroId);


    void deleteById(Long id);

}