package br.com.libraryfree.library_free.repository;

import br.com.libraryfree.library_free.domain.Bibliotecario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BibliotecarioRepository extends CrudRepository<Bibliotecario, Long> {
    Optional<Bibliotecario> findByEmail(String email);
}
