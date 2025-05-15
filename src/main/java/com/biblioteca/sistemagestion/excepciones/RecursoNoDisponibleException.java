package com.biblioteca.sistemagestion.excepciones;

public class RecursoNoDisponibleException extends RuntimeException {
    public RecursoNoDisponibleException(String message) {
        super(message);
    }
}