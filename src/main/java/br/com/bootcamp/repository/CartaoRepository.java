package br.com.bootcamp.repository;

import br.com.bootcamp.model.Cartao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartaoRepository extends CrudRepository<Cartao, Long> {
    Optional<Cartao> findByidCartao(String idCartao);
}
