package libraryfree.app.controller.acervo;

import jakarta.validation.Valid;
import libraryfree.app.dto.generico.IdNomeResponse;
import libraryfree.app.dto.generico.NomeRequest;
import libraryfree.app.service.acervo.AutorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/autores")
public class AutorController {
    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO', 'ALUNO')")
    public ResponseEntity<List<IdNomeResponse>> listar() {
        return ResponseEntity.ok(autorService.findAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public ResponseEntity<IdNomeResponse> create(@RequestBody @Valid NomeRequest request) {
        IdNomeResponse response = autorService.create(request);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }
}
