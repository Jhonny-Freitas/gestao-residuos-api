package com.fiap.gestao.residuos.service;

import com.fiap.gestao.residuos.dto.request.ContainerRequest;
import com.fiap.gestao.residuos.dto.response.ContainerResponse;
import com.fiap.gestao.residuos.dto.response.TipoResiduoResponse;
import com.fiap.gestao.residuos.exception.ResourceNotFoundException;
import com.fiap.gestao.residuos.model.Container;
import com.fiap.gestao.residuos.model.TipoResiduo;
import com.fiap.gestao.residuos.repository.ContainerRepository;
import com.fiap.gestao.residuos.repository.TipoResiduoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContainerService {

    private final ContainerRepository repository;
    private final TipoResiduoRepository tipoResiduoRepository;

    @Transactional(readOnly = true)
    public List<ContainerResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ContainerResponse findById(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Container", "id", id)));
    }

    @Transactional(readOnly = true)
    public List<ContainerResponse> findCriticos() {
        return repository.findContainersCriticos().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public ContainerResponse create(ContainerRequest request) {
        TipoResiduo tipo = tipoResiduoRepository.findById(request.getIdTipoResiduo())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo Resíduo", "id", request.getIdTipoResiduo()));

        Container container = new Container();
        container.setTipoResiduo(tipo);
        container.setLocalizacao(request.getLocalizacao());
        container.setCapacidadeMaxima(request.getCapacidadeMaxima());
        container.setCapacidadeAtual(BigDecimal.ZERO);
        container.setPercentualOcupacao(BigDecimal.ZERO);
        container.setStatus(request.getStatus() != null ? request.getStatus() : "ATIVO");
        container.setDataInstalacao(LocalDate.now());

        return toResponse(repository.save(container));
    }

    @Transactional
    public ContainerResponse update(Long id, ContainerRequest request) {
        Container container = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Container", "id", id));

        if (request.getIdTipoResiduo() != null) {
            TipoResiduo tipo = tipoResiduoRepository.findById(request.getIdTipoResiduo())
                    .orElseThrow(() -> new ResourceNotFoundException("Tipo Resíduo", "id", request.getIdTipoResiduo()));
            container.setTipoResiduo(tipo);
        }

        container.setLocalizacao(request.getLocalizacao());
        container.setCapacidadeMaxima(request.getCapacidadeMaxima());
        if (request.getStatus() != null) container.setStatus(request.getStatus());

        return toResponse(repository.save(container));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Container", "id", id);
        repository.deleteById(id);
    }

    private ContainerResponse toResponse(Container c) {
        TipoResiduoResponse tipo = new TipoResiduoResponse(
                c.getTipoResiduo().getIdTipoResiduo(), c.getTipoResiduo().getNome(),
                c.getTipoResiduo().getDescricao(), c.getTipoResiduo().getCorIdentificacao(),
                c.getTipoResiduo().getImpactoAmbiental(), c.getTipoResiduo().getDataCadastro());
        return new ContainerResponse(c.getIdContainer(), tipo, c.getLocalizacao(),
                c.getCapacidadeMaxima(), c.getCapacidadeAtual(), c.getPercentualOcupacao(),
                c.getStatus(), c.getDataInstalacao(), c.getUltimaColeta());
    }
}
