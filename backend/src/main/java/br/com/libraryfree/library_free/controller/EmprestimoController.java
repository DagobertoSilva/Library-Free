package br.com.libraryfree.library_free.controller;

import br.com.libraryfree.library_free.DTO.EmprestimoRequestDTO;
import br.com.libraryfree.library_free.DTO.EmprestimoResponseDTO;
import br.com.libraryfree.library_free.service.EmprestimoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/libraryfree")
public class EmprestimoController {
    @Autowired
    EmprestimoService emprestimoService;

    @GetMapping("/emprestimos")
    public ResponseEntity<List<EmprestimoResponseDTO>> findAll(){
        List<EmprestimoResponseDTO> emprestimos = emprestimoService.findAll();
        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping("/emprestimos/{requestedID}")
    public ResponseEntity<EmprestimoResponseDTO> findById(@PathVariable Long requestedID){
        EmprestimoResponseDTO emprestimo = emprestimoService.findById(requestedID);
        return ResponseEntity.ok(emprestimo);
    }

    @PostMapping("/emprestimos")
    public ResponseEntity<Void> createEmprestimo(@RequestBody EmprestimoRequestDTO newEmprestimoRequest, UriComponentsBuilder ucb){
        EmprestimoResponseDTO emprestimo = emprestimoService.createEmprestimo(newEmprestimoRequest);
        URI locationOfNewEmprestimo = ucb.path("/libraryfree/emprestimos/{id}").buildAndExpand(emprestimo.getId()).toUri();
        return ResponseEntity.created(locationOfNewEmprestimo).build();
    }

    @PutMapping("/emprestimos/{livroID}")
    public ResponseEntity<Void> editEmprestimo(@PathVariable Long livroID){
        emprestimoService.editEmprestimo(livroID);
        return ResponseEntity.noContent().build();
    }
}
