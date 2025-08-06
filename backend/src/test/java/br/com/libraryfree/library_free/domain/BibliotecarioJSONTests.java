package br.com.libraryfree.library_free.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;

@JsonTest
public class BibliotecarioJSONTests {
    @Autowired
    private JacksonTester<Bibliotecario> jacksonTester;

    private final Bibliotecario bibliotecario = new Bibliotecario(
            1L,
            "Ciclano",
            "emailgenerico@hotmail.com",
            "123456789"
    );

    @Test
    public void BibliotecarioSerializationTest() throws IOException {
        JsonContent<Bibliotecario> serializedBibliotecario = jacksonTester.write(bibliotecario);
        assertThat(serializedBibliotecario).isNotNull();
        assertThat(serializedBibliotecario).isEqualTo("Bibliotecario.json");
        assertThat(serializedBibliotecario).hasJsonPathNumberValue("@.id");
        assertThat(serializedBibliotecario).extractingJsonPathNumberValue("@.id").isEqualTo(bibliotecario.getId().intValue());
        assertThat(serializedBibliotecario).hasJsonPathStringValue("@.nome");
        assertThat(serializedBibliotecario).extractingJsonPathStringValue("@.nome").isEqualTo(bibliotecario.getNome());
        assertThat(serializedBibliotecario).hasJsonPathStringValue("@.email");
        assertThat(serializedBibliotecario).extractingJsonPathStringValue("@.email").isEqualTo(bibliotecario.getEmail());
        assertThat(serializedBibliotecario).hasJsonPathStringValue("@.senha");
        assertThat(serializedBibliotecario).extractingJsonPathStringValue("@.senha").isEqualTo(bibliotecario.getSenha());
    }

    @Test
    public void BibliotecarioDeserializationTest() throws IOException {
        String expected = """
        {
            "id": 1,
            "nome": "Ciclano",
            "email": "emailgenerico@hotmail.com",
            "senha": "123456789"
        }
        """;
        ObjectContent<Bibliotecario> deserializedBibliotecario = jacksonTester.parse(expected);
        assertThat(deserializedBibliotecario).isEqualTo(bibliotecario);
        assertThat(deserializedBibliotecario.getObject().getId()).isEqualTo(bibliotecario.getId());
        assertThat(deserializedBibliotecario.getObject().getNome()).isEqualTo(bibliotecario.getNome());
        assertThat(deserializedBibliotecario.getObject().getEmail()).isEqualTo(bibliotecario.getEmail());
        assertThat(deserializedBibliotecario.getObject().getSenha()).isEqualTo(bibliotecario.getSenha());
    }
}
