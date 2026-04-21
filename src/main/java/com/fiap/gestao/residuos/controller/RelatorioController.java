package com.fiap.gestao.residuos.controller;

import com.fiap.gestao.residuos.dto.response.RelatorioEstatisticaResponse;
import com.fiap.gestao.residuos.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
@Tag(name = "Relatórios", description = "Relatórios e estatísticas do sistema")
public class RelatorioController {

    private final RelatorioService service;

    @GetMapping("/dashboard")
    @Operation(summary = "Dashboard com estatísticas gerais")
    public ResponseEntity<RelatorioEstatisticaResponse> getDashboard() {
        return ResponseEntity.ok(service.getDashboard());
    }
}
