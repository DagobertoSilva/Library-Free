package libraryfree.app.dto.login;

import libraryfree.app.dto.perfil.Perfil;

public record ResponseRequest(
        String tipo,
        Perfil perfil
) { }
