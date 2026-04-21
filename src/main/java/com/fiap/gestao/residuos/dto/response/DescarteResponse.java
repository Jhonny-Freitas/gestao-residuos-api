package com.fiap.gestao.residuos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DescarteResponse {
    private Long idDescarte;
    private Long idContainer;
    private String localizacaoContainer;
    private TipoResiduoResponse tipoResiduo;
    private BigDecimal pesoKg;
    private LocalDateTime dataDescarte;
    private String descarteCorreto;
    private String observacao;
}
