package br.com.libraryfree.library_free.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Aluno")
@Getter
@Setter
@EqualsAndHashCode(of = "cpf")
@ToString
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 255)
    private String nome;
    @Column(nullable = false, length = 50, unique = true)
    private String matricula;
    @Column(nullable = false, length = 13, unique = true)
    private String cpf;
    @Column(nullable = false)
    private Boolean ativo;
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("aluno-emprestimo")
    private List<Emprestimo> emprestimos = new ArrayList<>();

    public Aluno() {}

    public Aluno(Long id, String nome, String matricula, String cpf, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.matricula = matricula;
        this.cpf = cpf;
        this.ativo = ativo;
    }
}
