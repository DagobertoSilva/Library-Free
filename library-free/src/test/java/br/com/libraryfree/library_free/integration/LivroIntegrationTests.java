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
import org.springframework.test.context.ActiveProfiles;

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
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ParameterizedTypeReference<Iterable<Livro>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<Iterable<Livro>> getResponse = testRestTemplate.exchange(
                "/libraryfree/livros",
                HttpMethod.GET,
                entity,
                responseType
        );
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
    }
}