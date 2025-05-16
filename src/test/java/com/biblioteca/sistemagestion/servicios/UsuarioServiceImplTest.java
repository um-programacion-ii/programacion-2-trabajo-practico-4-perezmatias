package com.biblioteca.sistemagestion.servicios;

import com.biblioteca.sistemagestion.modelo.Usuario;
import com.biblioteca.sistemagestion.modelo.EstadoUsuario;
import com.biblioteca.sistemagestion.repositorios.UsuarioRepository;
import com.biblioteca.sistemagestion.excepciones.UsuarioNoEncontradoException;
import com.biblioteca.sistemagestion.excepciones.RecursoDuplicadoException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepositoryMock;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuarioPrueba;
    private Usuario usuarioExistente;

    @BeforeEach
    void setUp() {
        usuarioPrueba = new Usuario("Usuario Nuevo", "nuevo@example.com");
        usuarioExistente = new Usuario("Usuario Existente", "existente@example.com");
        usuarioExistente.setId(1L);
        usuarioExistente.setEstado(EstadoUsuario.ACTIVO);
    }


}