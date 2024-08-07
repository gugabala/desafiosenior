package br.com.senior.desafiobackend.db.entity;



import java.time.LocalDateTime;

import br.com.senior.desafiobackend.db.constates.StatusTarefa;
import br.com.senior.desafiobackend.db.dto.TarefaDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @Enumerated(EnumType.STRING)
    private StatusTarefa status;

    public static  Tarefa convertToEntity(TarefaDTO tarefaDTO) {
        return Tarefa.builder()
                .titulo(tarefaDTO.getTitulo())
                .descricao(tarefaDTO.getDescricao())
                .dataConclusao(tarefaDTO.getDataConclusao())
                .status(tarefaDTO.getStatus())
                .build();
    }
}
