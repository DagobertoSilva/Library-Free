package libraryfree.app.service.acervo;

import libraryfree.app.domain.acervo.Emprestimo;
import libraryfree.app.domain.acervo.Livro;
import libraryfree.app.domain.usuario.Aluno;
import libraryfree.app.dto.emprestimo.EmprestimoRequest;
import libraryfree.app.dto.emprestimo.EmprestimoResponse;
import libraryfree.app.exception.ConflitoDeDadosException;
import libraryfree.app.exception.RecursoNaoEncontradoException;
import libraryfree.app.repository.acervo.EmprestimoRepository;
import libraryfree.app.repository.acervo.LivroRepository;
import libraryfree.app.repository.usuario.AlunoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class EmprestimoService {
    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;
    private final AlunoRepository alunoRepository;

    private static final int DIAS_PRAZO_EMPRESTIMO = 7;

    public EmprestimoService(EmprestimoRepository emprestimoRepository,
                             LivroRepository livroRepository,
                             AlunoRepository alunoRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.livroRepository = livroRepository;
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public EmprestimoResponse create(EmprestimoRequest request) {
        Livro livro = livroRepository.findById(request.livroId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado."));

        Aluno aluno = alunoRepository.findById(request.alunoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado."));

        if (livro.getQuantidadeDisponivel() <= 0) {
            throw new ConflitoDeDadosException("O livro '" + livro.getTitulo() + "' não possui exemplares disponíveis para empréstimo no momento.");
        }

        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);
        Emprestimo emprestimo = new Emprestimo(livro, aluno, DIAS_PRAZO_EMPRESTIMO);

        livroRepository.save(livro);
        emprestimoRepository.save(emprestimo);

        return new EmprestimoResponse(emprestimo);
    }

    @Transactional
    public void devolver(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Empréstimo não encontrado."));

        if (emprestimo.getDataDevolucao() != null) {
            throw new ConflitoDeDadosException("Este empréstimo já foi devolvido em: " + emprestimo.getDataDevolucao());
        }

        emprestimo.setDataDevolucao(LocalDateTime.now());
        Livro livro = emprestimo.getLivro();
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);

        livroRepository.save(livro);
        emprestimoRepository.save(emprestimo);
    }
}