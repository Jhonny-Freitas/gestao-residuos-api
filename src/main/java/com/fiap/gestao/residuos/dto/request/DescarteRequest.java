package com.fiap.gestao.residuos.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DescarteRequest {
    
    @NotNull(message = "ID do container é obrigatório")
    private Long idContainer;
    
    @NotNull(message = "ID do tipo de resíduo é obrigatório")
    private Long idTipoResiduo;
    
    @NotNull(message = "Peso é obrigatório")
    @DecimalMin(value = "0.01", message = "Peso deve ser maior que zero")
    private BigDecimal pesoKg;
    
    @Size(max = 200, message = "Observação deve ter no máximo 200 caracteres")
    private String observacao;
}
