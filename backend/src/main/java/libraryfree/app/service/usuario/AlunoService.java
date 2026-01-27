package libraryfree.app.service.usuario;

import libraryfree.app.domain.acervo.Editora;
import libraryfree.app.domain.escolar.Turma;
import libraryfree.app.domain.usuario.Aluno;
import libraryfree.app.dto.aluno.CreateAlunoRequest;
import libraryfree.app.dto.aluno.AlunoResponse;
import libraryfree.app.dto.aluno.UpdateAlunoRequest;
import libraryfree.app.exception.ConflitoDeDadosException;
import libraryfree.app.exception.RecursoNaoEncontradoException;
import libraryfree.app.repository.acervo.EmprestimoRepository;
import libraryfree.app.repository.escolar.TurmaRepository;
import libraryfree.app.repository.usuario.AlunoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlunoService {
    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;
    private final EmprestimoRepository emprestimoRepository;

    public AlunoService(AlunoRepository alunoRepository, TurmaRepository turmaRepository, EmprestimoRepository emprestimoRepository) {
        this.alunoRepository = alunoRepository;
        this.turmaRepository = turmaRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    @Transactional
    public AlunoResponse create(CreateAlunoRequest request) {
        if (alunoRepository.existsByMatricula(request.matricula())) {
            throw new ConflitoDeDadosException("Matricula já cadastrada");
        }

        Turma turma = turmaRepository
                .findById(request.turmaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Turma não encontrada."));

        Aluno aluno = new Aluno();
        aluno.setNome(request.nome());
        aluno.setMatricula(request.matricula());
        aluno.setTurma(turma);

        alunoRepository.save(aluno);
        return new AlunoResponse(aluno);
    }

    @Transactional()
    public AlunoResponse findById(Long id) {
        return alunoRepository.findById(id)
                .map(AlunoResponse::new)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado."));
    }

    public Page<AlunoResponse> findAll(Long cursoId, Long turmaId, Pageable pageable) {
        return alunoRepository.findAllWithFilters(cursoId, turmaId, pageable).map(AlunoResponse::new);
    }

    @Transactional
    public AlunoResponse update(Long id, UpdateAlunoRequest request) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado."));

        if (request.matricula() != null && !request.matricula().equals(aluno.getMatricula())) {
            // Não pode alterar matrícula para alguma já existente.
            if (alunoRepository.existsByMatricula(request.matricula())) {
                throw new ConflitoDeDadosException("Matrícula já cadastrado.");
            }
            aluno.setMatricula(request.matricula());
        }

        if (request.turmaId() != null) {
            Turma turma = turmaRepository.findById(request.turmaId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Turma não encontrada."));
            aluno.setTurma(turma);
        }

        if(request.nome() != null){
            aluno.setNome(request.nome());
        }

        alunoRepository.save(aluno);
        return new AlunoResponse(aluno);
    }

    @Transactional
    public void delete(Long id) {
        if (!alunoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Aluno não encontrado.");
        }

        if (emprestimoRepository.existsByAlunoId(id)) {
            throw new ConflitoDeDadosException("Não é possível excluir o aluno pois ele possui histórico de empréstimos vinculados.");
        }

        alunoRepository.deleteById(id);
    }
}