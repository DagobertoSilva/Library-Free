package br.com.libraryfree.library_free.domain;

import java.util.List;

public record User(Long id, String email, String password, String registration, String name,
                   String cpf, String status, List<Role> roles) {
}
