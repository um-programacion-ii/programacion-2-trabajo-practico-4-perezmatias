package com.biblioteca.sistemagestion.web.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import com.biblioteca.sistemagestion.excepciones.LibroNoEncontradoException;
import com.biblioteca.sistemagestion.excepciones.UsuarioNoEncontradoException;
import com.biblioteca.sistemagestion.excepciones.PrestamoNoEncontradoException;
import com.biblioteca.sistemagestion.excepciones.RecursoDuplicadoException;
import com.biblioteca.sistemagestion.excepciones.RecursoNoDisponibleException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler {

    private Map<String, Object> crearCuerpoDeError(HttpStatus status, String mensaje, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", mensaje);
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return body;
    }

    @ExceptionHandler(LibroNoEncontradoException.class)
    public ResponseEntity<Object> handleLibroNoEncontrado(
            LibroNoEncontradoException ex, WebRequest request) {
        Map<String, Object> body = crearCuerpoDeError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<Object> handleUsuarioNoEncontrado(
            UsuarioNoEncontradoException ex, WebRequest request) {
        Map<String, Object> body = crearCuerpoDeError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PrestamoNoEncontradoException.class)
    public ResponseEntity<Object> handlePrestamoNoEncontrado(
            PrestamoNoEncontradoException ex, WebRequest request) {
        Map<String, Object> body = crearCuerpoDeError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<Object> handleRecursoDuplicado(
            RecursoDuplicadoException ex, WebRequest request) {
        Map<String, Object> body = crearCuerpoDeError(HttpStatus.CONFLICT, ex.getMessage(), request);
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RecursoNoDisponibleException.class)
    public ResponseEntity<Object> handleRecursoNoDisponible(
            RecursoNoDisponibleException ex, WebRequest request) {
        Map<String, Object> body = crearCuerpoDeError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {
        Map<String, Object> body = crearCuerpoDeError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}