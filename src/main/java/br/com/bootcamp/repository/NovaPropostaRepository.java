package br.com.bootcamp.repository;

import br.com.bootcamp.model.NovaProposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NovaPropostaRepository extends JpaRepository<NovaProposta, Long> {
    boolean existsByDocumento(String documento);
}
