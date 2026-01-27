package libraryfree.app.controller.escolar;

import libraryfree.app.dto.escolar.CursoResponse;
import libraryfree.app.dto.escolar.TurmaResponse;
import libraryfree.app.service.escolar.EscolarService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/escolar")
public class EscolarController {
    private final EscolarService escolarService;

    public EscolarController(EscolarService escolarService) {
        this.escolarService = escolarService;
    }

    @GetMapping("/cursos")
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public ResponseEntity<List<CursoResponse>> listarCursos() {
        return ResponseEntity.ok(escolarService.listarCursos());
    }

    @GetMapping("/turmas")
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public ResponseEntity<List<TurmaResponse>> listarTurmas(@RequestParam(required = false) Long cursoId) {
        return ResponseEntity.ok(escolarService.listarTurmas(cursoId));
    }
}