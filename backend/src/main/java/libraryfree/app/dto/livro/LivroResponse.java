package libraryfree.app.dto.livro;

import libraryfree.app.domain.acervo.Autor;
import libraryfree.app.domain.acervo.Editora;
import libraryfree.app.domain.acervo.Genero;

import java.util.Set;

public record LivroResponse(
        Long id,
        String isbn,
        String titulo,
        Integer anoPublicacao,
        Integer quantidadeTotal,
        Integer quantidadeDisponivel,
        Editora editora,
        Set<Autor> autores,
        Set<Genero> generos
) { }
