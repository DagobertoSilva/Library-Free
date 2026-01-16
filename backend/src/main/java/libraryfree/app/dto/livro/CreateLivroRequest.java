package libraryfree.app.dto.livro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

public record CreateLivroRequest(
        @Length(max = 20, message = "O tamanho máximo é 20 caracteres.")
        String isbn,

        @NotBlank(message = "O título é obrigatório.")
        String titulo,

        @Positive(message = "O ano não pode ser negativo.")
        Integer anoPublicacao,

        @NotNull(message = "A quantidade total é obrigatória.")
        @PositiveOrZero(message = "A quantidade total não pode ser negativa.")
        Integer quantidadeTotal,

        Long editoraId,
        Set<Long> autoresIds,
        Set<Long> generosIds
) { }