package br.com.libraryfree.library_free.controller;

import br.com.libraryfree.library_free.domain.Livro;
import br.com.libraryfree.library_free.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/libraryfree")
public class LivroController {
    @Autowired
    LivroService livroService;

    @GetMapping("/livros")
    public ResponseEntity<Iterable<Livro>> findAll(){
        Iterable<Livro> livros = livroService.findAll();
        return ResponseEntity.ok(livros);
    }
}
