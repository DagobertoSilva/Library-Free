package libraryfree.app.service.acervo;

import libraryfree.app.domain.acervo.Editora;
import libraryfree.app.dto.generico.IdNomeResponse;
import libraryfree.app.dto.generico.NomeRequest;
import libraryfree.app.repository.acervo.EditoraRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EditoraService {
    private final EditoraRepository editoraRepository;

    public EditoraService(EditoraRepository editoraRepository) {
        this.editoraRepository = editoraRepository;
    }

    @Transactional(readOnly = true)
    public List<IdNomeResponse> findAll() {
        return editoraRepository.findAll(Sort.by("nome").ascending()).stream()
                .map(a -> new IdNomeResponse(a.getId(), a.getNome()))
                .toList();
    }

    @Transactional
    public IdNomeResponse create(NomeRequest request) {
        Editora editora = new Editora();
        editora.setNome(request.nome());
        editora = editoraRepository.save(editora);
        return new IdNomeResponse(editora.getId(), editora.getNome());
    }
}