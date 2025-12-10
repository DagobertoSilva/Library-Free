package libraryfree.app.dto.login;

import libraryfree.app.dto.perfil.Perfil;

public record LoginResponse(
        String tipo,
        Perfil perfil
) { }
