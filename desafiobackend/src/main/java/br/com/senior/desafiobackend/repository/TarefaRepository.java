package br.com.senior.desafiobackend.repository;

import br.com.senior.desafiobackend.db.entity.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
}
