package com.fiap.gestao.residuos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioEstatisticaResponse {
    private Long totalContainers;
    private Long containersAtivos;
    private Long containersCriticos;
    private Long totalDescartes;
    private Long descartesIncorretos;
    private Long coletasRealizadas;
    private BigDecimal pesoTotalColetado;
    private BigDecimal taxaReciclagem;
    private Long notificacoesPendentes;
}
