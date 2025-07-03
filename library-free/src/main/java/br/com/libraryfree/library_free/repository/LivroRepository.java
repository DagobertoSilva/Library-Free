package br.com.libraryfree.library_free.repository;

import br.com.libraryfree.library_free.domain.Livro;
import org.springframework.data.repository.CrudRepository;

public interface LivroRepository extends CrudRepository<Livro, Long> {
}
