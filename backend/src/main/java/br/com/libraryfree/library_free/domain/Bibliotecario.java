package br.com.libraryfree.library_free.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Bibliotecario")
@Getter
@Setter
@EqualsAndHashCode(of = "email")
public class Bibliotecario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 255)
    private String nome;
    @Column(nullable = false, length = 255, unique = true)
    private String email;
    @Column(nullable = false, length = 255)
    private String senha;

    public Bibliotecario() {}

    public Bibliotecario(Long id, String nome, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }
}
