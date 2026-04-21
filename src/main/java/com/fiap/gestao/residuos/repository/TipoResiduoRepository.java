package com.fiap.gestao.residuos.repository;

import com.fiap.gestao.residuos.model.TipoResiduo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoResiduoRepository extends JpaRepository<TipoResiduo, Long> {
    
    Optional<TipoResiduo> findByNome(String nome);
    
    boolean existsByNome(String nome);
}
