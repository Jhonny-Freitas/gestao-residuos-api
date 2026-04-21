package com.fiap.gestao.residuos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColetaResponse {
    private Long idColeta;
    private Long idContainer;
    private String localizacaoContainer;
    private LocalDateTime dataColeta;
    private BigDecimal pesoColetado;
    private String empresaResponsavel;
    private String statusColeta;
    private String destinoFinal;
    private LocalDateTime dataAgendamento;
    private String observacao;
}
