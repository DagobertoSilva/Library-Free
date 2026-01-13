package libraryfree.app.repository;

import libraryfree.app.domain.usuario.Bibliotecario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BibliotecarioRepository extends JpaRepository<Bibliotecario, Long> {
    Optional<Bibliotecario> findByUsuarioId(Long id);
}
