package libraryfree.app.dto.aluno;

import libraryfree.app.domain.usuario.Aluno;

public record AlunoResponse(
        Long id,
        String nome,
        String matricula,

        Long turmaId,
        Integer serie,

        Long cursoId,
        String cursoNome
) {
    public AlunoResponse(Aluno entity) {
        this(
                entity.getId(),
                entity.getNome(),
                entity.getMatricula(),
                entity.getTurma().getId(),
                entity.getTurma().getSerie(),
                entity.getTurma().getCurso().getId(),
                entity.getTurma().getCurso().getNome()
        );
    }
}