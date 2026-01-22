package libraryfree.app.dto.bibliotecario;

import java.time.LocalDateTime;

public record BibliotecarioResponse(
        Long id,
        String nome,
        String email,
        String tipo
) {}