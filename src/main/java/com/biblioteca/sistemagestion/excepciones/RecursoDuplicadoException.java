package com.biblioteca.sistemagestion.excepciones;

public class RecursoDuplicadoException extends RuntimeException {
    public RecursoDuplicadoException(String message) {
        super(message);
    }
}