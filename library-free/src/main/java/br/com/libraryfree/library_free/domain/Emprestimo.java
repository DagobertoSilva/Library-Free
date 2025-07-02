package br.com.libraryfree.library_free.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "Aluno_Livro")
@Getter
@Setter
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    @JsonBackReference("aluno-emprestimo")
    private Aluno aluno;
    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false)
    @JsonBackReference("livro-emprestimo")
    private Livro livro;
    @Column(nullable = false)
    private LocalDate dataEmprestimo;
    @Column(nullable = false)
    private LocalDate prazoDevolucao;
    private LocalDate dataDevolucao;
    @Column(nullable = false)
    private Boolean ativo;

    public Emprestimo() {}

    public Emprestimo(Long id, Aluno aluno, Livro livro, LocalDate dataEmprestimo, LocalDate prazoDevolucao, LocalDate dataDevolucao, Boolean ativo) {
        this.id = id;
        this.aluno = aluno;
        this.livro = livro;
        this.dataEmprestimo = dataEmprestimo;
        this.prazoDevolucao = prazoDevolucao;
        this.dataDevolucao = dataDevolucao;
        this.ativo = ativo;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Emprestimo emprestimo = (Emprestimo) object;
        return id != null && id.equals(emprestimo.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
