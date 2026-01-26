package libraryfree.app.repository.acervo;

import libraryfree.app.domain.acervo.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    @Query("""
        SELECT e FROM Emprestimo e
        WHERE (:nomeAluno IS NULL OR LOWER(e.aluno.nome) LIKE :nomeAluno)
        AND (:tituloLivro IS NULL OR LOWER(e.livro.titulo) LIKE :tituloLivro)
    """)
    Page<Emprestimo> findByFiltros(
            @Param("nomeAluno") String nomeAluno,
            @Param("tituloLivro") String tituloLivro,
            Pageable pageable
    );

    boolean existsByLivroId(Long livroId);
    boolean existsByAlunoId(Long alunoId);
}
