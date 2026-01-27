package libraryfree.app.repository.acervo;

import libraryfree.app.domain.acervo.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<Autor, Long> {
}
