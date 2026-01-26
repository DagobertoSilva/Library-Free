package libraryfree.app.controller.acervo;

import jakarta.validation.Valid;
import libraryfree.app.dto.emprestimo.EmprestimoRequest;
import libraryfree.app.dto.emprestimo.EmprestimoResponse;
import libraryfree.app.service.acervo.EmprestimoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/emprestimos")
public class EmprestimoController {
    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public ResponseEntity<EmprestimoResponse> create(@RequestBody @Valid EmprestimoRequest request) {
        EmprestimoResponse response = emprestimoService.create(request);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PostMapping("/{id}/devolucao")
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public ResponseEntity<Void> devolver(@PathVariable Long id) {
        emprestimoService.devolver(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO', 'ALUNO')")
    public ResponseEntity<Page<EmprestimoResponse>> listarTodos(
            @RequestParam(required = false) String nomeAluno,
            @RequestParam(required = false) String tituloLivro,
            @PageableDefault(size = 8, sort = "dataEmprestimo", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(emprestimoService.findAll(nomeAluno, tituloLivro, pageable));
    }
}