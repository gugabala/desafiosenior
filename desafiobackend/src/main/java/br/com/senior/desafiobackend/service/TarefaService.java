package br.com.senior.desafiobackend.service;

import br.com.senior.desafiobackend.db.constates.StatusTarefa;
import br.com.senior.desafiobackend.db.dto.TarefaDTO;
import br.com.senior.desafiobackend.db.entity.Tarefa;
import br.com.senior.desafiobackend.exception.ResourceNotFoundException;
import br.com.senior.desafiobackend.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;
    public List<TarefaDTO> getAllTarefas() {
        return tarefaRepository.findAllTarefasOrdenadas().stream().map(TarefaDTO::convertToDTO).collect(Collectors.toList());
    }

    public TarefaDTO getTarefaById(Long id) {
        Tarefa tarefa = getTarefa(id);
        return TarefaDTO.convertToDTO(tarefa);
    }

    private Tarefa getTarefa(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com id " + id));
    }

    public TarefaDTO createTarefa(TarefaDTO tarefaDTO) {
        this.validateTituloDescricao(tarefaDTO.getTitulo(), tarefaDTO.getDescricao());
        Tarefa tarefa = Tarefa.convertToEntity(tarefaDTO);
        this.criteriosCreate(tarefa);
        tarefaRepository.save(tarefa);
        return TarefaDTO.convertToDTO(tarefa);
    }

    private void criteriosCreate(Tarefa tarefa) {
        tarefa.setDataCriacao(LocalDateTime.now());
        tarefa.setDataConclusao(null);
        tarefa.setStatus(StatusTarefa.PENDENTE);
    }

    public TarefaDTO updateTarefa(Long id, TarefaDTO tarefaDTO) {
        this.validateTituloDescricao(tarefaDTO.getTitulo(), tarefaDTO.getDescricao());
        Tarefa tarefa = getTarefa(id);

        this.criteriosUpdate(tarefaDTO, tarefa);

        tarefaRepository.save(tarefa);

        return TarefaDTO.convertToDTO(tarefa);
    }

    private  void criteriosUpdate(TarefaDTO tarefaDTO, Tarefa tarefa) {

        tarefa.setTitulo(tarefaDTO.getTitulo());
        tarefa.setDescricao(tarefaDTO.getDescricao());
        tarefa.setDataConclusao(tarefaDTO.getDataConclusao());
        tarefa.setStatus(tarefaDTO.getStatus());
    }

    public void deleteTarefa(Long id) {
        Tarefa tarefa = getTarefa(id);

        tarefaRepository.delete(tarefa);
    }

    public TarefaDTO concluirTarefa(Long id) {
        Tarefa tarefa = getTarefa(id);

        if (tarefa.getStatus().equals(StatusTarefa.CONCLUIDA)) {
            throw new ResourceNotFoundException("Tarefa já está concluída");
        }

        tarefa.setStatus(StatusTarefa.CONCLUIDA);
        tarefa.setDataConclusao(LocalDateTime.now());

        tarefaRepository.save(tarefa);
        return TarefaDTO.convertToDTO(tarefa);
    }
    private void validateTituloDescricao(String titulo, String descricao) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Título não pode ser nulo ou vazio");
        }
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição não pode ser nula ou vazia");
        }
    }

}