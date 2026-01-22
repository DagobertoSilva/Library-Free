package libraryfree.app.dto.generico;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record NomeRequest(
        @NotBlank(message = "O nome é obrigatório.")
        @Length(max = 255, message = "O nome deve ter no máximo 255 caracteres.")
        String nome
) {}