package libraryfree.app.controller;

import jakarta.validation.Valid;
import libraryfree.app.dto.bibliotecario.CreateBibliotecarioRequest;
import libraryfree.app.service.BibliotecarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bibliotecarios")
public class BibliotecarioController {
    BibliotecarioService bibliotecarioService;

    public BibliotecarioController(BibliotecarioService bibliotecarioService) {
        this.bibliotecarioService = bibliotecarioService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateBibliotecarioRequest request){
        bibliotecarioService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
