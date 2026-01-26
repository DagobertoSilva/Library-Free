package libraryfree.app.controller.usuario;

import jakarta.validation.Valid;
import libraryfree.app.dto.aluno.CreateAlunoRequest;
import libraryfree.app.dto.aluno.UpdateAlunoRequest;
import libraryfree.app.dto.aluno.AlunoResponse;
import libraryfree.app.service.usuario.AlunoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/alunos")
public class AlunoController {
    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public ResponseEntity<AlunoResponse> create(@RequestBody @Valid CreateAlunoRequest request) {
        AlunoResponse response = alunoService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO')")
    public ResponseEntity<Page<AlunoResponse>> findAll(
            @RequestParam(required = false) Long cursoId,
            @RequestParam(required = false) Long turmaId,
            @PageableDefault(size = 8, sort = "nome") Pageable pageable
    ) {
        return ResponseEntity.ok(alunoService.findAll(cursoId, turmaId, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public ResponseEntity<AlunoResponse> update(@PathVariable Long id, @RequestBody @Valid UpdateAlunoRequest request) {
        return ResponseEntity.ok(alunoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        alunoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}