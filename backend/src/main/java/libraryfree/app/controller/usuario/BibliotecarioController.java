package libraryfree.app.controller.usuario;

import jakarta.validation.Valid;
import libraryfree.app.dto.bibliotecario.BibliotecarioResponse;
import libraryfree.app.dto.bibliotecario.CreateBibliotecarioRequest;
import libraryfree.app.service.usuario.BibliotecarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/bibliotecarios")
public class BibliotecarioController {
    private final BibliotecarioService bibliotecarioService;

    public BibliotecarioController(BibliotecarioService bibliotecarioService) {
        this.bibliotecarioService = bibliotecarioService;
    }

    @PostMapping
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public ResponseEntity<BibliotecarioResponse> create(@Valid @RequestBody CreateBibliotecarioRequest request){
        BibliotecarioResponse response = bibliotecarioService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(location).body(response);
    }
}