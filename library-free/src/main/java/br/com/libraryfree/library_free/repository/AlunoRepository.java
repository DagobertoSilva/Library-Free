package br.com.libraryfree.library_free.repository;

import br.com.libraryfree.library_free.domain.Aluno;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AlunoRepository extends CrudRepository<Aluno, Long> {
    Optional<Aluno> findByMatricula(String matricula);
    Optional<Aluno> findByCpf(String cpf);
}
