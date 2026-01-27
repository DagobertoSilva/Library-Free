package libraryfree.app.service.usuario;

import libraryfree.app.domain.usuario.Bibliotecario;
import libraryfree.app.domain.usuario.Usuario;
import libraryfree.app.dto.bibliotecario.BibliotecarioResponse;
import libraryfree.app.dto.bibliotecario.CreateBibliotecarioRequest;
import libraryfree.app.enumerator.TipoUsuario;
import libraryfree.app.repository.usuario.BibliotecarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BibliotecarioService {
    private final UsuarioService usuarioService;
    private final BibliotecarioRepository bibliotecarioRepository;

    public BibliotecarioService(UsuarioService usuarioService, BibliotecarioRepository bibliotecarioRepository) {
        this.usuarioService = usuarioService;
        this.bibliotecarioRepository = bibliotecarioRepository;
    }

    @Transactional
    public BibliotecarioResponse create(CreateBibliotecarioRequest request) {
        Usuario usuario = usuarioService.create(request.email(), request.senha(), TipoUsuario.BIBLIOTECARIO);

        Bibliotecario bibliotecario = new Bibliotecario();
        bibliotecario.setNome(request.nome());
        bibliotecario.setUsuario(usuario);

        Bibliotecario bibliotecarioSalvo = bibliotecarioRepository.save(bibliotecario);

        return new BibliotecarioResponse(
                bibliotecarioSalvo.getId(),
                bibliotecarioSalvo.getNome(),
                usuario.getEmail(),
                usuario.getTipo().name()
        );
    }
}