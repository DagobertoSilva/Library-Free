package br.com.libraryfree.library_free.controller;

import br.com.libraryfree.library_free.domain.Livro;
import br.com.libraryfree.library_free.service.LivroService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

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

    @GetMapping("/livros/{requestedID}")
    public ResponseEntity<Livro> findById(@PathVariable Long requestedID){
        Livro livro = livroService.findById(requestedID);
        return ResponseEntity.ok(livro);
    }

    @PostMapping("/livros")
    public ResponseEntity<Void> createLivro(@RequestBody Livro newLivroRequest, UriComponentsBuilder ucb){
        Livro livro = livroService.createLivro(newLivroRequest);
        URI locationOfNewLivro = ucb.path("/libraryfree/livros/{id}").buildAndExpand(livro.getId()).toUri();
        return ResponseEntity.created(locationOfNewLivro).build();
    }

    @PutMapping("/livros/{requestedID}")
    public ResponseEntity<Void> editLivro(@PathVariable Long requestedID, @RequestBody Livro livroUpdate){
        livroService.editLivro(requestedID, livroUpdate);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/livros/{requestedID}")
    public ResponseEntity<Void> deleteLivro(@PathVariable Long requestedID){
        livroService.deleteLivro(requestedID);
        return ResponseEntity.noContent().build();
    }
}
