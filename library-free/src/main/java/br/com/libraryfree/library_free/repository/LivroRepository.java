package br.com.libraryfree.library_free.repository;

import br.com.libraryfree.library_free.domain.Livro;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LivroRepository extends CrudRepository<Livro, Long> {
    Optional<Livro> findByIsbn(String isbn);
}
