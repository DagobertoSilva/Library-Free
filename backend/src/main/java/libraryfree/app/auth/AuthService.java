package libraryfree.app.auth;

import libraryfree.app.auth.strategy.LoginFactory;
import libraryfree.app.domain.usuario.Usuario;
import libraryfree.app.dto.login.LoginRequest;
import libraryfree.app.dto.login.LoginResponse;
import libraryfree.app.dto.perfil.Perfil;
import libraryfree.app.security.TokenService;
import libraryfree.app.security.UsuarioDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final LoginFactory loginFactory;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthService(AuthenticationManager authenticationManager, LoginFactory loginFactory, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.loginFactory = loginFactory;
        this.tokenService = tokenService;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
        Authentication authentication = authenticationManager.authenticate(authToken);

        UsuarioDetails usuarioDetails = (UsuarioDetails) authentication.getPrincipal();
        Usuario usuario = usuarioDetails.getUsuario();

        String token = tokenService.generateToken(usuarioDetails);

        Perfil perfil = loginFactory.getStrategy(usuario.getTipo()).procurarPerfil(usuario.getId());

        return new LoginResponse(token, usuario.getTipo().name(), perfil);
    }

    @Transactional(readOnly = true)
    public LoginResponse recuperarUsuarioLogado() {
        // Recupera o usuário via contexto Spring (Token já foi validado)
        UsuarioDetails usuarioDetails = (UsuarioDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Usuario usuario = usuarioDetails.getUsuario();

        // Renova o token desse usuário
        String novoToken = tokenService.generateToken(usuarioDetails);

        Perfil perfil = loginFactory.getStrategy(usuario.getTipo()).procurarPerfil(usuario.getId());

        return new LoginResponse(novoToken, usuario.getTipo().name(), perfil);
    }
}