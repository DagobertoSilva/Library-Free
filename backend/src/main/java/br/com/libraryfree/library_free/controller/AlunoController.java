package br.com.libraryfree.library_free.controller;

import br.com.libraryfree.library_free.domain.Aluno;
import br.com.libraryfree.library_free.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/libraryfree")
public class AlunoController {
    @Autowired
    AlunoService alunoService;

    @GetMapping("/alunos")
    public ResponseEntity<Iterable<Aluno>> findAll(){
        Iterable<Aluno> alunos = alunoService.findAll();
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/alunos/search")
    public ResponseEntity<Iterable<Aluno>> searchByNome(@RequestParam String nome) {
        Iterable<Aluno> alunos = alunoService.searchByNome(nome);
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/alunos/{requestedID}")
    public ResponseEntity<Aluno> findById(@PathVariable Long requestedID){
        Aluno aluno = alunoService.findById(requestedID);
        return ResponseEntity.ok(aluno);
    }

    @GetMapping("/alunos/matricula/{requestedMatricula}")
    public ResponseEntity<Aluno> findByMatricula(@PathVariable String requestedMatricula){
        Aluno aluno = alunoService.findByMatricula(requestedMatricula);
        return ResponseEntity.ok(aluno);
    }

    @PostMapping("/alunos")
    public ResponseEntity<Void> createAluno(@RequestBody Aluno newAlunoRequest, UriComponentsBuilder ucb){
        Aluno aluno = alunoService.createAluno(newAlunoRequest);
        URI locationOfNewAluno = ucb.path("/libraryfree/alunos/{id}").buildAndExpand(aluno.getId()).toUri();
        return ResponseEntity.created(locationOfNewAluno).build();
    }

    @PutMapping("/alunos/{requestedID}")
    public ResponseEntity<Void> editAluno(@PathVariable Long requestedID, @RequestBody Aluno alunoUpdate){
        alunoService.editAluno(requestedID, alunoUpdate);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/alunos/{requestedID}")
    public ResponseEntity<Void> deleteAluno(@PathVariable Long requestedID){
        alunoService.deleteAluno(requestedID);
        return ResponseEntity.noContent().build();
    }
}
