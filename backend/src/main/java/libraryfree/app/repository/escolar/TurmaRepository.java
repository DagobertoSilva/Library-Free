package libraryfree.app.repository.escolar;

import libraryfree.app.domain.escolar.Turma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TurmaRepository extends JpaRepository<Turma, Long> {
    List<Turma> findByCursoId(Long cursoId);
    List<Turma> findAllByOrderBySerieAsc();
}
