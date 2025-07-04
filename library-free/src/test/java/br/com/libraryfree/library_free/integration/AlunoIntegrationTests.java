package br.com.libraryfree.library_free.integration;

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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
        LoginDTO loginDTO = new LoginDTO("admin@libraryfree.com", "senha123");
        ResponseEntity<BibliotecarioDTO> loginResponse = testRestTemplate.postForEntity(
                "/libraryfree/auth/login", loginDTO, BibliotecarioDTO.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<String> cookies = loginResponse.getHeaders().get("Set-Cookie");
        assertThat(cookies).isNotNull();
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
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
        LoginDTO loginDTO = new LoginDTO("admin@libraryfree.com", "senha123");
        ResponseEntity<BibliotecarioDTO> loginResponse = testRestTemplate.postForEntity(
                "/libraryfree/auth/login",
                loginDTO,
                BibliotecarioDTO.class
        );
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<String> cookies = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
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
        assertThat(aluno).isNotNull();
        assertThat(aluno.getId()).isEqualTo(1);
        assertThat(aluno.getNome()).isEqualTo("João da Silva");
        assertThat(aluno.getMatricula()).isEqualTo("20230001");
        assertThat(aluno.getCpf()).isEqualTo("123.456.789-00");
        assertThat(aluno.getAtivo()).isEqualTo(true);
    }

    @Test
    public void shouldReturnNotFoundWhenIdDoesNotExist() {
        LoginDTO loginDTO = new LoginDTO("admin@libraryfree.com", "senha123");
        ResponseEntity<BibliotecarioDTO> loginResponse = testRestTemplate.postForEntity(
                "/libraryfree/auth/login",
                loginDTO,
                BibliotecarioDTO.class
        );
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<String> cookies = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
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
        LoginDTO loginDTO = new LoginDTO("admin@libraryfree.com", "senha123");
        ResponseEntity<BibliotecarioDTO> loginResponse = testRestTemplate.postForEntity(
                "/libraryfree/auth/login",
                loginDTO,
                BibliotecarioDTO.class
        );
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Aluno aluno = new Aluno(
                null,
                "Fernanda Almeida",
                "20240005",
                "567.890.123-44",
                true
        );
        List<String> cookies = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Aluno> request = new HttpEntity<>(aluno, headers);
        ResponseEntity<Void> postResponse = testRestTemplate.postForEntity(
                "/libraryfree/alunos",
                request,
                Void.class
        );
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
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
        LoginDTO loginDTO = new LoginDTO("admin@libraryfree.com", "senha123");
        ResponseEntity<BibliotecarioDTO> loginResponse = testRestTemplate.postForEntity(
                "/libraryfree/auth/login",
                loginDTO,
                BibliotecarioDTO.class
        );
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Aluno aluno = new Aluno(
                null,
                "Novo aluno",
                "20230001",
                "888.999.000-11",
                true
        );
        List<String> cookies = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.setContentType(MediaType.APPLICATION_JSON);
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
        LoginDTO loginDTO = new LoginDTO("admin@libraryfree.com", "senha123");
        ResponseEntity<BibliotecarioDTO> loginResponse = testRestTemplate.postForEntity(
                "/libraryfree/auth/login",
                loginDTO,
                BibliotecarioDTO.class
        );
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Aluno aluno = new Aluno(
                null,
                "Novo aluno",
                "20240006",
                "123.456.789-00",
                true
        );
        List<String> cookies = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.setContentType(MediaType.APPLICATION_JSON);
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
        LoginDTO loginDTO = new LoginDTO("admin@libraryfree.com", "senha123");
        ResponseEntity<BibliotecarioDTO> loginResponse = testRestTemplate.postForEntity(
                "/libraryfree/auth/login", loginDTO, BibliotecarioDTO.class);
        List<String> cookies = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.setContentType(MediaType.APPLICATION_JSON);
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
        HttpEntity<Void> getRequest = new HttpEntity<>(headers);
        ResponseEntity<Aluno> getByIdResponse = testRestTemplate.exchange(
                "/libraryfree/alunos/1",
                HttpMethod.GET,
                getRequest,
                Aluno.class
        );
        assertThat(getByIdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getByIdResponse.getBody()).isNotNull();
        assertThat(getByIdResponse.getBody().getMatricula()).isEqualTo(updatedAluno.getMatricula());
        assertThat(getByIdResponse.getBody().getCpf()).isEqualTo(updatedAluno.getCpf());
    }

    @Test
    public void shouldReturnNotFoundWhenUpdatingNonExistingAluno() {
        LoginDTO loginDTO = new LoginDTO("admin@libraryfree.com", "senha123");
        ResponseEntity<BibliotecarioDTO> loginResponse = testRestTemplate.postForEntity(
                "/libraryfree/auth/login", loginDTO, BibliotecarioDTO.class);
        List<String> cookies = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.setContentType(MediaType.APPLICATION_JSON);
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
        LoginDTO loginDTO = new LoginDTO("admin@libraryfree.com", "senha123");
        ResponseEntity<BibliotecarioDTO> loginResponse = testRestTemplate.postForEntity(
                "/libraryfree/auth/login", loginDTO, BibliotecarioDTO.class);
        List<String> cookies = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.setContentType(MediaType.APPLICATION_JSON);
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
        LoginDTO loginDTO = new LoginDTO("admin@libraryfree.com", "senha123");
        ResponseEntity<BibliotecarioDTO> loginResponse = testRestTemplate.postForEntity(
                "/libraryfree/auth/login", loginDTO, BibliotecarioDTO.class);
        List<String> cookies = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.setContentType(MediaType.APPLICATION_JSON);
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
        LoginDTO loginDTO = new LoginDTO("admin@libraryfree.com", "senha123");
        ResponseEntity<BibliotecarioDTO> loginResponse = testRestTemplate.postForEntity(
                "/libraryfree/auth/login",
                loginDTO,
                BibliotecarioDTO.class
        );
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<String> cookies = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
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
        LoginDTO loginDTO = new LoginDTO("admin@libraryfree.com", "senha123");
        ResponseEntity<BibliotecarioDTO> loginResponse = testRestTemplate.postForEntity(
                "/libraryfree/auth/login",
                loginDTO,
                BibliotecarioDTO.class
        );
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<String> cookies = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/libraryfree/alunos/1000",
                HttpMethod.DELETE,
                request,
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
