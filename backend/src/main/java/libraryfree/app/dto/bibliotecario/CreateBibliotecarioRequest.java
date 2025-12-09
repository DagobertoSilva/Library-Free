package libraryfree.app.dto.bibliotecario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateBibliotecarioRequest(
    @NotBlank
    @Email
    String email,

    @NotBlank
    String senha,

    @NotBlank
    String nome
) { }
