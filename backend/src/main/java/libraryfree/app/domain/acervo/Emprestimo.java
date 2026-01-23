package libraryfree.app.domain.acervo;

import jakarta.persistence.*;
import libraryfree.app.domain.escolar.Turma;
import libraryfree.app.domain.usuario.Aluno;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "emprestimo")
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_emprestimo", nullable = false)
    private LocalDateTime dataEmprestimo;

    @Column(name = "prazo_devolucao", nullable = false)
    private LocalDate prazoDevolucao;

    @Column(name = "data_devolucao")
    private LocalDateTime dataDevolucao;

    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    public Emprestimo(Livro livro, Aluno aluno, int diasPrazo) {
        this.livro = livro;
        this.aluno = aluno;
        this.dataEmprestimo = LocalDateTime.now();
        this.prazoDevolucao = LocalDate.now().plusDays(diasPrazo);
        this.turma = aluno.getTurma();
    }
}