package br.com.libraryfree.library_free.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Livro")
@Getter
@Setter
@EqualsAndHashCode(of = "isbn")
@ToString
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 255)
    private String titulo;
    @Column(unique = true, nullable = false, length = 13)
    private String isbn;
    private String autor;
    private String genero;
    private String edicao;
    private LocalDate publicacao;
    private String editora;
    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean emprestado;
    @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("livro-emprestimo")
    private List<Emprestimo> emprestimos = new ArrayList<>();

    public Livro() {}

    public Livro(Long id, String titulo, String isbn, String autor, String genero, String edicao, LocalDate publicacao,
                 String editora, Boolean emprestado) {
        this.id = id;
        this.titulo = titulo;
        this.isbn = isbn;
        this.autor = autor;
        this.genero = genero;
        this.edicao = edicao;
        this.publicacao = publicacao;
        this.editora = editora;
        this.emprestado = emprestado;
    }
}
