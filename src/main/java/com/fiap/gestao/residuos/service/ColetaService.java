package com.fiap.gestao.residuos.service;

import com.fiap.gestao.residuos.dto.request.*;
import com.fiap.gestao.residuos.dto.response.ColetaResponse;
import com.fiap.gestao.residuos.exception.ResourceNotFoundException;
import com.fiap.gestao.residuos.model.*;
import com.fiap.gestao.residuos.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColetaService {

    private final ColetaRepository repository;
    private final ContainerRepository containerRepository;

    @Transactional(readOnly = true)
    public List<ColetaResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ColetaResponse> findAgendadas() {
        return repository.findColetasAgendadas().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public ColetaResponse create(ColetaRequest request) {
        Container container = containerRepository.findById(request.getIdContainer())
                .orElseThrow(() -> new ResourceNotFoundException("Container", "id", request.getIdContainer()));

        Coleta coleta = new Coleta();
        coleta.setContainer(container);
        coleta.setEmpresaResponsavel(request.getEmpresaResponsavel());
        coleta.setStatusColeta(request.getStatusColeta() != null ? request.getStatusColeta() : "AGENDADA");
        coleta.setDestinoFinal(request.getDestinoFinal());
        coleta.setDataAgendamento(LocalDateTime.now());
        coleta.setObservacao(request.getObservacao());

        return toResponse(repository.save(coleta));
    }

    @Transactional
    public ColetaResponse atualizarStatus(Long id, AtualizarStatusColetaRequest request) {
        Coleta coleta = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coleta", "id", id));

        coleta.setStatusColeta(request.getStatusColeta());
        if ("REALIZADA".equals(request.getStatusColeta())) {
            coleta.setDataColeta(LocalDateTime.now());
            coleta.setPesoColetado(request.getPesoColetado());
        }
        if (request.getDestinoFinal() != null) coleta.setDestinoFinal(request.getDestinoFinal());
        if (request.getObservacao() != null) coleta.setObservacao(request.getObservacao());

        return toResponse(repository.save(coleta));
    }

    private ColetaResponse toResponse(Coleta c) {
        return new ColetaResponse(c.getIdColeta(), c.getContainer().getIdContainer(),
                c.getContainer().getLocalizacao(), c.getDataColeta(), c.getPesoColetado(),
                c.getEmpresaResponsavel(), c.getStatusColeta(), c.getDestinoFinal(),
                c.getDataAgendamento(), c.getObservacao());
    }
}
