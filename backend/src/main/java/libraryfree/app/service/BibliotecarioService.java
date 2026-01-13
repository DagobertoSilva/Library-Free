package libraryfree.app.service;

import jakarta.transaction.Transactional;
import libraryfree.app.domain.usuario.Bibliotecario;
import libraryfree.app.domain.usuario.Usuario;
import libraryfree.app.dto.bibliotecario.CreateBibliotecarioRequest;
import libraryfree.app.enumerator.TipoUsuario;
import libraryfree.app.repository.BibliotecarioRepository;
import org.springframework.stereotype.Service;

@Service
public class BibliotecarioService {
    private final UsuarioService usuarioService;
    private final BibliotecarioRepository bibliotecarioRepository;

    public BibliotecarioService(UsuarioService usuarioService, BibliotecarioRepository bibliotecarioRepository) {
        this.usuarioService = usuarioService;
        this.bibliotecarioRepository = bibliotecarioRepository;
    }

    @Transactional
    public void create(CreateBibliotecarioRequest request){
        Usuario usuario = usuarioService.create(request.email(), request.senha(), TipoUsuario.BIBLIOTECARIO);

        Bibliotecario bibliotecario = new Bibliotecario();
        bibliotecario.setNome(request.nome());
        bibliotecario.setUsuario(usuario);

        bibliotecarioRepository.save(bibliotecario);
    }
}