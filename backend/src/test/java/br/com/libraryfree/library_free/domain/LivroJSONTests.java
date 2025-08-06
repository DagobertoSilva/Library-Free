package br.com.libraryfree.library_free.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;
import java.time.LocalDate;

@JsonTest
public class LivroJSONTests {
    @Autowired
    JacksonTester<Livro> jacksonTester;

    private final Livro livro = new Livro(
            1L,
            "O Senhor dos Anéis",
            "9788595084759",
            "J.R.R. Tolkien",
            "Fantasia",
            "Volume Único",
            LocalDate.of(2019, 11, 1),
            "HarperCollins Brasil",
            false
    );

    @Test
    public void LivroSerializationTest() throws IOException {
        JsonContent<Livro> serializedLivro = jacksonTester.write(livro);
        assertThat(serializedLivro).isNotNull();
        assertThat(serializedLivro).isStrictlyEqualToJson("Livro.json");
        assertThat(serializedLivro).hasJsonPathNumberValue("@.id");
        assertThat(serializedLivro).extractingJsonPathNumberValue("@.id").isEqualTo(livro.getId().intValue());
        assertThat(serializedLivro).hasJsonPathStringValue("@.titulo");
        assertThat(serializedLivro).extractingJsonPathStringValue("@.titulo").isEqualTo(livro.getTitulo());
        assertThat(serializedLivro).hasJsonPathStringValue("@.isbn");
        assertThat(serializedLivro).extractingJsonPathStringValue("@.isbn").isEqualTo(livro.getIsbn());
        assertThat(serializedLivro).hasJsonPathStringValue("@.autor");
        assertThat(serializedLivro).extractingJsonPathStringValue("@.autor").isEqualTo(livro.getAutor());
        assertThat(serializedLivro).hasJsonPathStringValue("@.genero");
        assertThat(serializedLivro).extractingJsonPathStringValue("@.genero").isEqualTo(livro.getGenero());
        assertThat(serializedLivro).hasJsonPathStringValue("@.edicao");
        assertThat(serializedLivro).extractingJsonPathStringValue("@.edicao").isEqualTo(livro.getEdicao());
        assertThat(serializedLivro).hasJsonPathStringValue("@.publicacao");
        assertThat(serializedLivro).extractingJsonPathStringValue("@.publicacao").isEqualTo("2019-11-01");
        assertThat(serializedLivro).hasJsonPathStringValue("@.editora");
        assertThat(serializedLivro).extractingJsonPathStringValue("@.editora").isEqualTo(livro.getEditora());
        assertThat(serializedLivro).hasJsonPathBooleanValue("@.emprestado");
        assertThat(serializedLivro).extractingJsonPathBooleanValue("@.emprestado").isEqualTo(livro.getEmprestado());
        assertThat(serializedLivro).hasJsonPathArrayValue("@.emprestimos");
        assertThat(serializedLivro).extractingJsonPathArrayValue("@.emprestimos").isEqualTo(livro.getEmprestimos());
    }

    @Test
    public void LivroDeserializationTest() throws IOException {
        String expected = """
        {
            "id": 1,
            "titulo": "O Senhor dos Anéis",
            "isbn": "9788595084759",
            "autor": "J.R.R. Tolkien",
            "genero": "Fantasia",
            "edicao": "Volume Único",
            "publicacao": "2019-11-01",
            "editora": "HarperCollins Brasil",
            "emprestado": false,
            "emprestimos": []
        }
        """;

        ObjectContent<Livro> deserializedLivro = jacksonTester.parse(expected);
        assertThat(deserializedLivro).isEqualTo(livro);
        assertThat(deserializedLivro.getObject().getId()).isEqualTo(livro.getId());
        assertThat(deserializedLivro.getObject().getTitulo()).isEqualTo(livro.getTitulo());
        assertThat(deserializedLivro.getObject().getIsbn()).isEqualTo(livro.getIsbn());
        assertThat(deserializedLivro.getObject().getAutor()).isEqualTo(livro.getAutor());
        assertThat(deserializedLivro.getObject().getGenero()).isEqualTo(livro.getGenero());
        assertThat(deserializedLivro.getObject().getEdicao()).isEqualTo(livro.getEdicao());
        assertThat(deserializedLivro.getObject().getPublicacao()).isEqualTo(livro.getPublicacao());
        assertThat(deserializedLivro.getObject().getEditora()).isEqualTo(livro.getEditora());
        assertThat(deserializedLivro.getObject().getEmprestado()).isEqualTo(livro.getEmprestado());
        assertThat(deserializedLivro.getObject().getEmprestimos()).isEqualTo(livro.getEmprestimos());
    }


}
