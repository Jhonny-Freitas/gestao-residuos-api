package com.fiap.gestao.residuos.controller;

import com.fiap.gestao.residuos.dto.response.NotificacaoResponse;
import com.fiap.gestao.residuos.service.NotificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notificacoes")
@RequiredArgsConstructor
@Tag(name = "Notificações", description = "Gerenciamento de notificações e alertas")
public class NotificacaoController {

    private final NotificacaoService service;

    @GetMapping
    @Operation(summary = "Listar todas as notificações")
    public ResponseEntity<List<NotificacaoResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/pendentes")
    @Operation(summary = "Listar notificações pendentes")
    public ResponseEntity<List<NotificacaoResponse>> findPendentes() {
        return ResponseEntity.ok(service.findPendentes());
    }

    @PutMapping("/{id}/resolver")
    @Operation(summary = "Marcar notificação como resolvida")
    public ResponseEntity<NotificacaoResponse> resolver(@PathVariable Long id) {
        return ResponseEntity.ok(service.resolver(id));
    }
}
