package libraryfree.app.controller;

import jakarta.validation.Valid;
import libraryfree.app.dto.livro.CreateLivroRequest;
import libraryfree.app.dto.livro.LivroResponse;
import libraryfree.app.service.LivroService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/livros")
public class LivroController {
    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    public ResponseEntity<LivroResponse> create(@Valid @RequestBody CreateLivroRequest request){
        LivroResponse response = livroService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO', 'ALUNO')")
    public ResponseEntity<LivroResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(livroService.buscarPorId(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO', 'ALUNO')")
    public ResponseEntity<Page<LivroResponse>> listarTodos(@PageableDefault(size = 8, sort = "titulo") Pageable pageable) {
        return ResponseEntity.ok(livroService.findAll(pageable));
    }

    @GetMapping("/titulo")
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO', 'ALUNO')")
    public ResponseEntity<Page<LivroResponse>> buscarPorTitulo(@RequestParam String termo, @PageableDefault(size = 8, sort = "titulo") Pageable pageable) {
        return ResponseEntity.ok(livroService.buscarPorTitulo(termo, pageable));
    }

    @GetMapping("/autor")
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO', 'ALUNO')")
    public ResponseEntity<Page<LivroResponse>> buscarPorAutor(@RequestParam String termo, @PageableDefault(size = 8, sort = "titulo") Pageable pageable) {
        return ResponseEntity.ok(livroService.buscarPorAutor(termo, pageable));
    }

    @GetMapping("/genero")
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO', 'ALUNO')")
    public ResponseEntity<Page<LivroResponse>> buscarPorGenero(@RequestParam String termo, @PageableDefault(size = 8, sort = "titulo") Pageable pageable) {
        return ResponseEntity.ok(livroService.buscarPorGenero(termo, pageable));
    }
}
