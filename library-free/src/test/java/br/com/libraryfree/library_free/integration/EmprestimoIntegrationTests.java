package br.com.libraryfree.library_free.integration;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.libraryfree.library_free.DTO.BibliotecarioDTO;
import br.com.libraryfree.library_free.DTO.EmprestimoRequestDTO;
import br.com.libraryfree.library_free.DTO.EmprestimoResponseDTO;
import br.com.libraryfree.library_free.DTO.LoginDTO;
import br.com.libraryfree.library_free.domain.Emprestimo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmprestimoIntegrationTests {
    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    public void shouldReturnUnauthorizedWhenGettingEmprestimosWithoutAuth() {
        ParameterizedTypeReference<List<Emprestimo>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<Emprestimo>> getResponse = testRestTemplate.exchange(
                "/libraryfree/emprestimos",
                HttpMethod.GET,
                null,
                responseType
        );
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void shouldReturnAllEmprestimosWhenAuthenticated() {
        ResponseEntity<BibliotecarioDTO> loginResponse = BibliotecarioLogin();
        HttpHeaders headers = createHeadersWithCookie(loginResponse);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ParameterizedTypeReference<List<EmprestimoResponseDTO>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<EmprestimoResponseDTO>> getResponse = testRestTemplate.exchange(
                "/libraryfree/emprestimos",
                HttpMethod.GET,
                request,
                responseType
        );
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
    }

    @Test
    public void shouldReturnEmprestimoWhenIdExists() {
        ResponseEntity<BibliotecarioDTO> loginResponse = BibliotecarioLogin();
        HttpHeaders headers = createHeadersWithCookie(loginResponse);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<EmprestimoResponseDTO> response = testRestTemplate.exchange(
                "/libraryfree/emprestimos/1",
                HttpMethod.GET,
                request,
                EmprestimoResponseDTO.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        EmprestimoResponseDTO emprestimo = response.getBody();
        assertThat(emprestimo.getId()).isEqualTo(1);
        assertThat(emprestimo.getAlunoNome()).isEqualTo("João da Silva");
        assertThat(emprestimo.getLivroTitulo()).isEqualTo("O Senhor dos Anéis");
    }

    @Test
    public void shouldReturnNotFoundWhenIdDoesNotExist() {
        ResponseEntity<BibliotecarioDTO> loginResponse = BibliotecarioLogin();
        HttpHeaders headers = createHeadersWithCookie(loginResponse);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<EmprestimoResponseDTO> response = testRestTemplate.exchange(
                "/libraryfree/emprestimos/1000000",
                HttpMethod.GET,
                request,
                EmprestimoResponseDTO.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    public void shouldCreateEmprestimoSuccessfullyWhenDataIsValid() {
        ResponseEntity<BibliotecarioDTO> loginResponse = BibliotecarioLogin();
        HttpHeaders headers = createHeadersWithCookie(loginResponse);
        EmprestimoRequestDTO emprestimo = new EmprestimoRequestDTO(1L,2L, LocalDate.of(2030,10,10));
        HttpEntity<EmprestimoRequestDTO> request = new HttpEntity<>(emprestimo, headers);
        ResponseEntity<EmprestimoResponseDTO> postResponse = testRestTemplate.postForEntity(
                "/libraryfree/emprestimos",
                request,
                EmprestimoResponseDTO.class
        );
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(postResponse.getHeaders().getLocation()).isNotNull();
        URI locationOfTheNewEmprestimo = postResponse.getHeaders().getLocation();
        request = new HttpEntity<>(headers);
        ResponseEntity<EmprestimoResponseDTO> getResponse = testRestTemplate.exchange(
                locationOfTheNewEmprestimo,
                HttpMethod.GET,
                request,
                EmprestimoResponseDTO.class
        );
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        EmprestimoResponseDTO emprestimoResponseDTO = getResponse.getBody();
        assertThat(emprestimoResponseDTO.getId()).isEqualTo(3);
        assertThat(emprestimoResponseDTO.getAlunoId()).isEqualTo(1);
        assertThat(emprestimoResponseDTO.getAlunoNome()).isEqualTo("João da Silva");
        assertThat(emprestimoResponseDTO.getLivroId()).isEqualTo(2);
        assertThat(emprestimoResponseDTO.getLivroTitulo()).isEqualTo("Dom Casmurro");
        assertThat(emprestimoResponseDTO.getDataEmprestimo()).isEqualTo(LocalDate.parse("2025-07-05"));
        assertThat(emprestimoResponseDTO.getPrazoDevolucao()).isEqualTo(LocalDate.parse("2030-10-10"));
        assertThat(emprestimoResponseDTO.getDataDevolucao()).isNull();
        assertThat(emprestimoResponseDTO.getAtivo()).isTrue();
    }

    @Test
    public void shouldReturnBadRequestWhenAlunoDoesNotExist() {
        ResponseEntity<BibliotecarioDTO> loginResponse = BibliotecarioLogin();
        HttpHeaders headers = createHeadersWithCookie(loginResponse);
        EmprestimoRequestDTO requestDTO = new EmprestimoRequestDTO(1000L,1L, LocalDate.of(2030,10,10));
        HttpEntity<EmprestimoRequestDTO> request = new HttpEntity<>(requestDTO, headers);
        ResponseEntity<EmprestimoResponseDTO> postResponse = testRestTemplate.postForEntity(
                "/libraryfree/emprestimos",
                request,
                EmprestimoResponseDTO.class
        );
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldReturnBadRequestWhenLivroDoesNotExist() {
        ResponseEntity<BibliotecarioDTO> loginResponse = BibliotecarioLogin();
        HttpHeaders headers = createHeadersWithCookie(loginResponse);
        EmprestimoRequestDTO requestDTO = new EmprestimoRequestDTO(1L,1000L, LocalDate.of(2030,10,10));
        HttpEntity<EmprestimoRequestDTO> request = new HttpEntity<>(requestDTO, headers);
        ResponseEntity<EmprestimoResponseDTO> postResponse = testRestTemplate.postForEntity(
                "/libraryfree/emprestimos",
                request,
                EmprestimoResponseDTO.class
        );
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldReturnBadRequestWhenAlunoIsNotActive() {
        ResponseEntity<BibliotecarioDTO> loginResponse = BibliotecarioLogin();
        HttpHeaders headers = createHeadersWithCookie(loginResponse);
        EmprestimoRequestDTO requestDTO = new EmprestimoRequestDTO(4L,1L, LocalDate.of(2030,10,10));
        HttpEntity<EmprestimoRequestDTO> request = new HttpEntity<>(requestDTO, headers);
        ResponseEntity<EmprestimoResponseDTO> postResponse = testRestTemplate.postForEntity(
                "/libraryfree/emprestimos",
                request,
                EmprestimoResponseDTO.class
        );
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldReturnBadRequestWhenLivroIsAlreadyEmprestado() {
        ResponseEntity<BibliotecarioDTO> loginResponse = BibliotecarioLogin();
        HttpHeaders headers = createHeadersWithCookie(loginResponse);
        EmprestimoRequestDTO requestDTO = new EmprestimoRequestDTO(1L,4L, LocalDate.of(2030,10,10));
        HttpEntity<EmprestimoRequestDTO> request = new HttpEntity<>(requestDTO, headers);
        ResponseEntity<EmprestimoResponseDTO> postResponse = testRestTemplate.postForEntity(
                "/libraryfree/emprestimos",
                request,
                EmprestimoResponseDTO.class
        );
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DirtiesContext
    public void shouldReturnEmprestimoSuccessfully(){
        ResponseEntity<BibliotecarioDTO> loginResponse = BibliotecarioLogin();
        HttpHeaders headers = createHeadersWithCookie(loginResponse);
        EmprestimoRequestDTO emprestimo = new EmprestimoRequestDTO(1L,3L, LocalDate.of(2030,10,10));
        HttpEntity<EmprestimoRequestDTO> request = new HttpEntity<>(emprestimo, headers);
        ResponseEntity<EmprestimoResponseDTO> postResponse = testRestTemplate.postForEntity(
                "/libraryfree/emprestimos",
                request,
                EmprestimoResponseDTO.class
        );
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(postResponse.getHeaders().getLocation()).isNotNull();
        URI locationOfTheNewEmprestimo = postResponse.getHeaders().getLocation();
        request = new HttpEntity<>(headers);
        ResponseEntity<EmprestimoResponseDTO> getResponse = testRestTemplate.exchange(
                locationOfTheNewEmprestimo,
                HttpMethod.GET,
                request,
                EmprestimoResponseDTO.class
        );
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getAtivo()).isEqualTo(true);
        ResponseEntity<EmprestimoResponseDTO> putResponse = testRestTemplate.exchange(
                "/libraryfree/emprestimos/" + getResponse.getBody().getLivroId(),
                HttpMethod.PUT,
                request,
                EmprestimoResponseDTO.class
        );
        assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        getResponse = testRestTemplate.exchange(
                locationOfTheNewEmprestimo,
                HttpMethod.GET,
                request,
                EmprestimoResponseDTO.class
        );
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getAtivo()).isEqualTo(false);
    }

    @Test
    public void shouldReturnNotFoundWhenDevolvingLivroWithNoActiveEmprestimo(){
        ResponseEntity<BibliotecarioDTO> loginResponse = BibliotecarioLogin();
        HttpHeaders headers = createHeadersWithCookie(loginResponse);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Void> putResponse = testRestTemplate.exchange(
                "/libraryfree/emprestimos/3",
                HttpMethod.PUT,
                request,
                Void.class
        );
        assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldReturnNotFoundWhenDevolvingLivroWithNonExistentId(){
        ResponseEntity<BibliotecarioDTO> loginResponse = BibliotecarioLogin();
        HttpHeaders headers = createHeadersWithCookie(loginResponse);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Void> putResponse = testRestTemplate.exchange(
                "/libraryfree/emprestimos/1000",
                HttpMethod.PUT,
                request,
                Void.class
        );
        assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }



    private ResponseEntity<BibliotecarioDTO> BibliotecarioLogin() {
        LoginDTO loginDTO = new LoginDTO("admin@libraryfree.com", "senha123");
        ResponseEntity<BibliotecarioDTO> loginResponse = testRestTemplate.postForEntity(
                "/libraryfree/auth/login", loginDTO, BibliotecarioDTO.class);
        Assertions.assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        return loginResponse;
    }

    private HttpHeaders createHeadersWithCookie(ResponseEntity<?> response) {
        List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        Assertions.assertThat(cookies).isNotNull();
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
