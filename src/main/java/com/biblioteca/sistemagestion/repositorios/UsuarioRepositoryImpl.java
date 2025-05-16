package com.biblioteca.sistemagestion.repositorios;

import com.biblioteca.sistemagestion.modelo.Usuario;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final Map<Long, Usuario> usuarios = new HashMap<>();
    private final AtomicLong sequenceGenerator = new AtomicLong(1);

    @Override
    public Usuario save(Usuario usuario) {
        Objects.requireNonNull(usuario, "El usuario no puede ser nulo.");
        if (usuario.getId() == null) {
            usuario.setId(sequenceGenerator.getAndIncrement());
        }
        usuarios.put(usuario.getId(), usuario);
        return usuario;
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo.");
        return Optional.ofNullable(usuarios.get(id));
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        Objects.requireNonNull(email, "El email no puede ser nulo.");
        return usuarios.values().stream()
                .filter(usuario -> usuario.getEmail() != null && email.equalsIgnoreCase(usuario.getEmail()))
                .findFirst();
    }

    @Override
    public List<Usuario> findAll() {
        return new ArrayList<>(usuarios.values());
    }

    @Override
    public void deleteById(Long id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo para eliminar.");
        usuarios.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo para verificar existencia.");
        return usuarios.containsKey(id);
    }
}