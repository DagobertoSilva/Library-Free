package br.com.libraryfree.library_free.DTO;

import br.com.libraryfree.library_free.domain.Emprestimo;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
public class EmprestimoResponseDTO {
    private Long id;
    private Long alunoId;
    private String alunoNome;
    private Long livroId;
    private String livroTitulo;
    private LocalDate dataEmprestimo;
    private LocalDate prazoDevolucao;
    private LocalDate dataDevolucao;
    private Boolean ativo;

    public EmprestimoResponseDTO() {}

    public EmprestimoResponseDTO(Emprestimo emprestimo) {
        this.id = emprestimo.getId();
        this.alunoId = emprestimo.getAluno().getId();
        this.alunoNome = emprestimo.getAluno().getNome();
        this.livroId = emprestimo.getLivro().getId();
        this.livroTitulo = emprestimo.getLivro().getTitulo();
        this.dataEmprestimo = emprestimo.getDataEmprestimo();
        this.prazoDevolucao = emprestimo.getPrazoDevolucao();
        this.dataDevolucao = emprestimo.getDataDevolucao();
        this.ativo = emprestimo.getAtivo();
    }
}
