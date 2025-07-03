package br.com.libraryfree.library_free.integration;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.libraryfree.library_free.DTO.BibliotecarioDTO;
import br.com.libraryfree.library_free.DTO.LoginDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BibliotecarioIntegrationTests {
    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    public void shouldReturnOkAndBibliotecarioDataWhenLoginWithValidCredentials(){
        LoginDTO loginDTO = new LoginDTO("admin@libraryfree.com", "senha123");
        ResponseEntity<BibliotecarioDTO> loginResponse = testRestTemplate.postForEntity(
                "/libraryfree/auth/login", loginDTO, BibliotecarioDTO.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        BibliotecarioDTO bibliotecario = loginResponse.getBody();
        assertThat(bibliotecario).isNotNull();
        assertThat(bibliotecario.id()).isEqualTo(1);
        assertThat(bibliotecario.nome()).isEqualTo("Felipe Ferreira");
        assertThat(bibliotecario.email()).isEqualTo("admin@libraryfree.com");
    }

    @Test
    public void shouldReturnUnauthorizedWhenLoginWithInvalidPassword() {
        LoginDTO loginDTO = new LoginDTO("admin@libraryfree.com", "senhainvalida");
        ResponseEntity<BibliotecarioDTO> loginResponse = testRestTemplate.postForEntity(
                "/libraryfree/auth/login", loginDTO, BibliotecarioDTO.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldReturnUnauthorizedWhenLoginWithNonExistentUser() {
        LoginDTO loginDTO = new LoginDTO("usuarionaoexiste@email.com", "senha123");
        ResponseEntity<BibliotecarioDTO> loginResponse = testRestTemplate.postForEntity(
                "/libraryfree/auth/login", loginDTO, BibliotecarioDTO.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
