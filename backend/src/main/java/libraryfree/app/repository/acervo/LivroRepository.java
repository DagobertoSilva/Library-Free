package libraryfree.app.repository.acervo;

import libraryfree.app.domain.acervo.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    boolean existsByIsbn(String isbn);
    Page<Livro> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);

    @Query("SELECT DISTINCT l FROM Livro l JOIN l.autores a WHERE LOWER(a.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<Livro> buscarPorNomeAutor(@Param("nome") String nome, Pageable pageable);

    @Query("SELECT DISTINCT l FROM Livro l JOIN l.generos g WHERE LOWER(g.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<Livro> buscarPorNomeGenero(@Param("nome") String nome, Pageable pageable);
}
