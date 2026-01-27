package libraryfree.app.dto.emprestimo;

import libraryfree.app.domain.acervo.Emprestimo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmprestimoResponse(
        Long id,
        String tituloLivro,
        String nomeAluno,
        String turmaSnapshot,
        LocalDateTime dataEmprestimo,
        LocalDate prazoDevolucao,
        LocalDateTime dataDevolucao
) {
    public EmprestimoResponse(Emprestimo entity) {
        this(
                entity.getId(),
                entity.getLivro().getTitulo(),
                entity.getAluno().getNome(),
                String.format("%dº %s (%d)",
                        entity.getTurma().getSerie(),
                        entity.getTurma().getCurso().getNome(),
                        entity.getTurma().getAnoLetivo()),
                entity.getDataEmprestimo(),
                entity.getPrazoDevolucao(),
                entity.getDataDevolucao()
        );
    }
}