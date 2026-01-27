package libraryfree.app.auth.strategy;

import libraryfree.app.dto.perfil.Perfil;
import libraryfree.app.enumerator.TipoUsuario;

public interface LoginStrategy {
    Perfil procurarPerfil(Long usuarioId);
    TipoUsuario getTipo();
}
