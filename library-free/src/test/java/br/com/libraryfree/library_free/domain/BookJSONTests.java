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
public class BookJSONTests {
    @Autowired
    JacksonTester<Book> jacksonTester;

    @Test
    public void BookSerializationTest() throws IOException {
        Book book = new Book(1L,"5146846548465", "O Senhor dos anéis", "Aventura", "Livre");
        JsonContent<Book> serializedBook = jacksonTester.write(book);
        assertThat(serializedBook).isStrictlyEqualToJson("Book.json");
        assertThat(serializedBook).hasJsonPathNumberValue("@.id");
        assertThat(serializedBook).extractingJsonPathNumberValue("@.id").isEqualTo(1);
        assertThat(serializedBook).hasJsonPathStringValue("@.isbn");
        assertThat(serializedBook).extractingJsonPathStringValue("@.isbn").isEqualTo("5146846548465");
        assertThat(serializedBook).hasJsonPathStringValue("@.title");
        assertThat(serializedBook).extractingJsonPathStringValue("@.title").isEqualTo("O Senhor dos anéis");
        assertThat(serializedBook).hasJsonPathStringValue("@.genre");
        assertThat(serializedBook).extractingJsonPathStringValue("@.genre").isEqualTo("Aventura");
        assertThat(serializedBook).hasJsonPathStringValue("@.status");
        assertThat(serializedBook).extractingJsonPathStringValue("@.status").isEqualTo("Livre");
    }

    @Test
    public void BookDeserializationTest() throws IOException {
        Book book = new Book(1L,"5146846548465", "O Senhor dos anéis", "Aventura", "Livre");
        String expected = """
                {
                    "id": 1,
                     "isbn": "5146846548465",
                     "title": "O Senhor dos anéis",
                     "genre": "Aventura",
                     "status": "Livre"
                }
        """;
        ObjectContent<Book> deserializedBook = jacksonTester.parse(expected);
        assertThat(deserializedBook).isEqualTo(book);
        assertThat(deserializedBook.getObject().id()).isEqualTo(book.id());
        assertThat(deserializedBook.getObject().isbn()).isEqualTo(book.isbn());
        assertThat(deserializedBook.getObject().title()).isEqualTo(book.title());
        assertThat(deserializedBook.getObject().genre()).isEqualTo(book.genre());
        assertThat(deserializedBook.getObject().status()).isEqualTo(book.status());
    }
}
