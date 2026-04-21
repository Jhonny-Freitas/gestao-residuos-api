package com.fiap.gestao.residuos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacaoResponse {
    private Long idNotificacao;
    private Long idContainer;
    private String localizacaoContainer;
    private String tipoNotificacao;
    private String mensagem;
    private LocalDateTime dataGeracao;
    private String prioridade;
    private String status;
    private LocalDateTime dataResolucao;
}
