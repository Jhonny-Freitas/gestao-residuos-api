package com.fiap.gestao.residuos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "TIPO_RESIDUO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoResiduo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tipo_residuo")
    @SequenceGenerator(name = "seq_tipo_residuo", sequenceName = "SEQ_TIPO_RESIDUO", allocationSize = 1)
    @Column(name = "ID_TIPO_RESIDUO")
    private Long idTipoResiduo;

    @Column(name = "NOME", nullable = false, unique = true, length = 50)
    private String nome;

    @Column(name = "DESCRICAO", length = 200)
    private String descricao;

    @Column(name = "COR_IDENTIFICACAO", length = 20)
    private String corIdentificacao;

    @Column(name = "IMPACTO_AMBIENTAL", length = 10)
    private String impactoAmbiental;  // ALTO, MEDIO, BAIXO

    @Column(name = "DATA_CADASTRO", nullable = false)
    private LocalDate dataCadastro;

    @PrePersist
    protected void onCreate() {
        if (dataCadastro == null) {
            dataCadastro = LocalDate.now();
        }
    }
}
