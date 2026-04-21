package com.fiap.gestao.residuos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "RESIDUO_DESCARTADO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResiduoDescartado {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_residuo_descartado")
    @SequenceGenerator(name = "seq_residuo_descartado", sequenceName = "SEQ_RESIDUO_DESCARTADO", allocationSize = 1)
    @Column(name = "ID_DESCARTE")
    private Long idDescarte;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_CONTAINER", nullable = false)
    private Container container;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_TIPO_RESIDUO", nullable = false)
    private TipoResiduo tipoResiduo;

    @Column(name = "PESO_KG", nullable = false, precision = 10, scale = 2)
    private BigDecimal pesoKg;

    @Column(name = "DATA_DESCARTE", nullable = false)
    private LocalDateTime dataDescarte;

    @Column(name = "DESCARTE_CORRETO", length = 1)
    private String descarteCorreto = "S";  // S ou N

    @Column(name = "OBSERVACAO", length = 200)
    private String observacao;

    @PrePersist
    protected void onCreate() {
        if (dataDescarte == null) {
            dataDescarte = LocalDateTime.now();
        }
        if (descarteCorreto == null) {
            descarteCorreto = "S";
        }
    }
}
