package com.biblioteca.sistemagestion.servicios;

import com.biblioteca.sistemagestion.modelo.Prestamo;
import com.biblioteca.sistemagestion.excepciones.LibroNoEncontradoException;
import com.biblioteca.sistemagestion.excepciones.UsuarioNoEncontradoException;
import com.biblioteca.sistemagestion.excepciones.RecursoNoDisponibleException;
import com.biblioteca.sistemagestion.excepciones.PrestamoNoEncontradoException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PrestamoService {

    Prestamo realizarPrestamo(Long libroId, Long usuarioId, LocalDate fechaDevolucion)
            throws LibroNoEncontradoException, UsuarioNoEncontradoException, RecursoNoDisponibleException;

    Prestamo registrarDevolucion(Long prestamoId) throws PrestamoNoEncontradoException;

    Optional<Prestamo> obtenerPrestamoPorId(Long prestamoId);

    List<Prestamo> obtenerTodosLosPrestamos();

    List<Prestamo> obtenerPrestamosPorUsuario(Long usuarioId);

    List<Prestamo> obtenerPrestamosPorLibro(Long libroId);

}