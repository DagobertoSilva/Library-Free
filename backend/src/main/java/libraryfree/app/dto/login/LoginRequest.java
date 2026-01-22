package libraryfree.app.dto.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record LoginRequest(
        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Formato de e-mail inválido.")
        @Length(max = 255, message = "O email deve ter no máximo 255 caracteres.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Length(max = 255, message = "A senha deve ter no máximo 255 caracteres.")
        String senha
) { }
