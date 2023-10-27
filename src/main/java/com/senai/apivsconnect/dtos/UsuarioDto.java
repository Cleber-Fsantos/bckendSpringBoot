package com.senai.apivsconnect.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioDto(
        @NotBlank String nome,
        //validation do java
        @NotBlank @Email(message = "O Email deve estar em formato válido") String email,
        @NotBlank String senha,
        String endereco,
        String cep,
        String tipo_usuario,
        String url_img
) {
}
