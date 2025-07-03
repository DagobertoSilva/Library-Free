package br.com.libraryfree.library_free.controller;

import br.com.libraryfree.library_free.DTO.LoginDTO;
import br.com.libraryfree.library_free.DTO.BibliotecarioDTO;
import br.com.libraryfree.library_free.service.BibliotecarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/libraryfree")
public class BibliotecarioController {
    @Autowired
    private BibliotecarioService bibliotecarioService;

    @PostMapping("/auth/login")
    public ResponseEntity<BibliotecarioDTO> login(@RequestBody LoginDTO loginDTO) {
        try {
            BibliotecarioDTO bibliotecarioDTO = bibliotecarioService.login(loginDTO);
            return ResponseEntity.ok(bibliotecarioDTO);
        } catch (UsernameNotFoundException | BadCredentialsException error) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
