package com.biblioteca.sistemagestion.modelo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private Long id;
    private String nombre;
    private String email;
    private EstadoUsuario estado;

    public Usuario(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
        this.estado = EstadoUsuario.ACTIVO;
    }
}