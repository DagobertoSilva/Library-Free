package libraryfree.app.repository.acervo;

import libraryfree.app.domain.acervo.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    boolean existsByLivroId(Long livroId);
    boolean existsByAlunoId(Long alunoId);
}
