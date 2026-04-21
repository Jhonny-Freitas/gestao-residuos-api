package com.fiap.gestao.residuos.service;

import com.fiap.gestao.residuos.dto.request.DescarteRequest;
import com.fiap.gestao.residuos.dto.response.DescarteResponse;
import com.fiap.gestao.residuos.dto.response.TipoResiduoResponse;
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
public class ResiduoDescartadoService {

    private final ResiduoDescartadoRepository repository;
    private final ContainerRepository containerRepository;
    private final TipoResiduoRepository tipoResiduoRepository;

    @Transactional(readOnly = true)
    public List<DescarteResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DescarteResponse> findIncorretos() {
        return repository.findByDescarteCorreto("N").stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public DescarteResponse create(DescarteRequest request) {
        Container container = containerRepository.findById(request.getIdContainer())
                .orElseThrow(() -> new ResourceNotFoundException("Container", "id", request.getIdContainer()));
        TipoResiduo tipo = tipoResiduoRepository.findById(request.getIdTipoResiduo())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo Resíduo", "id", request.getIdTipoResiduo()));

        ResiduoDescartado descarte = new ResiduoDescartado();
        descarte.setContainer(container);
        descarte.setTipoResiduo(tipo);
        descarte.setPesoKg(request.getPesoKg());
        descarte.setDataDescarte(LocalDateTime.now());
        descarte.setObservacao(request.getObservacao());

        return toResponse(repository.save(descarte));
    }

    private DescarteResponse toResponse(ResiduoDescartado d) {
        TipoResiduoResponse tipo = new TipoResiduoResponse(
                d.getTipoResiduo().getIdTipoResiduo(), d.getTipoResiduo().getNome(),
                d.getTipoResiduo().getDescricao(), d.getTipoResiduo().getCorIdentificacao(),
                d.getTipoResiduo().getImpactoAmbiental(), d.getTipoResiduo().getDataCadastro());
        return new DescarteResponse(d.getIdDescarte(), d.getContainer().getIdContainer(),
                d.getContainer().getLocalizacao(), tipo, d.getPesoKg(), d.getDataDescarte(),
                d.getDescarteCorreto(), d.getObservacao());
    }
}
