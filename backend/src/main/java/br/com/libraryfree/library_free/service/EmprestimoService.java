package br.com.libraryfree.library_free.service;

import br.com.libraryfree.library_free.DTO.EmprestimoRequestDTO;
import br.com.libraryfree.library_free.DTO.EmprestimoResponseDTO;
import br.com.libraryfree.library_free.domain.Aluno;
import br.com.libraryfree.library_free.domain.Emprestimo;
import br.com.libraryfree.library_free.domain.Livro;
import br.com.libraryfree.library_free.repository.AlunoRepository;
import br.com.libraryfree.library_free.repository.EmprestimoRepository;
import br.com.libraryfree.library_free.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Service
public class EmprestimoService {
    @Autowired
    EmprestimoRepository emprestimoRepository;
    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private LivroRepository livroRepository;

    public List<EmprestimoResponseDTO> findAll(){
        Iterable<Emprestimo> emprestimos = emprestimoRepository.findAll();
        List<EmprestimoResponseDTO> emprestimosDTO = new LinkedList<>();
        emprestimos.forEach(
                (emprestimo) -> emprestimosDTO.add(new EmprestimoResponseDTO(emprestimo))
        );
        return emprestimosDTO;
    }

    public List<EmprestimoResponseDTO> findLatestActiveLoans(){
        Iterable<Emprestimo> emprestimos = emprestimoRepository.findTop4ByAtivoIsTrueOrderByDataEmprestimoDesc();
        List<EmprestimoResponseDTO> emprestimosDTO = new LinkedList<>();
        emprestimos.forEach(
                (emprestimo) -> emprestimosDTO.add(new EmprestimoResponseDTO(emprestimo))
        );
        return emprestimosDTO;
    }

    public EmprestimoResponseDTO findById(Long id){
       Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado."));
       return new EmprestimoResponseDTO(emprestimo);
    }

    public EmprestimoResponseDTO createEmprestimo(EmprestimoRequestDTO emprestimo){
        Aluno existingAluno = alunoRepository.findById(emprestimo.alunoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aluno não encontrado."));
        Livro existingLivro = livroRepository.findById(emprestimo.livroId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Livro não encontrado."));
        if (!existingAluno.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aluno não está ativo.");
        }
        if (existingLivro.getEmprestado()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Livro já está emprestado.");
        }
        Emprestimo novoEmprestimo = new Emprestimo(
                null,
                existingAluno,
                existingLivro,
                LocalDate.now(),
                emprestimo.prazoDevolucao(),
                null,
                true
        );
        Emprestimo emprestimoSalvo = emprestimoRepository.save(novoEmprestimo);
        existingLivro.setEmprestado(true);
        livroRepository.save(existingLivro);
        return new EmprestimoResponseDTO(emprestimoSalvo);
    }

    public void editEmprestimo(Long livroID){
        Emprestimo emprestimo = emprestimoRepository.findByLivroIdAndAtivoTrue(livroID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado empréstimo ativo para este livro."));
        emprestimo.setAtivo(false);
        emprestimo.setDataDevolucao(LocalDate.now());
        Livro livro = emprestimo.getLivro();
        livro.setEmprestado(false);
        livroRepository.save(livro);
        emprestimoRepository.save(emprestimo);
    }
}
