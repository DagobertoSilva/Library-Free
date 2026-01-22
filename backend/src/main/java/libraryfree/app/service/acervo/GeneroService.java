package libraryfree.app.service.acervo;

import libraryfree.app.domain.acervo.Genero;
import libraryfree.app.dto.generico.IdNomeResponse;
import libraryfree.app.dto.generico.NomeRequest;
import libraryfree.app.repository.acervo.GeneroRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GeneroService {
    private final GeneroRepository generoRepository;

    public GeneroService(GeneroRepository generoRepository) {
        this.generoRepository = generoRepository;
    }

    @Transactional(readOnly = true)
    public List<IdNomeResponse> findAll() {
        return generoRepository.findAll(Sort.by("nome").ascending()).stream()
                .map(a -> new IdNomeResponse(a.getId(), a.getNome()))
                .toList();
    }

    @Transactional
    public IdNomeResponse create(NomeRequest request) {
        Genero genero = new Genero();
        genero.setNome(request.nome());
        genero = generoRepository.save(genero);
        return new IdNomeResponse(genero.getId(), genero.getNome());
    }
}