package libraryfree.app.dto.bibliotecario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateBibliotecarioRequest(
        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Formato de e-mail inválido.")
        String email,

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
                message = "A senha deve ter no mínimo 8 caracteres, com pelo menos uma letra e um número.")
        @NotBlank(message = "A senha é obrigatória.")
        String senha,

        @NotBlank(message = "O nome é obrigatório.")
        String nome
) { }
