package br.com.libraryfree.library_free.security;

import br.com.libraryfree.library_free.domain.Bibliotecario;
import br.com.libraryfree.library_free.repository.BibliotecarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    BibliotecarioRepository bibliotecarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Bibliotecario bibliotecario = bibliotecarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Email não encontrado."));
        return new UserDetailsImpl(bibliotecario);
    }
}
