package libraryfree.app.dto.bibliotecario;

public record BibliotecarioResponse(
        Long id,
        String nome,
        String email,
        String tipo
) {}