package com.biblioteca.sistemagestion.excepciones;

public class LibroNoEncontradoException extends RuntimeException {

    public LibroNoEncontradoException(String message) {
        super(message);
    }

    public LibroNoEncontradoException(Long id) {
        super("Libro no encontrado con ID: " + id);
    }

}