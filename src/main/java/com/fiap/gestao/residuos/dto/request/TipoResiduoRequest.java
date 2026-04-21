package com.fiap.gestao.residuos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TipoResiduoRequest {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 50, message = "Nome deve ter entre 3 e 50 caracteres")
    private String nome;
    
    @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
    private String descricao;
    
    @Size(max = 20, message = "Cor deve ter no máximo 20 caracteres")
    private String corIdentificacao;
    
    @Pattern(regexp = "ALTO|MEDIO|BAIXO", message = "Impacto ambiental deve ser ALTO, MEDIO ou BAIXO")
    private String impactoAmbiental;
}
