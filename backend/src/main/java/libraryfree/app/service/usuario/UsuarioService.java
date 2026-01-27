package libraryfree.app.service.usuario;

import libraryfree.app.domain.usuario.Usuario;
import libraryfree.app.enumerator.TipoUsuario;
import libraryfree.app.exception.ConflitoDeDadosException;
import libraryfree.app.repository.usuario.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario create(String email, String senha, TipoUsuario tipo){
        if (usuarioRepository.existsByEmail(email)) {
            throw new ConflitoDeDadosException("Este e-mail já está cadastrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(senha)); // Aplica função BCrypt na senha do usuário
        usuario.setTipo(tipo);

        return usuarioRepository.save(usuario);
    }
}
