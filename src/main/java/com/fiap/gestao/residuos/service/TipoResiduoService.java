package com.fiap.gestao.residuos.service;

import com.fiap.gestao.residuos.dto.request.TipoResiduoRequest;
import com.fiap.gestao.residuos.dto.response.TipoResiduoResponse;
import com.fiap.gestao.residuos.exception.BusinessException;
import com.fiap.gestao.residuos.exception.ResourceNotFoundException;
import com.fiap.gestao.residuos.model.TipoResiduo;
import com.fiap.gestao.residuos.repository.TipoResiduoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TipoResiduoService {

    private final TipoResiduoRepository repository;

    @Transactional(readOnly = true)
    public List<TipoResiduoResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TipoResiduoResponse findById(Long id) {
        TipoResiduo tipoResiduo = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de Resíduo", "id", id));
        return toResponse(tipoResiduo);
    }

    @Transactional
    public TipoResiduoResponse create(TipoResiduoRequest request) {
        if (repository.existsByNome(request.getNome())) {
            throw new BusinessException("Já existe um tipo de resíduo com o nome: " + request.getNome());
        }

        TipoResiduo tipoResiduo = new TipoResiduo();
        tipoResiduo.setNome(request.getNome());
        tipoResiduo.setDescricao(request.getDescricao());
        tipoResiduo.setCorIdentificacao(request.getCorIdentificacao());
        tipoResiduo.setImpactoAmbiental(request.getImpactoAmbiental());
        tipoResiduo.setDataCadastro(LocalDate.now());

        TipoResiduo saved = repository.save(tipoResiduo);
        return toResponse(saved);
    }

    @Transactional
    public TipoResiduoResponse update(Long id, TipoResiduoRequest request) {
        TipoResiduo tipoResiduo = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de Resíduo", "id", id));

        if (!tipoResiduo.getNome().equals(request.getNome()) && repository.existsByNome(request.getNome())) {
            throw new BusinessException("Já existe um tipo de resíduo com o nome: " + request.getNome());
        }

        tipoResiduo.setNome(request.getNome());
        tipoResiduo.setDescricao(request.getDescricao());
        tipoResiduo.setCorIdentificacao(request.getCorIdentificacao());
        tipoResiduo.setImpactoAmbiental(request.getImpactoAmbiental());

        TipoResiduo updated = repository.save(tipoResiduo);
        return toResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Tipo de Resíduo", "id", id);
        }
        repository.deleteById(id);
    }

    private TipoResiduoResponse toResponse(TipoResiduo tipoResiduo) {
        return new TipoResiduoResponse(
                tipoResiduo.getIdTipoResiduo(),
                tipoResiduo.getNome(),
                tipoResiduo.getDescricao(),
                tipoResiduo.getCorIdentificacao(),
                tipoResiduo.getImpactoAmbiental(),
                tipoResiduo.getDataCadastro()
        );
    }
}
