package br.com.libraryfree.library_free.service;

import br.com.libraryfree.library_free.domain.Livro;
import br.com.libraryfree.library_free.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

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

    public void editLivro(Long id, Livro livro){
        Livro existingLivro = livroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado."));

        livroRepository.findByIsbn(livro.getIsbn())
                .filter(outroLivro -> !outroLivro.getId().equals(id))
                .ifPresent(conflicting -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ISBN já está em uso por outro livro.");
                });

        existingLivro.setTitulo(livro.getTitulo());
        existingLivro.setIsbn(livro.getIsbn());
        existingLivro.setAutor(livro.getAutor());
        existingLivro.setGenero(livro.getGenero());
        existingLivro.setEdicao(livro.getEdicao());
        existingLivro.setPublicacao(livro.getPublicacao());
        existingLivro.setEditora(livro.getEditora());
        existingLivro.setEmprestado(livro.getEmprestado());
        livroRepository.save(existingLivro);
    }

    public void deleteLivro(Long id){
        if (!livroRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado.");
        }
        livroRepository.deleteById(id);
    }
}
