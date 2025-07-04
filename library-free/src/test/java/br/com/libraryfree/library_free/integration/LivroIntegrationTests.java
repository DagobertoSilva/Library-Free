package br.com.libraryfree.library_free.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import br.com.libraryfree.library_free.DTO.BibliotecarioDTO;
import br.com.libraryfree.library_free.DTO.LoginDTO;
import br.com.libraryfree.library_free.domain.Livro;
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
public class LivroIntegrationTests {
    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    public void shouldReturnForbiddenWhenGettingLivrosWithoutAuth() {
        ParameterizedTypeReference<Iterable<Livro>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<Iterable<Livro>> getResponse = testRestTemplate.exchange(
                "/libraryfree/livros",
                HttpMethod.GET,
                null,
                responseType
        );
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void shouldReturnAllLivrosWhenAuthenticated() {
        LoginDTO loginDTO = new LoginDTO("admin@libraryfree.com", "senha123");
        ResponseEntity<BibliotecarioDTO> loginResponse = testRestTemplate.postForEntity(
                "/libraryfree/auth/login", loginDTO, BibliotecarioDTO.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<String> cookies = loginResponse.getHeaders().get("Set-Cookie");
        assertThat(cookies).isNotNull();
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ParameterizedTypeReference<Iterable<Livro>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<Iterable<Livro>> getResponse = testRestTemplate.exchange(
                "/libraryfree/livros",
                HttpMethod.GET,
                request,
                responseType
        );
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
    }

    @Test
    public void shouldReturnLivroWhenIdExists() {
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
        ResponseEntity<Livro> response = testRestTemplate.exchange(
                "/libraryfree/livros/1",
                HttpMethod.GET,
                request,
                Livro.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Livro livro = response.getBody();
        assertThat(livro).isNotNull();
        assertThat(livro.getId()).isEqualTo(1);
        assertThat(livro.getTitulo()).isEqualTo("O Senhor dos Anéis");
        assertThat(livro.getIsbn()).isEqualTo("9788595084759");
        assertThat(livro.getAutor()).isEqualTo("J.R.R. Tolkien");
        assertThat(livro.getGenero()).isEqualTo("Alta Fantasia");
        assertThat(livro.getEdicao()).isEqualTo("Volume Único");
        assertThat(livro.getPublicacao()).isEqualTo("2019-11-01");
        assertThat(livro.getEditora()).isEqualTo("HarperCollins Brasil");
        assertThat(livro.getEmprestado()).isEqualTo(false);
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
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/libraryfree/livros/1000000",
                HttpMethod.GET,
                request,
                String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    public void shouldCreateLivroWhenIsbnDoesNotExist() {
        LoginDTO loginDTO = new LoginDTO("admin@libraryfree.com", "senha123");
        ResponseEntity<BibliotecarioDTO> loginResponse = testRestTemplate.postForEntity(
                "/libraryfree/auth/login",
                loginDTO,
                BibliotecarioDTO.class
        );
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Livro livro = new Livro(null,
                "Rápido e Devagar",
                "9788539004118",
                "Daniel Kahneman",
                "Psicologia",
                "1°",
                LocalDate.parse("2012-03-01"),
                "Objetiva",
                false
                );
        List<String> cookies = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Livro> request = new HttpEntity<>(livro, headers);

        ResponseEntity<Livro> postResponse = testRestTemplate.postForEntity(
                "/libraryfree/livros",
                request,
                Livro.class
        );
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        request = new HttpEntity<>(headers);
        URI locationOfTheNewLivro = postResponse.getHeaders().getLocation();
        ResponseEntity<Livro> getByIdResponse = testRestTemplate.exchange(
                locationOfTheNewLivro,
                HttpMethod.GET,
                request,
                Livro.class);
        assertThat(getByIdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getByIdResponse.getBody().getIsbn()).isEqualTo("9788539004118");
    }

    @Test
    public void shouldReturnBadRequestWhenIsbnAlreadyExists() {
        LoginDTO loginDTO = new LoginDTO("admin@libraryfree.com", "senha123");
        ResponseEntity<BibliotecarioDTO> loginResponse = testRestTemplate.postForEntity(
                "/libraryfree/auth/login",
                loginDTO,
                BibliotecarioDTO.class
        );
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Livro livro = new Livro(null,
                "O Senhor dos Anéis",
                "9788595084759",
                "J.R.R. Tolkien",
                "Alta Fantasia",
                "Volume Único",
                LocalDate.parse("2019-11-01"),
                "HarperCollins Brasil",
                false
        );
        List<String> cookies = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Livro> request = new HttpEntity<>(livro, headers);
        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/libraryfree/livros",
                request,
                String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DirtiesContext
    public void shouldDeleteLivroWhenIdExists() {
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
                "/libraryfree/livros/1",
                HttpMethod.DELETE,
                request,
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        ResponseEntity<Livro> getByIdResponse = testRestTemplate.exchange(
                "/libraryfree/livros/1",
                HttpMethod.GET,
                request,
                Livro.class
        );
        assertThat(getByIdResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldReturnNotFoundWhenDeletingNonExistingId() {
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
                "/libraryfree/livros/1000",
                HttpMethod.DELETE,
                request,
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldUpdateLivroWhenIdExistsAndIsbnIsUnique() {
        LoginDTO loginDTO = new LoginDTO("admin@libraryfree.com", "senha123");
        ResponseEntity<BibliotecarioDTO> loginResponse = testRestTemplate.postForEntity(
                "/libraryfree/auth/login", loginDTO, BibliotecarioDTO.class);
        List<String> cookies = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Livro updatedLivro = new Livro(null,
                "Rápido e Devagar - Editado",
                "9788539004118",
                "Daniel Kahneman",
                "Psicologia",
                "2ª",
                LocalDate.parse("2012-03-01"),
                "Objetiva",
                false
        );
        HttpEntity<Livro> request = new HttpEntity<>(updatedLivro, headers);
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/libraryfree/livros/1",
                HttpMethod.PUT,
                request,
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        ResponseEntity<Livro> getByIdResponse = testRestTemplate.exchange(
                "/libraryfree/livros/1",
                HttpMethod.GET,
                request,
                Livro.class
        );
        assertThat(getByIdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getByIdResponse.getBody().getIsbn()).isEqualTo(updatedLivro.getIsbn());
    }

    @Test
    public void shouldReturnNotFoundWhenUpdatingNonExistingLivro() {
        LoginDTO loginDTO = new LoginDTO("admin@libraryfree.com", "senha123");
        ResponseEntity<BibliotecarioDTO> loginResponse = testRestTemplate.postForEntity(
                "/libraryfree/auth/login", loginDTO, BibliotecarioDTO.class);
        List<String> cookies = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Livro updatedLivro = new Livro(null,
                "Rápido e Devagar - Editado",
                "9788539004118",
                "Daniel Kahneman",
                "Psicologia",
                "2ª",
                LocalDate.parse("2012-03-01"),
                "Objetiva",
                false
        );
        HttpEntity<Livro> request = new HttpEntity<>(updatedLivro, headers);
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/libraryfree/livros/1000",
                HttpMethod.PUT,
                request,
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldReturnBadRequestWhenUpdatingLivroWithDuplicateIsbn() {
        LoginDTO loginDTO = new LoginDTO("admin@libraryfree.com", "senha123");
        ResponseEntity<BibliotecarioDTO> loginResponse = testRestTemplate.postForEntity(
                "/libraryfree/auth/login", loginDTO, BibliotecarioDTO.class);
        List<String> cookies = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Livro updatedLivro = new Livro(null,
                "O Senhor dos Anéis",
                "9788595084759",
                "J.R.R. Tolkien",
                "Alta Fantasia",
                "Volume Único",
                LocalDate.parse("2019-11-01"),
                "HarperCollins Brasil",
                false
        );
        HttpEntity<Livro> request = new HttpEntity<>(updatedLivro, headers);
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/libraryfree/livros/2",
                HttpMethod.PUT,
                request,
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}