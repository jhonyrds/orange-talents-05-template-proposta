package br.com.bootcamp.repository;

import br.com.bootcamp.model.NovaProposta;
import br.com.bootcamp.model.enums.StatusProposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NovaPropostaRepository extends JpaRepository<NovaProposta, Long> {
    boolean existsByDocumento(String documento);

    List<NovaProposta> findByStatusPropostaEqualsAndIdCartaoIsNull(StatusProposta status);
}
