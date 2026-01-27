package libraryfree.app.dto.emprestimo;

import jakarta.validation.constraints.NotNull;

public record EmprestimoRequest(
        @NotNull(message = "O ID do livro é obrigatório")
        Long livroId,

        @NotNull(message = "O ID do aluno é obrigatório")
        Long alunoId
) {}