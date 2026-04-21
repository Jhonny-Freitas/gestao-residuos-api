package com.fiap.gestao.residuos.controller;

import com.fiap.gestao.residuos.dto.request.TipoResiduoRequest;
import com.fiap.gestao.residuos.dto.response.TipoResiduoResponse;
import com.fiap.gestao.residuos.service.TipoResiduoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tipos-residuos")
@RequiredArgsConstructor
@Tag(name = "Tipos de Resíduos", description = "Gerenciamento de tipos de resíduos")
public class TipoResiduoController {

    private final TipoResiduoService service;

    @GetMapping
    @Operation(summary = "Listar todos os tipos de resíduos")
    public ResponseEntity<List<TipoResiduoResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tipo de resíduo por ID")
    public ResponseEntity<TipoResiduoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar novo tipo de resíduo")
    public ResponseEntity<TipoResiduoResponse> create(@Valid @RequestBody TipoResiduoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tipo de resíduo")
    public ResponseEntity<TipoResiduoResponse> update(@PathVariable Long id, @Valid @RequestBody TipoResiduoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar tipo de resíduo")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
