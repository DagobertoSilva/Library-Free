package libraryfree.app.repository.acervo;

import libraryfree.app.domain.acervo.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    boolean existsByIsbn(String isbn);
    Page<Livro> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);
    Page<Livro> findByAutoresNomeContainingIgnoreCase(String nomeAutor, Pageable pageable);
    Page<Livro> findByGenerosNomeContainingIgnoreCase(String nomeGenero, Pageable pageable);
}
