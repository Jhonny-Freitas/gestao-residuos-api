package com.fiap.gestao.residuos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContainerResponse {
    private Long idContainer;
    private TipoResiduoResponse tipoResiduo;
    private String localizacao;
    private BigDecimal capacidadeMaxima;
    private BigDecimal capacidadeAtual;
    private BigDecimal percentualOcupacao;
    private String status;
    private LocalDate dataInstalacao;
    private LocalDate ultimaColeta;
}
