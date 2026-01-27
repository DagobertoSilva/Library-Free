package libraryfree.app.auth;

import jakarta.validation.Valid;
import libraryfree.app.dto.login.LoginRequest;
import libraryfree.app.dto.login.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<LoginResponse> recuperarUsuarioLogado() {
        LoginResponse response = authService.recuperarUsuarioLogado();
        return ResponseEntity.ok(response);
    }
}
