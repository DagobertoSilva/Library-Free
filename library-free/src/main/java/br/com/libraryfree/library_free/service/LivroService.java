package br.com.libraryfree.library_free.service;

import br.com.libraryfree.library_free.domain.Livro;
import br.com.libraryfree.library_free.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LivroService {
    @Autowired
    private LivroRepository livroRepository;

    public Iterable<Livro> findAll(){
        return livroRepository.findAll();
    }
}
