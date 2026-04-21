package com.fiap.gestao.residuos.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AtualizarStatusColetaRequest {
    
    @NotBlank(message = "Status da coleta é obrigatório")
    @Pattern(regexp = "REALIZADA|CANCELADA", message = "Status deve ser REALIZADA ou CANCELADA")
    private String statusColeta;
    
    @DecimalMin(value = "0.0", message = "Peso coletado não pode ser negativo")
    private BigDecimal pesoColetado;
    
    @Size(max = 200, message = "Destino final deve ter no máximo 200 caracteres")
    private String destinoFinal;
    
    @Size(max = 200, message = "Observação deve ter no máximo 200 caracteres")
    private String observacao;
}
