package libraryfree.app.dto.escolar;

import libraryfree.app.domain.escolar.Curso;

public record CursoResponse(Long id, String nome) {
    public CursoResponse(Curso entity) {
        this(entity.getId(), entity.getNome());
    }
}