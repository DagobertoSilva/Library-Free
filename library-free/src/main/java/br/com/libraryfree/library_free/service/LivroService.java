package br.com.libraryfree.library_free.service;

import br.com.libraryfree.library_free.domain.Livro;
import br.com.libraryfree.library_free.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LivroService {
    @Autowired
    private LivroRepository livroRepository;

    public Iterable<Livro> findAll(){
        return livroRepository.findAll();
    }

    public Livro findById(Long id){
        return livroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado."));

    }

    public Livro createLivro(Livro livro){
        livroRepository.findByIsbn(livro.getIsbn())
                .ifPresent(existing -> { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ISBN já existente."); });
        return livroRepository.save(livro);
    }
}
