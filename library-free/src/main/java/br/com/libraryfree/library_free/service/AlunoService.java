package br.com.libraryfree.library_free.service;

import br.com.libraryfree.library_free.domain.Aluno;
import br.com.libraryfree.library_free.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AlunoService {
    @Autowired
    AlunoRepository alunoRepository;

    public Iterable<Aluno> findAll(){
        return alunoRepository.findAll();
    }

    public Aluno findById(Long id){
        return alunoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado."));
    }

    public Aluno createAluno(Aluno aluno){
        alunoRepository.findByMatricula(aluno.getMatricula())
                .ifPresent(existing -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Matrícula já existente.");
                });

        alunoRepository.findByCpf(aluno.getCpf())
                .ifPresent(existing -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF já existente.");
                });

        return alunoRepository.save(aluno);
    }

    public void editAluno(Long id, Aluno aluno){
        Aluno existingAluno = alunoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado."));

        alunoRepository.findByMatricula(aluno.getMatricula())
                .filter(outroAluno -> !outroAluno.getId().equals(id))
                .ifPresent(conflicting -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Matrícula já está em uso por outro aluno.");
                });

        alunoRepository.findByCpf(aluno.getCpf())
                .filter(outroAluno -> !outroAluno.getId().equals(id))
                .ifPresent(conflicting -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF já está em uso por outro aluno.");
                });

        existingAluno.setNome(aluno.getNome());
        existingAluno.setMatricula(aluno.getMatricula());
        existingAluno.setCpf(aluno.getCpf());
        existingAluno.setAtivo(aluno.getAtivo());
        alunoRepository.save(existingAluno);
    }

    public void deleteAluno(Long id){
        if (!alunoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado.");
        }
        alunoRepository.deleteById(id);
    }
}
