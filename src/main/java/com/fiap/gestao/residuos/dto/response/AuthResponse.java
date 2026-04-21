package com.fiap.gestao.residuos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private String email;
    private String nome;
    private String role;
    
    public AuthResponse(String token, String email, String nome, String role) {
        this.token = token;
        this.email = email;
        this.nome = nome;
        this.role = role;
    }
}
