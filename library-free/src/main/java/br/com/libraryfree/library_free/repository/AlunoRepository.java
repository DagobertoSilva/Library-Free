package br.com.libraryfree.library_free.repository;

import br.com.libraryfree.library_free.domain.Aluno;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AlunoRepository extends CrudRepository<Aluno, Long> {
    @Query("SELECT a FROM Aluno a WHERE upper(a.nome) LIKE concat('%', upper(:nome), '%')")
    Iterable<Aluno> findByNome(String nome);
    Optional<Aluno> findByMatricula(String matricula);
    Optional<Aluno> findByCpf(String cpf);
}
