package com.biblioteca.sistemagestion.dtos;

import java.time.LocalDate;

public record PrestamoRequestDTO(
        Long libroId,
        Long usuarioId,
        LocalDate fechaDevolucionSugerida
) {}