package com.fiap.gestao.residuos.repository;

import com.fiap.gestao.residuos.model.ResiduoDescartado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ResiduoDescartadoRepository extends JpaRepository<ResiduoDescartado, Long> {
    
    List<ResiduoDescartado> findByContainerIdContainer(Long idContainer);
    
    List<ResiduoDescartado> findByDescarteCorreto(String descarteCorreto);
    
    @Query("SELECT r FROM ResiduoDescartado r WHERE r.dataDescarte BETWEEN :inicio AND :fim")
    List<ResiduoDescartado> findByPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
    
    @Query("SELECT r FROM ResiduoDescartado r WHERE r.container.idContainer = :idContainer AND r.descarteCorreto = 'N'")
    List<ResiduoDescartado> findDescartesIncorretosByContainer(@Param("idContainer") Long idContainer);
}
