package libraryfree.app.auth.strategy;

import libraryfree.app.domain.usuario.Bibliotecario;
import libraryfree.app.dto.perfil.BibliotecarioPerfil;
import libraryfree.app.dto.perfil.Perfil;
import libraryfree.app.enumerator.TipoUsuario;
import libraryfree.app.exception.RecursoNaoEncontradoException;
import libraryfree.app.repository.BibliotecarioRepository;
import org.springframework.stereotype.Component;

@Component
public class BibliotecarioLoginStrategy implements LoginStrategy {
    private final BibliotecarioRepository bibliotecarioRepository;

    public BibliotecarioLoginStrategy(BibliotecarioRepository bibliotecarioRepository) {
        this.bibliotecarioRepository = bibliotecarioRepository;
    }

    @Override
    public Perfil procurarPerfil(Long usuarioId) {
        Bibliotecario bibliotecario = bibliotecarioRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Bibliotecario não encontrado."));

        return new BibliotecarioPerfil(bibliotecario.getId(), bibliotecario.getNome());
    }

    @Override
    public TipoUsuario getTipo(){
        return TipoUsuario.BIBLIOTECARIO;
    }
}
