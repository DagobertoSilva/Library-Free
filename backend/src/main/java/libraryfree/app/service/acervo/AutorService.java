package libraryfree.app.service.acervo;

import libraryfree.app.domain.acervo.Autor;
import libraryfree.app.dto.generico.IdNomeResponse;
import libraryfree.app.dto.generico.NomeRequest;
import libraryfree.app.repository.acervo.AutorRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AutorService {
    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Transactional(readOnly = true)
    public List<IdNomeResponse> findAll() {
        return autorRepository.findAll(Sort.by("nome").ascending()).stream()
                .map(a -> new IdNomeResponse(a.getId(), a.getNome()))
                .toList();
    }

    @Transactional
    public IdNomeResponse create(NomeRequest request) {
        Autor autor = new Autor();
        autor.setNome(request.nome());
        autor = autorRepository.save(autor);
        return new IdNomeResponse(autor.getId(), autor.getNome());
    }
}