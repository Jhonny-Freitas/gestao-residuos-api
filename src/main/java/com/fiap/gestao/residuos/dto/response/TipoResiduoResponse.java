package com.fiap.gestao.residuos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoResiduoResponse {
    private Long idTipoResiduo;
    private String nome;
    private String descricao;
    private String corIdentificacao;
    private String impactoAmbiental;
    private LocalDate dataCadastro;
}
