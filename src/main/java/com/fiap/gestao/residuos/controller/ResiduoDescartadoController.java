package com.fiap.gestao.residuos.controller;

import com.fiap.gestao.residuos.dto.request.DescarteRequest;
import com.fiap.gestao.residuos.dto.response.DescarteResponse;
import com.fiap.gestao.residuos.service.ResiduoDescartadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/descartes")
@RequiredArgsConstructor
@Tag(name = "Descartes", description = "Registro de descartes de resíduos")
public class ResiduoDescartadoController {

    private final ResiduoDescartadoService service;

    @GetMapping
    @Operation(summary = "Listar todos os descartes")
    public ResponseEntity<List<DescarteResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/incorretos")
    @Operation(summary = "Listar descartes incorretos")
    public ResponseEntity<List<DescarteResponse>> findIncorretos() {
        return ResponseEntity.ok(service.findIncorretos());
    }

    @PostMapping
    @Operation(summary = "Registrar novo descarte")
    public ResponseEntity<DescarteResponse> create(@Valid @RequestBody DescarteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }
}
