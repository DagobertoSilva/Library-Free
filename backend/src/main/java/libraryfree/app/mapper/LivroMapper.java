package libraryfree.app.mapper;

import libraryfree.app.domain.acervo.Autor;
import libraryfree.app.domain.acervo.Editora;
import libraryfree.app.domain.acervo.Genero;
import libraryfree.app.domain.acervo.Livro;
import libraryfree.app.dto.livro.CreateLivroRequest;
import libraryfree.app.dto.livro.LivroResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface LivroMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quantidadeDisponivel", ignore = true)
    Livro toEntity(CreateLivroRequest request, Editora editora, Set<Autor> autores, Set<Genero> generos);

    @AfterMapping
    default void definirValoresIniciais(CreateLivroRequest request, @MappingTarget Livro livro) {
        livro.setQuantidadeDisponivel(request.quantidadeTotal());
    }

    LivroResponse toResponse(Livro livro);
}