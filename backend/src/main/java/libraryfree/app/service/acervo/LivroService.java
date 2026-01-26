package libraryfree.app.service.acervo;

import libraryfree.app.domain.acervo.Autor;
import libraryfree.app.domain.acervo.Editora;
import libraryfree.app.domain.acervo.Genero;
import libraryfree.app.domain.acervo.Livro;
import libraryfree.app.dto.livro.CreateLivroRequest;
import libraryfree.app.dto.livro.LivroResponse;
import libraryfree.app.dto.livro.UpdateLivroRequest;
import libraryfree.app.exception.ConflitoDeDadosException;
import libraryfree.app.exception.RecursoNaoEncontradoException;
import libraryfree.app.mapper.LivroMapper;
import libraryfree.app.repository.acervo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class LivroService {
    private final LivroRepository livroRepository;
    private final LivroMapper livroMapper;
    private final EditoraRepository editoraRepository;
    private final AutorRepository autorRepository;
    private final GeneroRepository generoRepository;
    private final EmprestimoRepository emprestimoRepository;

    public LivroService(LivroRepository livroRepository,
                        LivroMapper livroMapper,
                        EditoraRepository editoraRepository,
                        AutorRepository autorRepository,
                        GeneroRepository generoRepository,
                        EmprestimoRepository emprestimoRepository) {
        this.livroRepository = livroRepository;
        this.livroMapper = livroMapper;
        this.editoraRepository = editoraRepository;
        this.autorRepository = autorRepository;
        this.generoRepository = generoRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    @Transactional
    public LivroResponse create(CreateLivroRequest request) {
        if (request.isbn() != null && livroRepository.existsByIsbn(request.isbn())) {
            throw new ConflitoDeDadosException("ISBN já cadastrado.");
        }

        Editora editora = null;
        if(request.editoraId() != null) {
            editora = editoraRepository
                    .findById(request.editoraId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Editora não encontrada."));
        }

        Set<Autor> autores = new HashSet<>();
        if (request.autoresIds() != null && !request.autoresIds().isEmpty()) {
            autores = new HashSet<>(autorRepository.findAllById(request.autoresIds()));

            if (autores.size() != request.autoresIds().size()) {
                throw new RecursoNaoEncontradoException("Um ou mais autores informados não foram encontrados.");
            }
        }

        Set<Genero> generos = new HashSet<>();
        if (request.generosIds() != null && !request.generosIds().isEmpty()) {
            generos = new HashSet<>(generoRepository.findAllById(request.generosIds()));

            if (generos.size() != request.generosIds().size()) {
                throw new RecursoNaoEncontradoException("Um ou mais gêneros informados não foram encontrados.");
            }
        }

        Livro livro = livroMapper.toEntity(request, editora, autores, generos);
        Livro livroSalvo = livroRepository.save(livro);
        return livroMapper.toResponse(livroSalvo);
    }

    @Transactional
    public LivroResponse buscarPorId(Long id) {
        return livroRepository.findById(id)
                .map(livroMapper::toResponse)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado."));
    }

    @Transactional
    public Page<LivroResponse> findAll(Pageable pageable) {
        return livroRepository.findAll(pageable).map(livroMapper::toResponse);
    }

    @Transactional
    public Page<LivroResponse> buscarPorTitulo(String titulo, Pageable pageable) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo, pageable).map(livroMapper::toResponse);
    }

    @Transactional
    public Page<LivroResponse> buscarPorAutor(String nomeAutor, Pageable pageable) {
        return livroRepository.findDistinctByAutores_NomeContainingIgnoreCase(nomeAutor, pageable).map(livroMapper::toResponse);
    }

    @Transactional
    public Page<LivroResponse> buscarPorGenero(String nomeGenero, Pageable pageable) {
        return livroRepository.findDistinctByGeneros_NomeContainingIgnoreCase(nomeGenero, pageable).map(livroMapper::toResponse);
    }

    @Transactional
    public LivroResponse update(Long id, UpdateLivroRequest request) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado."));

        if (request.quantidadeTotal() != null) {
            Integer totalAtual = livro.getQuantidadeTotal();
            Integer novoTotal = request.quantidadeTotal();

            if (!totalAtual.equals(novoTotal)) {
                int emprestados = totalAtual - livro.getQuantidadeDisponivel();

                // Quantidade total não pode ser menor que livros emprestados.
                if (novoTotal < emprestados) {
                    throw new ConflitoDeDadosException(
                            "Não é possível reduzir o total para " + novoTotal + ". Existem " + emprestados + " livros emprestados no momento."
                    );
                }

                livro.setQuantidadeTotal(novoTotal);
                livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + (novoTotal - totalAtual));
            }
        }

        if (request.isbn() != null && !request.isbn().equals(livro.getIsbn())) {
            // Não pode alterar ISBN para algum já existente.
            if (livroRepository.existsByIsbn(request.isbn())) {
                throw new ConflitoDeDadosException("ISBN já cadastrado.");
            }
        }

        if (request.editoraId() != null) {
            Editora editora = editoraRepository.findById(request.editoraId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Editora não encontrada."));
            livro.setEditora(editora);
        }

        // Se a lista for vazia, todos os autores serão removidos.
        if (request.autoresIds() != null) {
            Set<Autor> novosAutores = new HashSet<>();
            if (!request.autoresIds().isEmpty()) {
                novosAutores = new HashSet<>(autorRepository.findAllById(request.autoresIds()));
                if (novosAutores.size() != request.autoresIds().size()) {
                    throw new RecursoNaoEncontradoException("Um ou mais autores não foram encontrados.");
                }
            }
            livro.setAutores(novosAutores);
        }

        // Se a lista for vazia, todos os gêneros serão removidos.
        if (request.generosIds() != null) {
            Set<Genero> novosGeneros = new HashSet<>();
            if (!request.generosIds().isEmpty()) {
                novosGeneros = new HashSet<>(generoRepository.findAllById(request.generosIds()));
                if (novosGeneros.size() != request.generosIds().size()) {
                    throw new RecursoNaoEncontradoException("Um ou mais gêneros não foram encontrados.");
                }
            }
            livro.setGeneros(novosGeneros);
        }

        livroMapper.updateEntityFromDto(request, livro);
        Livro livroSalvo = livroRepository.save(livro);
        return livroMapper.toResponse(livroSalvo);
    }

    @Transactional
    public void delete(Long id) {
        if (!livroRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Livro não encontrado.");
        }

        if (emprestimoRepository.existsByLivroId(id)) {
            throw new ConflitoDeDadosException("Não é possível excluir o livro pois ele possui histórico de empréstimos vinculados.");
        }

        livroRepository.deleteById(id);
    }
}