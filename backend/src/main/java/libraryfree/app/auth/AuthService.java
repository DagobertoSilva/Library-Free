package libraryfree.app.auth;

import libraryfree.app.auth.strategy.LoginFactory;
import libraryfree.app.auth.strategy.LoginStrategy;
import libraryfree.app.domain.Usuario;
import libraryfree.app.dto.login.LoginRequest;
import libraryfree.app.dto.login.LoginResponse;
import libraryfree.app.dto.perfil.Perfil;
import libraryfree.app.exception.CredenciaisInvalidasException;
import libraryfree.app.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final LoginFactory loginFactory;

    public AuthService(UsuarioRepository usuarioRepository, LoginFactory loginFactory) {
        this.usuarioRepository = usuarioRepository;
        this.loginFactory = loginFactory;
    }

    public LoginResponse login(LoginRequest request){
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new CredenciaisInvalidasException("Credenciais inválidas."));

        if (!usuario.getSenha().equals(request.senha())) {
            throw new CredenciaisInvalidasException("Credenciais inválidas.");
        }

        LoginStrategy strategy = loginFactory.getStrategy(usuario.getTipo());
        Perfil perfil = strategy.procurarPerfil(usuario.getId());
        return new LoginResponse(usuario.getTipo().name(), perfil);
    }
}
