package br.com.libraryfree.library_free.service;

import br.com.libraryfree.library_free.DTO.LoginDTO;
import br.com.libraryfree.library_free.DTO.BibliotecarioDTO;
import br.com.libraryfree.library_free.domain.Bibliotecario;
import br.com.libraryfree.library_free.repository.BibliotecarioRepository;

import br.com.libraryfree.library_free.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class BibliotecarioService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    BibliotecarioRepository bibliotecarioRepository;

    public BibliotecarioDTO login(LoginDTO loginDTO){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.senha());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Bibliotecario bibliotecario = userDetails.getBibliotecario();
        return new BibliotecarioDTO(bibliotecario.getId(), bibliotecario.getNome(), bibliotecario.getEmail());
    }
}
