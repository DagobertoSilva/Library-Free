package br.com.libraryfree.library_free.integration;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.libraryfree.library_free.DTO.BibliotecarioDTO;
import br.com.libraryfree.library_free.DTO.LoginDTO;
import br.com.libraryfree.library_free.domain.Aluno;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.util.List;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlunoIntegrationTests {
    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    public void shouldReturnForbiddenWhenGettingAlunosWithoutAuth() {
        ParameterizedTypeReference<Iterable<Aluno>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<Iterable<Aluno>> getResponse = testRestTemplate.exchange(
                "/libraryfree/alunos",
                HttpMethod.GET,
                null,
                responseType
        );
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void shouldReturnAllAlunosWhenAuthenticated() {
        ResponseEntity<BibliotecarioDTO> loginResponse = BibliotecarioLogin();
        HttpHeaders headers = createHeadersWithCookie(loginResponse);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ParameterizedTypeReference<Iterable<Aluno>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<Iterable<Aluno>> getResponse = testRestTemplate.exchange(
                "/libraryfree/alunos",
                HttpMethod.GET,
                request,
                responseType
        );
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
    }

    @Test
    public void shouldReturnAlunoWhenIdExists() {
        ResponseEntity<BibliotecarioDTO> loginResponse = BibliotecarioLogin();
        HttpHeaders headers = createHeadersWithCookie(loginResponse);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Aluno> response = testRestTemplate.exchange(
                "/libraryfree/alunos/1",
                HttpMethod.GET,
                request,
                Aluno.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        Aluno aluno = response.getBody();
        assertThat(aluno.getId()).isEqualTo(1);
        assertThat(aluno.getNome()).isEqualTo("João da Silva");
        assertThat(aluno.getMatricula()).isEqualTo("20230001");
        assertThat(aluno.getCpf()).isEqualTo("123.456.789-00");
        assertThat(aluno.getAtivo()).isEqualTo(true);
    }

    @Test
    public void shouldReturnNotFoundWhenIdDoesNotExist() {
        ResponseEntity<BibliotecarioDTO> loginResponse = BibliotecarioLogin();
        HttpHeaders headers = createHeadersWithCookie(loginResponse);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Aluno> response = testRestTemplate.exchange(
                "/libraryfree/alunos/1000000",
                HttpMethod.GET,
                request,
                Aluno.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    public void shouldCreateAlunoWhenMatriculaAndCpfDoesNotExist() {
        ResponseEntity<BibliotecarioDTO> loginResponse = BibliotecarioLogin();
        HttpHeaders headers = createHeadersWithCookie(loginResponse);
        Aluno aluno = new Aluno(
                null,
                "Fernanda Almeida",
                "20240005",
                "567.890.123-44",
                true
        );
        HttpEntity<Aluno> request = new HttpEntity<>(aluno, headers);
        ResponseEntity<Void> postResponse = testRestTemplate.postForEntity(
                "/libraryfree/alunos",
                request,
                Void.class
        );
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        request = new HttpEntity<>(headers);
        URI locationOfTheNewAluno = postResponse.getHeaders().getLocation();
        ResponseEntity<Aluno> getByIdResponse = testRestTemplate.exchange(
                locationOfTheNewAluno,
                HttpMethod.GET,
                request,
                Aluno.class);
        assertThat(getByIdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getByIdResponse.getBody()).isNotNull();
        assertThat(getByIdResponse.getBody().getMatricula()).isEqualTo("20240005");
        assertThat(getByIdResponse.getBody().getCpf()).isEqualTo("567.890.123-44");
    }

    @Test
    public void shouldReturnBadRequestWhenMatriculaAlreadyExists() {
        ResponseEntity<BibliotecarioDTO> loginResponse = BibliotecarioLogin();
        HttpHeaders headers = createHeadersWithCookie(loginResponse);
        Aluno aluno = new Aluno(
                null,
                "Novo aluno",
                "20230001",
                "888.999.000-11",
                true
        );
        HttpEntity<Aluno> request = new HttpEntity<>(aluno, headers);
        ResponseEntity<Void> postResponse = testRestTemplate.postForEntity(
                "/libraryfree/alunos",
                request,
                Void.class
        );
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldReturnBadRequestWhenCpfAlreadyExists() {
        ResponseEntity<BibliotecarioDTO> loginResponse = BibliotecarioLogin();
        HttpHeaders headers = createHeadersWithCookie(loginResponse);
        Aluno aluno = new Aluno(
                null,
                "Novo aluno",
                "20240006",
                "123.456.789-00",
                true
        );
        HttpEntity<Aluno> request = new HttpEntity<>(aluno, headers);
        ResponseEntity<Void> postResponse = testRestTemplate.postForEntity(
                "/libraryfree/alunos",
                request,
                Void.class
        );
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DirtiesContext
    public void shouldUpdateAlunoWhenIdExistsAndMatriculaAndCpfAreUnique() {
        ResponseEntity<BibliotecarioDTO> loginResponse = BibliotecarioLogin();
        HttpHeaders headers = createHeadersWithCookie(loginResponse);
        Aluno updatedAluno = new Aluno(
                null,
                "Marcos Vinicius",
                "20240005",
                "567.890.123-44",
                true
        );
        HttpEntity<Aluno> request = new HttpEntity<>(updatedAluno, headers);
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/libraryfree/alunos/1",
                HttpMethod.PUT,
                request,
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        request = new HttpEntity<>(headers);
        ResponseEntity<Aluno> getByIdResponse = testRestTemplate.exchange(
                "/libraryfree/alunos/1",
                HttpMethod.GET,
                request,
                Aluno.class
        );
        assertThat(getByIdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getByIdResponse.getBody()).isNotNull();
        assertThat(getByIdResponse.getBody().getMatricula()).isEqualTo(updatedAluno.getMatricula());
        assertThat(getByIdResponse.getBody().getCpf()).isEqualTo(updatedAluno.getCpf());
    }

    @Test
    public void shouldReturnNotFoundWhenUpdatingNonExistingAluno() {
        ResponseEntity<BibliotecarioDTO> loginResponse = BibliotecarioLogin();
        HttpHeaders headers = createHeadersWithCookie(loginResponse);
        Aluno updatedAluno = new Aluno(
                null,
                "Marcos Vinicius",
                "20240005",
                "567.890.123-44",
                true
        );
        HttpEntity<Aluno> request = new HttpEntity<>(updatedAluno, headers);
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/libraryfree/alunos/1000",
                HttpMethod.PUT,
                request,
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldReturnBadRequestWhenUpdatingAlunoWithDuplicateMatricula() {
        ResponseEntity<BibliotecarioDTO> loginResponse = BibliotecarioLogin();
        HttpHeaders headers = createHeadersWithCookie(loginResponse);
        Aluno updatedAluno = new Aluno(
                null,
                "Marcos Vinicius",
                "20230001",
                "567.890.123-44",
                true
        );
        HttpEntity<Aluno> request = new HttpEntity<>(updatedAluno, headers);
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/libraryfree/alunos/2",
                HttpMethod.PUT,
                request,
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldReturnBadRequestWhenUpdatingAlunoWithDuplicateCpf() {
        ResponseEntity<BibliotecarioDTO> loginResponse = BibliotecarioLogin();
        HttpHeaders headers = createHeadersWithCookie(loginResponse);
        Aluno updatedAluno = new Aluno(
                null,
                "Marcos Vinicius",
                "20240005",
                "123.456.789-00",
                true
        );
        HttpEntity<Aluno> request = new HttpEntity<>(updatedAluno, headers);
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/libraryfree/alunos/2",
                HttpMethod.PUT,
                request,
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DirtiesContext
    public void shouldDeleteAlunoWhenIdExists() {
        ResponseEntity<BibliotecarioDTO> loginResponse = BibliotecarioLogin();
        HttpHeaders headers = createHeadersWithCookie(loginResponse);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/libraryfree/alunos/1",
                HttpMethod.DELETE,
                request,
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        ResponseEntity<Aluno> getByIdResponse = testRestTemplate.exchange(
                "/libraryfree/alunos/1",
                HttpMethod.GET,
                request,
                Aluno.class
        );
        assertThat(getByIdResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldReturnNotFoundWhenDeletingNonExistingAluno() {
        ResponseEntity<BibliotecarioDTO> loginResponse = BibliotecarioLogin();
        HttpHeaders headers = createHeadersWithCookie(loginResponse);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/libraryfree/alunos/1000",
                HttpMethod.DELETE,
                request,
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<BibliotecarioDTO> BibliotecarioLogin() {
        LoginDTO loginDTO = new LoginDTO("admin@libraryfree.com", "senha123");
        ResponseEntity<BibliotecarioDTO> loginResponse = testRestTemplate.postForEntity(
                "/libraryfree/auth/login", loginDTO, BibliotecarioDTO.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        return loginResponse;
    }

    private HttpHeaders createHeadersWithCookie(ResponseEntity<?> response) {
        List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        assertThat(cookies).isNotNull();
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}