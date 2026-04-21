package com.fiap.gestao.residuos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "COLETA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coleta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_coleta")
    @SequenceGenerator(name = "seq_coleta", sequenceName = "SEQ_COLETA", allocationSize = 1)
    @Column(name = "ID_COLETA")
    private Long idColeta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_CONTAINER", nullable = false)
    private Container container;

    @Column(name = "DATA_COLETA")
    private LocalDateTime dataColeta;

    @Column(name = "PESO_COLETADO", precision = 10, scale = 2)
    private BigDecimal pesoColetado;

    @Column(name = "EMPRESA_RESPONSAVEL", length = 100)
    private String empresaResponsavel;

    @Column(name = "STATUS_COLETA", length = 20)
    private String statusColeta = "AGENDADA";  // AGENDADA, REALIZADA, CANCELADA

    @Column(name = "DESTINO_FINAL", length = 200)
    private String destinoFinal;

    @Column(name = "DATA_AGENDAMENTO", nullable = false)
    private LocalDateTime dataAgendamento;

    @Column(name = "OBSERVACAO", length = 200)
    private String observacao;

    @PrePersist
    protected void onCreate() {
        if (dataAgendamento == null) {
            dataAgendamento = LocalDateTime.now();
        }
        if (statusColeta == null) {
            statusColeta = "AGENDADA";
        }
    }
}
