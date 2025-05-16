package com.biblioteca.sistemagestion.excepciones;

public class UsuarioNoEncontradoException extends RuntimeException {

    public UsuarioNoEncontradoException(String message) {
        super(message);
    }

    public UsuarioNoEncontradoException(Long id) {
        super("Usuario no encontrado con ID: " + id);
    }

    public UsuarioNoEncontradoException(String identificador, String tipoIdentificador) {
        super("Usuario no encontrado con " + tipoIdentificador + ": " + identificador);
    }
}