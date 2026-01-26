package libraryfree.app.dto.escolar;

import libraryfree.app.domain.escolar.Turma;

public record TurmaResponse(
        Long id,
        Integer serie,
        Integer anoLetivo,
        String descricao
) {
    public TurmaResponse(Turma entity) {
        this(
                entity.getId(),
                entity.getSerie(),
                entity.getAnoLetivo(),
                entity.getSerie() + "º Ano (" + entity.getAnoLetivo() + ")"
        );
    }
}
