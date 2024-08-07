package br.com.senior.desafiobackend.db.dto;

import br.com.senior.desafiobackend.db.constates.StatusTarefa;
import br.com.senior.desafiobackend.db.entity.Tarefa;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TarefaDTO {

    private Long id;
    private String titulo;
    private String descricao;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCriacao;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataConclusao;

    private StatusTarefa status;

    public static TarefaDTO convertToDTO(Tarefa tarefa) {
        return TarefaDTO.builder()
                .id(tarefa.getId())
                .titulo(tarefa.getTitulo())
                .descricao(tarefa.getDescricao())
                .dataCriacao(tarefa.getDataCriacao())
                .dataConclusao(tarefa.getDataConclusao())
                .status(tarefa.getStatus())
                .build();
    }
}
