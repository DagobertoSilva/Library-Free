package br.com.libraryfree.library_free.service;

import br.com.libraryfree.library_free.DTO.BibliotecarioDTO;
import br.com.libraryfree.library_free.domain.Bibliotecario;
import br.com.libraryfree.library_free.repository.BibliotecarioRepository;

import br.com.libraryfree.library_free.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class BibliotecarioService {
    @Autowired
    BibliotecarioRepository bibliotecarioRepository;

    public BibliotecarioDTO getBibliotecarioDTO(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Bibliotecario bibliotecario = userDetails.getBibliotecario();
        return new BibliotecarioDTO(bibliotecario.getId(), bibliotecario.getNome(), bibliotecario.getEmail());
    }
}
