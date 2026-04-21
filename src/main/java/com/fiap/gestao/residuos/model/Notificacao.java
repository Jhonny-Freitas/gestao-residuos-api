package com.fiap.gestao.residuos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "NOTIFICACAO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_notificacao")
    @SequenceGenerator(name = "seq_notificacao", sequenceName = "SEQ_NOTIFICACAO", allocationSize = 1)
    @Column(name = "ID_NOTIFICACAO")
    private Long idNotificacao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_CONTAINER")
    private Container container;

    @Column(name = "TIPO_NOTIFICACAO", nullable = false, length = 50)
    private String tipoNotificacao;

    @Column(name = "MENSAGEM", nullable = false, length = 500)
    private String mensagem;

    @Column(name = "DATA_GERACAO", nullable = false)
    private LocalDateTime dataGeracao;

    @Column(name = "PRIORIDADE", length = 10)
    private String prioridade = "MEDIA";  // ALTA, MEDIA, BAIXA

    @Column(name = "STATUS", length = 20)
    private String status = "PENDENTE";  // PENDENTE, VISUALIZADA, RESOLVIDA

    @Column(name = "DATA_RESOLUCAO")
    private LocalDateTime dataResolucao;

    @PrePersist
    protected void onCreate() {
        if (dataGeracao == null) {
            dataGeracao = LocalDateTime.now();
        }
        if (prioridade == null) {
            prioridade = "MEDIA";
        }
        if (status == null) {
            status = "PENDENTE";
        }
    }
}
