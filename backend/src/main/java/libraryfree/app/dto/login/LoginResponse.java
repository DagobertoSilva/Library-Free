package libraryfree.app.dto.login;

import libraryfree.app.dto.perfil.Perfil;

public record LoginResponse(
        String token,
        String tipo,
        Perfil perfil
) { }
