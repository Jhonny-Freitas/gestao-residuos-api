package com.fiap.gestao.residuos.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ColetaRequest {
    
    @NotNull(message = "ID do container é obrigatório")
    private Long idContainer;
    
    private LocalDateTime dataColeta;
    
    @DecimalMin(value = "0.0", message = "Peso coletado não pode ser negativo")
    private BigDecimal pesoColetado;
    
    @Size(max = 100, message = "Nome da empresa deve ter no máximo 100 caracteres")
    private String empresaResponsavel;
    
    @Pattern(regexp = "AGENDADA|REALIZADA|CANCELADA", message = "Status deve ser AGENDADA, REALIZADA ou CANCELADA")
    private String statusColeta;
    
    @Size(max = 200, message = "Destino final deve ter no máximo 200 caracteres")
    private String destinoFinal;
    
    @Size(max = 200, message = "Observação deve ter no máximo 200 caracteres")
    private String observacao;
}
