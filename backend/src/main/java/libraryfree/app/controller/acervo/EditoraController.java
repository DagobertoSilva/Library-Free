package libraryfree.app.controller.acervo;

import jakarta.validation.Valid;
import libraryfree.app.dto.generico.IdNomeResponse;
import libraryfree.app.dto.generico.NomeRequest;
import libraryfree.app.service.acervo.EditoraService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/editoras")
public class EditoraController {
    private final EditoraService editoraService;

    public EditoraController(EditoraService editoraService) {
        this.editoraService = editoraService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO', 'ALUNO')")
    public ResponseEntity<List<IdNomeResponse>> listar() {
        return ResponseEntity.ok(editoraService.findAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public ResponseEntity<IdNomeResponse> create(@RequestBody @Valid NomeRequest request) {
        IdNomeResponse response = editoraService.create(request);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }
}
