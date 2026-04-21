package com.fiap.gestao.residuos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "CONTAINER")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Container {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_container")
    @SequenceGenerator(name = "seq_container", sequenceName = "SEQ_CONTAINER", allocationSize = 1)
    @Column(name = "ID_CONTAINER")
    private Long idContainer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_TIPO_RESIDUO", nullable = false)
    private TipoResiduo tipoResiduo;

    @Column(name = "LOCALIZACAO", nullable = false, length = 200)
    private String localizacao;

    @Column(name = "CAPACIDADE_MAXIMA", nullable = false, precision = 10, scale = 2)
    private BigDecimal capacidadeMaxima;

    @Column(name = "CAPACIDADE_ATUAL", precision = 10, scale = 2)
    private BigDecimal capacidadeAtual = BigDecimal.ZERO;

    @Column(name = "PERCENTUAL_OCUPACAO", precision = 5, scale = 2)
    private BigDecimal percentualOcupacao = BigDecimal.ZERO;

    @Column(name = "STATUS", length = 20)
    private String status = "ATIVO";  // ATIVO, INATIVO, MANUTENCAO, CHEIO

    @Column(name = "DATA_INSTALACAO", nullable = false)
    private LocalDate dataInstalacao;

    @Column(name = "ULTIMA_COLETA")
    private LocalDate ultimaColeta;

    @PrePersist
    protected void onCreate() {
        if (dataInstalacao == null) {
            dataInstalacao = LocalDate.now();
        }
        if (capacidadeAtual == null) {
            capacidadeAtual = BigDecimal.ZERO;
        }
        if (percentualOcupacao == null) {
            percentualOcupacao = BigDecimal.ZERO;
        }
        if (status == null) {
            status = "ATIVO";
        }
    }
}
