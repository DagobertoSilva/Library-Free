package libraryfree.app.service;

import libraryfree.app.domain.acervo.Autor;
import libraryfree.app.domain.acervo.Editora;
import libraryfree.app.domain.acervo.Genero;
import libraryfree.app.domain.acervo.Livro;
import libraryfree.app.dto.livro.CreateLivroRequest;
import libraryfree.app.dto.livro.LivroResponse;
import libraryfree.app.exception.ConflitoDeDadosException;
import libraryfree.app.exception.RecursoNaoEncontradoException;
import libraryfree.app.mapper.LivroMapper;
import libraryfree.app.repository.acervo.AutorRepository;
import libraryfree.app.repository.acervo.EditoraRepository;
import libraryfree.app.repository.acervo.GeneroRepository;
import libraryfree.app.repository.acervo.LivroRepository;
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

    public LivroService(LivroRepository livroRepository,
                        LivroMapper livroMapper,
                        EditoraRepository editoraRepository,
                        AutorRepository autorRepository,
                        GeneroRepository generoRepository) {
        this.livroRepository = livroRepository;
        this.livroMapper = livroMapper;
        this.editoraRepository = editoraRepository;
        this.autorRepository = autorRepository;
        this.generoRepository = generoRepository;
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
}