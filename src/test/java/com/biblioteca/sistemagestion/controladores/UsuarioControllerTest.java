package com.biblioteca.sistemagestion.controladores;

import com.biblioteca.sistemagestion.modelo.Usuario;
import com.biblioteca.sistemagestion.modelo.EstadoUsuario;
import com.biblioteca.sistemagestion.servicios.UsuarioService;
import com.biblioteca.sistemagestion.excepciones.UsuarioNoEncontradoException;
import com.biblioteca.sistemagestion.excepciones.RecursoDuplicadoException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioServiceMock;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        usuario1 = new Usuario("Usuario Uno ControllerTest", "u1.controller@example.com");
        usuario1.setId(1L);
        usuario1.setEstado(EstadoUsuario.ACTIVO);

        usuario2 = new Usuario("Usuario Dos ControllerTest", "u2.controller@example.com");
        usuario2.setId(2L);
        usuario2.setEstado(EstadoUsuario.SUSPENDIDO);
    }

    @Test
    @DisplayName("GET /api/usuarios devuelve lista de usuarios y status 200")
    void obtenerTodosLosUsuarios_DevuelveListaDeUsuarios_Y_Status200() throws Exception {
        List<Usuario> listaUsuarios = List.of(usuario1, usuario2);
        when(usuarioServiceMock.obtenerTodosLosUsuarios()).thenReturn(listaUsuarios);

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email", is(usuario1.getEmail())))
                .andExpect(jsonPath("$[1].email", is(usuario2.getEmail())));

        verify(usuarioServiceMock).obtenerTodosLosUsuarios();
    }

    @Test
    @DisplayName("GET /api/usuarios devuelve lista vacía y status 200 si no hay usuarios")
    void obtenerTodosLosUsuarios_DevuelveListaVacia_CuandoNoHayUsuarios() throws Exception {
        when(usuarioServiceMock.obtenerTodosLosUsuarios()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(usuarioServiceMock).obtenerTodosLosUsuarios();
    }

    @Test
    @DisplayName("GET /api/usuarios/{id} devuelve usuario y status 200 cuando usuario existe")
    void obtenerUsuarioPorId_CuandoUsuarioExiste_DevuelveUsuarioYStatus200() throws Exception {
        when(usuarioServiceMock.obtenerUsuarioPorId(1L)).thenReturn(Optional.of(usuario1));

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is(usuario1.getEmail())));

        verify(usuarioServiceMock).obtenerUsuarioPorId(1L);
    }

    @Test
    @DisplayName("GET /api/usuarios/{id} devuelve status 404 cuando usuario no existe")
    void obtenerUsuarioPorId_CuandoUsuarioNoExiste_DevuelveStatus404() throws Exception {
        when(usuarioServiceMock.obtenerUsuarioPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/999"))
                .andExpect(status().isNotFound());

        verify(usuarioServiceMock).obtenerUsuarioPorId(999L);
    }

    @Test
    @DisplayName("POST /api/usuarios crea usuario y devuelve status 201")
    void crearUsuario_ConDatosValidos_DevuelveUsuarioCreadoYStatus201() throws Exception {
        Usuario usuarioACrear = new Usuario("Usuario Nuevo API", "nuevo.api@example.com");

        Usuario usuarioCreadoConId = new Usuario("Usuario Nuevo API", "nuevo.api@example.com");
        usuarioCreadoConId.setId(3L);
        usuarioCreadoConId.setEstado(EstadoUsuario.ACTIVO);

        when(usuarioServiceMock.crearUsuario(Mockito.any(Usuario.class))).thenReturn(usuarioCreadoConId);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioACrear)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.email", is("nuevo.api@example.com")))
                .andExpect(header().string("Location", containsString("/api/usuarios/3")));

        verify(usuarioServiceMock).crearUsuario(Mockito.any(Usuario.class));
    }

    @Test
    @DisplayName("POST /api/usuarios con email duplicado devuelve status 409")
    void crearUsuario_CuandoEmailDuplicado_DevuelveStatus409() throws Exception {
        Usuario usuarioConEmailDuplicado = new Usuario("Otro Usuario", usuario1.getEmail());

        when(usuarioServiceMock.crearUsuario(Mockito.any(Usuario.class)))
                .thenThrow(new RecursoDuplicadoException("El email '" + usuario1.getEmail() + "' ya está registrado."));

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioConEmailDuplicado)))
                .andExpect(status().isConflict());

        verify(usuarioServiceMock).crearUsuario(Mockito.any(Usuario.class));
    }

    @Test
    @DisplayName("PUT /api/usuarios/{id} actualiza usuario y devuelve status 200")
    void actualizarUsuario_CuandoUsuarioExiste_DevuelveUsuarioActualizadoYStatus200() throws Exception {
        Long idExistente = 1L;
        Usuario datosActualizacion = new Usuario("Nombre Actualizado", "u1.actualizado@example.com");
        datosActualizacion.setEstado(EstadoUsuario.SUSPENDIDO);

        Usuario usuarioActualizado = new Usuario("Nombre Actualizado", "u1.actualizado@example.com");
        usuarioActualizado.setId(idExistente);
        usuarioActualizado.setEstado(EstadoUsuario.SUSPENDIDO);

        when(usuarioServiceMock.actualizarUsuario(eq(idExistente), Mockito.any(Usuario.class))).thenReturn(usuarioActualizado);

        mockMvc.perform(put("/api/usuarios/" + idExistente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datosActualizacion)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Nombre Actualizado")));

        verify(usuarioServiceMock).actualizarUsuario(eq(idExistente), Mockito.any(Usuario.class));
    }

    @Test
    @DisplayName("PUT /api/usuarios/{id} devuelve status 404 cuando usuario no existe")
    void actualizarUsuario_CuandoUsuarioNoExiste_DevuelveStatus404() throws Exception {
        Long idNoExistente = 999L;
        Usuario datosActualizacion = new Usuario("Dummy", "dummy@example.com");

        when(usuarioServiceMock.actualizarUsuario(eq(idNoExistente), Mockito.any(Usuario.class)))
                .thenThrow(new UsuarioNoEncontradoException(idNoExistente));

        mockMvc.perform(put("/api/usuarios/" + idNoExistente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datosActualizacion)))
                .andExpect(status().isNotFound());

        verify(usuarioServiceMock).actualizarUsuario(eq(idNoExistente), Mockito.any(Usuario.class));
    }

    @Test
    @DisplayName("PUT /api/usuarios/{id} con email duplicado de OTRO usuario devuelve status 409")
    void actualizarUsuario_conEmailDuplicadoDeOtroUsuario_DevuelveStatus409() throws Exception {
        Long idUsuarioAActualizar = usuario1.getId();

        Usuario datosActualizacion = new Usuario(usuario1.getNombre(), usuario2.getEmail());

        when(usuarioServiceMock.actualizarUsuario(eq(idUsuarioAActualizar), Mockito.any(Usuario.class)))
                .thenThrow(new RecursoDuplicadoException("El nuevo email '" + usuario2.getEmail() + "' ya está en uso por otro usuario."));

        mockMvc.perform(put("/api/usuarios/" + idUsuarioAActualizar)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datosActualizacion)))
                .andExpect(status().isConflict());

        verify(usuarioServiceMock).actualizarUsuario(eq(idUsuarioAActualizar), Mockito.any(Usuario.class));
    }


    @Test
    @DisplayName("DELETE /api/usuarios/{id} elimina usuario y devuelve status 204")
    void eliminarUsuario_CuandoUsuarioExiste_DevuelveStatus204() throws Exception {
        Long idExistente = 1L;
        doNothing().when(usuarioServiceMock).eliminarUsuario(idExistente);

        mockMvc.perform(delete("/api/usuarios/" + idExistente))
                .andExpect(status().isNoContent());

        verify(usuarioServiceMock).eliminarUsuario(idExistente);
    }

    @Test
    @DisplayName("DELETE /api/usuarios/{id} devuelve status 404 cuando usuario no existe")
    void eliminarUsuario_CuandoUsuarioNoExiste_DevuelveStatus404() throws Exception {
        Long idNoExistente = 999L;
        doThrow(new UsuarioNoEncontradoException(idNoExistente)).when(usuarioServiceMock).eliminarUsuario(idNoExistente);

        mockMvc.perform(delete("/api/usuarios/" + idNoExistente))
                .andExpect(status().isNotFound());

        verify(usuarioServiceMock).eliminarUsuario(idNoExistente);
    }
}