package libraryfree.app.repository.escolar;

import libraryfree.app.domain.escolar.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findAllByOrderByNomeAsc();
}
