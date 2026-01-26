package libraryfree.app.dto.aluno;

import org.hibernate.validator.constraints.Length;

public record UpdateAlunoRequest(
        @Length(max = 255, message = "O nome deve ter no máximo 255 caracteres.")
        String nome,

        @Length(max = 20, message = "A matrícula deve ter no máximo 20 caracteres.")
        String matricula,

        Long turmaId
) {}