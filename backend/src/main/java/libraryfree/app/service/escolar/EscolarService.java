package libraryfree.app.service.escolar;

import libraryfree.app.dto.escolar.CursoResponse;
import libraryfree.app.dto.escolar.TurmaResponse;
import libraryfree.app.repository.escolar.CursoRepository;
import libraryfree.app.repository.escolar.TurmaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EscolarService {
    private final CursoRepository cursoRepository;
    private final TurmaRepository turmaRepository;

    public EscolarService(CursoRepository cursoRepository, TurmaRepository turmaRepository) {
        this.cursoRepository = cursoRepository;
        this.turmaRepository = turmaRepository;
    }

    public List<CursoResponse> listarCursos() {
        return cursoRepository.findAllByOrderByNomeAsc()
                .stream()
                .map(CursoResponse::new)
                .toList();
    }

    public List<TurmaResponse> listarTurmas(Long cursoId) {
        var turmas = (cursoId != null)
                ? turmaRepository.findByCursoId(cursoId)
                : turmaRepository.findAllByOrderBySerieAsc();

        return turmas.stream()
                .map(TurmaResponse::new)
                .toList();
    }
}
