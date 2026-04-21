package com.fiap.gestao.residuos.service;

import com.fiap.gestao.residuos.dto.response.RelatorioEstatisticaResponse;
import com.fiap.gestao.residuos.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final ContainerRepository containerRepository;
    private final ResiduoDescartadoRepository residuoRepository;
    private final ColetaRepository coletaRepository;
    private final NotificacaoRepository notificacaoRepository;

    @Transactional(readOnly = true)
    public RelatorioEstatisticaResponse getDashboard() {
        Long totalContainers = containerRepository.count();
        Long containersAtivos = (long) containerRepository.findByStatus("ATIVO").size();
        Long containersCriticos = (long) containerRepository.findContainersCriticos().size();
        Long totalDescartes = residuoRepository.count();
        Long descartesIncorretos = (long) residuoRepository.findByDescarteCorreto("N").size();
        Long coletasRealizadas = (long) coletaRepository.findByStatusColeta("REALIZADA").size();
        
        BigDecimal pesoTotal = coletaRepository.findByStatusColeta("REALIZADA").stream()
                .map(c -> c.getPesoColetado() != null ? c.getPesoColetado() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal taxaReciclagem = totalDescartes > 0 
                ? BigDecimal.valueOf((totalDescartes - descartesIncorretos) * 100.0 / totalDescartes)
                : BigDecimal.ZERO;

        Long notificacoesPendentes = (long) notificacaoRepository.findByStatus("PENDENTE").size();

        return new RelatorioEstatisticaResponse(totalContainers, containersAtivos, containersCriticos,
                totalDescartes, descartesIncorretos, coletasRealizadas, pesoTotal,
                taxaReciclagem, notificacoesPendentes);
    }
}
