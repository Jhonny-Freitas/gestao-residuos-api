package com.fiap.gestao.residuos.repository;

import com.fiap.gestao.residuos.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    
    List<Notificacao> findByStatus(String status);
    
    List<Notificacao> findByPrioridade(String prioridade);
    
    List<Notificacao> findByContainerIdContainer(Long idContainer);
    
    @Query("SELECT n FROM Notificacao n WHERE n.status = 'PENDENTE' ORDER BY n.prioridade DESC, n.dataGeracao DESC")
    List<Notificacao> findNotificacoesPendentes();
    
    @Query("SELECT n FROM Notificacao n WHERE n.container.idContainer = :idContainer AND n.status = 'PENDENTE'")
    List<Notificacao> findNotificacoesPendentesByContainer(Long idContainer);
}
