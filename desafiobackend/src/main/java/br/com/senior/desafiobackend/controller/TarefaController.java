package br.com.senior.desafiobackend.controller;

import br.com.senior.desafiobackend.db.dto.TarefaDTO;
import br.com.senior.desafiobackend.exception.ResourceNotFoundException;
import br.com.senior.desafiobackend.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @GetMapping
    public List<TarefaDTO> getAllTarefas() {
        return tarefaService.getAllTarefas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaDTO> getTarefaById(@PathVariable Long id) {
        try {
            TarefaDTO tarefa = tarefaService.getTarefaById(id);
            return ResponseEntity.ok(tarefa);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<TarefaDTO> createTarefa(@Valid @RequestBody TarefaDTO tarefaDTO) {
        TarefaDTO createdTarefa = tarefaService.createTarefa(tarefaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTarefa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaDTO> updateTarefa(@PathVariable Long id, @Valid @RequestBody TarefaDTO tarefaDTO) {
        try {
            TarefaDTO updatedTarefa = tarefaService.updateTarefa(id, tarefaDTO);
            return ResponseEntity.ok(updatedTarefa);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarefa(@PathVariable Long id) {
        try {
            tarefaService.deleteTarefa(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}/concluir")
    public ResponseEntity<TarefaDTO> concluirTarefa(@PathVariable Long id) {
        try {
            TarefaDTO tarefa = tarefaService.concluirTarefa(id);
            return ResponseEntity.ok(tarefa);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}