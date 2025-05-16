package com.biblioteca.sistemagestion.excepciones;

public class PrestamoNoEncontradoException extends RuntimeException {
    public PrestamoNoEncontradoException(String message) {
        super(message);
    }

    public PrestamoNoEncontradoException(Long id) {
        super("Préstamo no encontrado con ID: " + id);
    }
}