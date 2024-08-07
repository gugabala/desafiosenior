package br.com.senior.desafiobackend.repository;

import br.com.senior.desafiobackend.db.entity.Tarefa;

import java.util.List;

public interface TarefaRepositoryCustom {
    List<Tarefa> findAllTarefasOrdenadas();
}
