package com.fiap.gestao.residuos.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ContainerRequest {
    
    @NotNull(message = "ID do tipo de resíduo é obrigatório")
    private Long idTipoResiduo;
    
    @NotBlank(message = "Localização é obrigatória")
    @Size(min = 3, max = 200, message = "Localização deve ter entre 3 e 200 caracteres")
    private String localizacao;
    
    @NotNull(message = "Capacidade máxima é obrigatória")
    @DecimalMin(value = "0.01", message = "Capacidade máxima deve ser maior que zero")
    private BigDecimal capacidadeMaxima;
    
    @Pattern(regexp = "ATIVO|INATIVO|MANUTENCAO|CHEIO", message = "Status deve ser ATIVO, INATIVO, MANUTENCAO ou CHEIO")
    private String status;
}
