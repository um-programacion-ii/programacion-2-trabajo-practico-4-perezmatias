package com.biblioteca.sistemagestion.servicios;

import com.biblioteca.sistemagestion.modelo.Usuario;
import com.biblioteca.sistemagestion.excepciones.UsuarioNoEncontradoException;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    Usuario crearUsuario(Usuario usuario);

    Optional<Usuario> obtenerUsuarioPorId(Long id);

    Optional<Usuario> obtenerUsuarioPorEmail(String email);

    List<Usuario> obtenerTodosLosUsuarios();

    Usuario actualizarUsuario(Long id, Usuario usuarioDetails) throws UsuarioNoEncontradoException;

    void eliminarUsuario(Long id) throws UsuarioNoEncontradoException;
}