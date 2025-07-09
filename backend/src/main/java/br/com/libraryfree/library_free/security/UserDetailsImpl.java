package br.com.libraryfree.library_free.security;

import br.com.libraryfree.library_free.domain.Bibliotecario;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class UserDetailsImpl implements UserDetails {
    private final Bibliotecario bibliotecario;

    UserDetailsImpl(Bibliotecario bibliotecario){
        this.bibliotecario = bibliotecario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return bibliotecario.getSenha();
    }

    @Override
    public String getUsername() {
        return bibliotecario.getEmail();
    }
}
