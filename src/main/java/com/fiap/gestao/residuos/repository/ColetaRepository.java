package com.fiap.gestao.residuos.repository;

import com.fiap.gestao.residuos.model.Coleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ColetaRepository extends JpaRepository<Coleta, Long> {
    
    List<Coleta> findByStatusColeta(String statusColeta);
    
    List<Coleta> findByContainerIdContainer(Long idContainer);
    
    @Query("SELECT c FROM Coleta c WHERE c.dataColeta BETWEEN :inicio AND :fim AND c.statusColeta = 'REALIZADA'")
    List<Coleta> findColetasRealizadasPorPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
    
    @Query("SELECT c FROM Coleta c WHERE c.statusColeta = 'AGENDADA' ORDER BY c.dataAgendamento ASC")
    List<Coleta> findColetasAgendadas();
}
