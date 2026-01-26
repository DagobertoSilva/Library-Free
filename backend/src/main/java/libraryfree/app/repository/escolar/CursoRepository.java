package libraryfree.app.repository.escolar;

import libraryfree.app.domain.escolar.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}
