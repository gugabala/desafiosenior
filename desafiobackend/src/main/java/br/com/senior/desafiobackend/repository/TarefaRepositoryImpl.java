package br.com.senior.desafiobackend.repository;

import br.com.senior.desafiobackend.db.entity.Tarefa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class TarefaRepositoryImpl implements TarefaRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public List<Tarefa> findAllTarefasOrdenadas() {
        String jpql = "SELECT t FROM Tarefa t ORDER BY " +
                "CASE t.status WHEN 'PENDENTE' THEN 1 WHEN 'CONCLUIDA' THEN 2 END, " +
                "t.dataCriacao, t.titulo";
        TypedQuery<Tarefa> query = entityManager.createQuery(jpql, Tarefa.class);
        return query.getResultList();
    }
}
