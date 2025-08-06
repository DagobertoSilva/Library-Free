package br.com.libraryfree.library_free.repository;

import br.com.libraryfree.library_free.domain.Livro;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LivroRepository extends CrudRepository<Livro, Long> {
    Optional<Livro> findByIsbn(String isbn);
    @Query("SELECT l FROM Livro l WHERE upper(l.titulo) LIKE concat('%', upper(:titulo), '%')")
    Iterable<Livro> findByTitulo(String titulo);
}
