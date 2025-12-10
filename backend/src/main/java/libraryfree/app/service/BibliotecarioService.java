package libraryfree.app.service;

import jakarta.transaction.Transactional;
import libraryfree.app.domain.Bibliotecario;
import libraryfree.app.domain.Usuario;
import libraryfree.app.dto.bibliotecario.CreateBibliotecarioRequest;
import libraryfree.app.enumerator.TipoUsuario;
import libraryfree.app.exception.EmailJaCadastradoException;
import libraryfree.app.repository.BibliotecarioRepository;
import libraryfree.app.repository.UsuarioRepository;
import org.springframework.stereotype.Service;


@Service
public class BibliotecarioService {
    private final UsuarioRepository usuarioRepository;
    private final BibliotecarioRepository bibliotecarioRepository;

    public BibliotecarioService(UsuarioRepository usuarioRepository, BibliotecarioRepository bibliotecarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.bibliotecarioRepository = bibliotecarioRepository;
    }

    @Transactional
    public void create(CreateBibliotecarioRequest request){
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new EmailJaCadastradoException("Este e-mail já está cadastrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.email());
        usuario.setSenha(request.senha());
        usuario.setTipo(TipoUsuario.BIBLIOTECARIO);

        usuario = usuarioRepository.save(usuario);

        Bibliotecario bibliotecario = new Bibliotecario();
        bibliotecario.setNome(request.nome());
        bibliotecario.setUsuario(usuario);

        bibliotecarioRepository.save(bibliotecario);
    }
}
