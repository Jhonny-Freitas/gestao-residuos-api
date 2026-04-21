package com.fiap.gestao.residuos.controller;

import com.fiap.gestao.residuos.dto.request.*;
import com.fiap.gestao.residuos.dto.response.ColetaResponse;
import com.fiap.gestao.residuos.service.ColetaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/coletas")
@RequiredArgsConstructor
@Tag(name = "Coletas", description = "Gerenciamento de coletas de resíduos")
public class ColetaController {

    private final ColetaService service;

    @GetMapping
    @Operation(summary = "Listar todas as coletas")
    public ResponseEntity<List<ColetaResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/agendadas")
    @Operation(summary = "Listar coletas agendadas")
    public ResponseEntity<List<ColetaResponse>> findAgendadas() {
        return ResponseEntity.ok(service.findAgendadas());
    }

    @PostMapping("/agendar")
    @Operation(summary = "Agendar nova coleta")
    public ResponseEntity<ColetaResponse> create(@Valid @RequestBody ColetaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Atualizar status da coleta (realizar/cancelar)")
    public ResponseEntity<ColetaResponse> atualizarStatus(@PathVariable Long id, @Valid @RequestBody AtualizarStatusColetaRequest request) {
        return ResponseEntity.ok(service.atualizarStatus(id, request));
    }
}
