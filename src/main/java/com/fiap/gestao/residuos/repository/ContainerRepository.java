package com.fiap.gestao.residuos.repository;

import com.fiap.gestao.residuos.model.Container;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ContainerRepository extends JpaRepository<Container, Long> {
    
    List<Container> findByStatus(String status);
    
    List<Container> findByLocalizacaoContaining(String localizacao);
    
    @Query("SELECT c FROM Container c WHERE c.percentualOcupacao >= :percentual")
    List<Container> findByPercentualOcupacaoGreaterThanEqual(BigDecimal percentual);
    
    @Query("SELECT c FROM Container c WHERE c.status = 'ATIVO' AND c.percentualOcupacao >= 80")
    List<Container> findContainersCriticos();
    
    List<Container> findByTipoResiduoIdTipoResiduo(Long idTipoResiduo);
}
