package br.com.libraryfree.library_free.DTO;

import java.time.LocalDate;

public record EmprestimoRequestDTO(Long alunoId, Long livroId, LocalDate prazoDevolucao) {
}
