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
        return tarefaRepository.findAll().stream().map(TarefaDTO::convertToDTO).collect(Collectors.toList());
    }

    public TarefaDTO getTarefaById(Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com id " + id));
        return TarefaDTO.convertToDTO(tarefa);
    }

    public TarefaDTO createTarefa(TarefaDTO tarefaDTO) {
        Tarefa tarefa = Tarefa.convertToEntity(tarefaDTO);
        tarefa.setDataCriacao(LocalDateTime.now());
        tarefaRepository.save(tarefa);
        return TarefaDTO.convertToDTO(tarefa);
    }

    public TarefaDTO updateTarefa(Long id, TarefaDTO tarefaDTO) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com id " + id));

        tarefa.setTitulo(tarefaDTO.getTitulo());
        tarefa.setDescricao(tarefaDTO.getDescricao());
        tarefa.setDataConclusao(tarefaDTO.getDataConclusao());
        tarefa.setStatus(tarefaDTO.getStatus());

        tarefaRepository.save(tarefa);

        return TarefaDTO.convertToDTO(tarefa);
    }

    public void deleteTarefa(Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com id " + id));

        tarefaRepository.delete(tarefa);
    }

    public TarefaDTO concluirTarefa(Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com id " + id));

        if (tarefa.getStatus().equals(StatusTarefa.CONCLUIDA)) {
            throw new ResourceNotFoundException("Tarefa já está concluída");
        }

        tarefa.setStatus(StatusTarefa.CONCLUIDA);
        tarefa.setDataConclusao(LocalDateTime.now());

        tarefaRepository.save(tarefa);
        return TarefaDTO.convertToDTO(tarefa);
    }

}