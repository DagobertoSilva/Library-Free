package libraryfree.app.auth;

import libraryfree.app.auth.strategy.LoginFactory;
import libraryfree.app.domain.Usuario;
import libraryfree.app.dto.login.LoginRequest;
import libraryfree.app.dto.login.LoginResponse;
import libraryfree.app.dto.perfil.Perfil;
import libraryfree.app.security.UsuarioDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final LoginFactory loginFactory;
    private final AuthenticationManager authenticationManager;

    public AuthService(AuthenticationManager authenticationManager, LoginFactory loginFactory) {
        this.authenticationManager = authenticationManager;
        this.loginFactory = loginFactory;
    }

    public LoginResponse login(LoginRequest request){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
        Authentication authentication = authenticationManager.authenticate(authToken);

        Usuario usuario = ((UsuarioDetails) authentication.getPrincipal()).getUsuario();

        Perfil perfil = loginFactory.getStrategy(usuario.getTipo()).procurarPerfil(usuario.getId());

        return new LoginResponse(usuario.getTipo().name(), perfil);
    }
}