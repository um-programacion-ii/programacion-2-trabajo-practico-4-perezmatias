package com.biblioteca.sistemagestion.servicios;

import com.biblioteca.sistemagestion.modelo.Libro;
import com.biblioteca.sistemagestion.modelo.Prestamo;
import com.biblioteca.sistemagestion.modelo.Usuario;
import com.biblioteca.sistemagestion.modelo.EstadoLibro;
import com.biblioteca.sistemagestion.repositorios.LibroRepository;
import com.biblioteca.sistemagestion.repositorios.PrestamoRepository;
import com.biblioteca.sistemagestion.repositorios.UsuarioRepository;
import com.biblioteca.sistemagestion.excepciones.LibroNoEncontradoException;
import com.biblioteca.sistemagestion.excepciones.UsuarioNoEncontradoException;
import com.biblioteca.sistemagestion.excepciones.RecursoNoDisponibleException;
import com.biblioteca.sistemagestion.excepciones.PrestamoNoEncontradoException;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;

    public PrestamoServiceImpl(PrestamoRepository prestamoRepository,
                               LibroRepository libroRepository,
                               UsuarioRepository usuarioRepository) {
        this.prestamoRepository = Objects.requireNonNull(prestamoRepository, "PrestamoRepository no puede ser nulo.");
        this.libroRepository = Objects.requireNonNull(libroRepository, "LibroRepository no puede ser nulo.");
        this.usuarioRepository = Objects.requireNonNull(usuarioRepository, "UsuarioRepository no puede ser nulo.");
    }

    @Override
    public Prestamo realizarPrestamo(Long libroId, Long usuarioId, LocalDate fechaDevolucionSugerida)
            throws LibroNoEncontradoException, UsuarioNoEncontradoException, RecursoNoDisponibleException {

        Objects.requireNonNull(libroId, "ID de libro no puede ser nulo.");
        Objects.requireNonNull(usuarioId, "ID de usuario no puede ser nulo.");
        Objects.requireNonNull(fechaDevolucionSugerida, "Fecha de devolución sugerida no puede ser nula.");

        if (fechaDevolucionSugerida.isBefore(LocalDate.now().plusDays(1)) && !fechaDevolucionSugerida.isEqual(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de devolución debe ser hoy o una fecha futura.");
        }

        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new LibroNoEncontradoException(libroId));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(usuarioId));

        if (libro.getEstado() != EstadoLibro.DISPONIBLE) {
            throw new RecursoNoDisponibleException("El libro '" + libro.getTitulo() + "' (ID: " + libroId
                    + ") no está disponible para préstamo. Estado actual: " + libro.getEstado());
        }

        libro.setEstado(EstadoLibro.PRESTADO);
        libroRepository.save(libro);

        Prestamo nuevoPrestamo = new Prestamo(libro, usuario, fechaDevolucionSugerida);

        return prestamoRepository.save(nuevoPrestamo);
    }

    @Override
    public Prestamo registrarDevolucion(Long prestamoId) throws PrestamoNoEncontradoException {
        Objects.requireNonNull(prestamoId, "ID de préstamo no puede ser nulo.");

        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new PrestamoNoEncontradoException(prestamoId));

        Libro libroPrestado = prestamo.getLibro();
        if (libroPrestado == null) {

            throw new IllegalStateException("El préstamo con ID " + prestamoId + " no tiene un libro asociado.");
        }

        libroPrestado.setEstado(EstadoLibro.DISPONIBLE);
        libroRepository.save(libroPrestado);

        prestamoRepository.deleteById(prestamoId);

        return prestamo;
    }

    @Override
    public Optional<Prestamo> obtenerPrestamoPorId(Long prestamoId) {
        Objects.requireNonNull(prestamoId, "ID de préstamo no puede ser nulo.");
        return prestamoRepository.findById(prestamoId);
    }

    @Override
    public List<Prestamo> obtenerTodosLosPrestamos() {
        return prestamoRepository.findAll();
    }

    @Override
    public List<Prestamo> obtenerPrestamosPorUsuario(Long usuarioId) {
        Objects.requireNonNull(usuarioId, "ID de usuario no puede ser nulo.");
        return prestamoRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Prestamo> obtenerPrestamosPorLibro(Long libroId) {
        Objects.requireNonNull(libroId, "ID de libro no puede ser nulo.");
        return prestamoRepository.findByLibroId(libroId);
    }
}