package libraryfree.app.repository.usuario;

import libraryfree.app.domain.usuario.Aluno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    boolean existsByMatricula(String matricula);

    @Query("""
        SELECT a FROM Aluno a
        WHERE (:cursoId IS NULL OR a.turma.curso.id = :cursoId)
        AND (:turmaId IS NULL OR a.turma.id = :turmaId)
        ORDER BY a.nome ASC
    """)
    Page<Aluno> findAllWithFilters(@Param("cursoId") Long cursoId, @Param("turmaId") Long turmaId, Pageable pageable);
}