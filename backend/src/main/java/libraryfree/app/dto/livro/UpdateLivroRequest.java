package libraryfree.app.dto.livro;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;
import java.util.Set;

public record UpdateLivroRequest(
        @Length(max = 20, message = "O tamanho máximo é 20 caracteres.")
        String isbn,

        @Length(max = 255, message = "O título deve ter no máximo 255 caracteres.")
        String titulo,

        @Positive(message = "O ano não pode ser negativo.")
        Integer anoPublicacao,

        @PositiveOrZero(message = "A quantidade total não pode ser negativa.")
        Integer quantidadeTotal,

        Long editoraId,
        Set<Long> autoresIds,
        Set<Long> generosIds
) { }