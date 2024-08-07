package br.com.senior.desafiobackend.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.senior.desafiobackend.db.dto.TarefaDTO;
import br.com.senior.desafiobackend.db.entity.Tarefa;
import br.com.senior.desafiobackend.db.constates.StatusTarefa;
import br.com.senior.desafiobackend.exception.ResourceNotFoundException;
import br.com.senior.desafiobackend.repository.TarefaRepository;
import br.com.senior.desafiobackend.service.TarefaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @InjectMocks
    private TarefaService tarefaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTarefas_returnsListOfTarefaDTOs() {
        Tarefa tarefa1 = new Tarefa(1L, "Titulo1", "Descricao1", LocalDateTime.now(), null, StatusTarefa.PENDENTE);
        Tarefa tarefa2 = new Tarefa(2L, "Titulo2", "Descricao2", LocalDateTime.now(), null, StatusTarefa.PENDENTE);
        when(tarefaRepository.findAllTarefasOrdenadas()).thenReturn(Arrays.asList(tarefa1, tarefa2));

        List<TarefaDTO> result = tarefaService.getAllTarefas();

        assertEquals(2, result.size());
        assertEquals("Titulo1", result.get(0).getTitulo());
        assertEquals("Titulo2", result.get(1).getTitulo());
    }

    @Test
    void getTarefaById_existingId_returnsTarefaDTO() {
        Tarefa tarefa = new Tarefa(1L, "Titulo", "Descricao", LocalDateTime.now(), null, StatusTarefa.PENDENTE);
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));

        TarefaDTO result = tarefaService.getTarefaById(1L);

        assertEquals("Titulo", result.getTitulo());
        assertEquals("Descricao", result.getDescricao());
    }

    @Test
    void getTarefaById_nonExistingId_throwsResourceNotFoundException() {
        when(tarefaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tarefaService.getTarefaById(1L));
    }

    @Test
    void createTarefa_validTarefaDTO_returnsCreatedTarefaDTO() {
        TarefaDTO tarefaDTO = new TarefaDTO(null, "Titulo", "Descricao", null, null, StatusTarefa.PENDENTE);
        Tarefa tarefa = new Tarefa(1L, "Titulo", "Descricao", LocalDateTime.now(), null, StatusTarefa.PENDENTE);
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);

        TarefaDTO result = tarefaService.createTarefa(tarefaDTO);

        assertEquals("Titulo", result.getTitulo());
        assertEquals("Descricao", result.getDescricao());
    }

    @Test
    void updateTarefa_existingId_updatesAndReturnsTarefaDTO() {
        Tarefa tarefa = new Tarefa(1L, "Titulo", "Descricao", LocalDateTime.now(), null, StatusTarefa.PENDENTE);
        TarefaDTO tarefaDTO = new TarefaDTO(1L, "Novo Titulo", "Nova Descricao", null, null, StatusTarefa.PENDENTE);
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);

        TarefaDTO result = tarefaService.updateTarefa(1L, tarefaDTO);

        assertEquals("Novo Titulo", result.getTitulo());
        assertEquals("Nova Descricao", result.getDescricao());
    }

    @Test
    void updateTarefa_nonExistingId_throwsResourceNotFoundException() {
        TarefaDTO tarefaDTO = new TarefaDTO(1L, "Titulo", "Descricao", null, null, StatusTarefa.PENDENTE);
        when(tarefaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tarefaService.updateTarefa(1L, tarefaDTO));
    }

    @Test
    void deleteTarefa_existingId_deletesTarefa() {
        Tarefa tarefa = new Tarefa(1L, "Titulo", "Descricao", LocalDateTime.now(), null, StatusTarefa.PENDENTE);
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));

        tarefaService.deleteTarefa(1L);

        verify(tarefaRepository, times(1)).delete(tarefa);
    }

    @Test
    void deleteTarefa_nonExistingId_throwsResourceNotFoundException() {
        when(tarefaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tarefaService.deleteTarefa(1L));
    }

    @Test
    void concluirTarefa_existingIdAndNotConcluded_setsStatusToConcluidaAndReturnsTarefaDTO() {
        Tarefa tarefa = new Tarefa(1L, "Titulo", "Descricao", LocalDateTime.now(), null, StatusTarefa.PENDENTE);
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);

        TarefaDTO result = tarefaService.concluirTarefa(1L);

        assertEquals(StatusTarefa.CONCLUIDA, result.getStatus());
        assertNotNull(result.getDataConclusao());
    }

    @Test
    void concluirTarefa_existingIdAndAlreadyConcluded_throwsResourceNotFoundException() {
        Tarefa tarefa = new Tarefa(1L, "Titulo", "Descricao", LocalDateTime.now(), LocalDateTime.now(), StatusTarefa.CONCLUIDA);
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));

        assertThrows(ResourceNotFoundException.class, () -> tarefaService.concluirTarefa(1L));
    }

    @Test
    void concluirTarefa_nonExistingId_throwsResourceNotFoundException() {
        when(tarefaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tarefaService.concluirTarefa(1L));
    }
}