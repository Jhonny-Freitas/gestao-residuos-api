package com.fiap.gestao.residuos.controller;

import com.fiap.gestao.residuos.dto.request.ContainerRequest;
import com.fiap.gestao.residuos.dto.response.ContainerResponse;
import com.fiap.gestao.residuos.service.ContainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/containers")
@RequiredArgsConstructor
@Tag(name = "Containers", description = "Gerenciamento de containers de coleta")
public class ContainerController {

    private final ContainerService service;

    @GetMapping
    @Operation(summary = "Listar todos os containers")
    public ResponseEntity<List<ContainerResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar container por ID")
    public ResponseEntity<ContainerResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/criticos")
    @Operation(summary = "Listar containers críticos (acima de 80%)")
    public ResponseEntity<List<ContainerResponse>> findCriticos() {
        return ResponseEntity.ok(service.findCriticos());
    }

    @PostMapping
    @Operation(summary = "Criar novo container")
    public ResponseEntity<ContainerResponse> create(@Valid @RequestBody ContainerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar container")
    public ResponseEntity<ContainerResponse> update(@PathVariable Long id, @Valid @RequestBody ContainerRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar container")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
