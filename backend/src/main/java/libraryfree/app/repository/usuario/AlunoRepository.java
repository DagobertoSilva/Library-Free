package libraryfree.app.repository.usuario;

import libraryfree.app.domain.usuario.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
}
