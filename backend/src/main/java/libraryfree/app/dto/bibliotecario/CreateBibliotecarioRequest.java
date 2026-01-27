package libraryfree.app.dto.bibliotecario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record CreateBibliotecarioRequest(
        @NotBlank(message = "O e-mail é obrigatório.")
        @Length(max = 255, message = "O email deve ter no máximo 255 caracteres.")
        @Email(message = "Formato de e-mail inválido.")
        String email,

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
                message = "A senha deve ter no mínimo 8 caracteres, com pelo menos uma letra e um número.")
        @NotBlank(message = "A senha é obrigatória.")
        @Length(max = 255, message = "A senha deve ter no máximo 255 caracteres.")
        String senha,

        @NotBlank(message = "O nome é obrigatório.")
        @Length(max = 255, message = "O nome deve ter no máximo 255 caracteres.")
        String nome
) { }
