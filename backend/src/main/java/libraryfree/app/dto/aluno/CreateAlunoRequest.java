package libraryfree.app.dto.aluno;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CreateAlunoRequest(
        @NotBlank(message = "O nome é obrigatório")
        @Length(max = 255, message = "O nome deve ter no máximo 255 caracteres.")
        String nome,

        @NotBlank(message = "A matrícula é obrigatória")
        @Length(max = 20, message = "A matrícula deve ter no máximo 20 caracteres.")
        String matricula,

        @NotNull(message = "O ID da turma é obrigatório")
        Long turmaId
) {}