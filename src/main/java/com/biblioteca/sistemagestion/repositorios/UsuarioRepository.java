package com.biblioteca.sistemagestion.repositorios;

import com.biblioteca.sistemagestion.modelo.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {

    Usuario save(Usuario usuario);

    Optional<Usuario> findById(Long id);

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

}