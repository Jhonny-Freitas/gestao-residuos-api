package com.fiap.gestao.residuos.service;

import com.fiap.gestao.residuos.dto.response.NotificacaoResponse;
import com.fiap.gestao.residuos.exception.ResourceNotFoundException;
import com.fiap.gestao.residuos.model.Notificacao;
import com.fiap.gestao.residuos.repository.NotificacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final NotificacaoRepository repository;

    @Transactional(readOnly = true)
    public List<NotificacaoResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificacaoResponse> findPendentes() {
        return repository.findNotificacoesPendentes().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public NotificacaoResponse resolver(Long id) {
        Notificacao notificacao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificação", "id", id));
        notificacao.setStatus("RESOLVIDA");
        notificacao.setDataResolucao(LocalDateTime.now());
        return toResponse(repository.save(notificacao));
    }

    private NotificacaoResponse toResponse(Notificacao n) {
        return new NotificacaoResponse(n.getIdNotificacao(),
                n.getContainer() != null ? n.getContainer().getIdContainer() : null,
                n.getContainer() != null ? n.getContainer().getLocalizacao() : null,
                n.getTipoNotificacao(), n.getMensagem(), n.getDataGeracao(),
                n.getPrioridade(), n.getStatus(), n.getDataResolucao());
    }
}
