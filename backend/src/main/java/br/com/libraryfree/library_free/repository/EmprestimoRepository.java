package br.com.libraryfree.library_free.repository;

import br.com.libraryfree.library_free.domain.Emprestimo;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmprestimoRepository extends CrudRepository<Emprestimo, Long> {
    Optional<Emprestimo> findByLivroIdAndAtivoTrue(Long livroId);
}
