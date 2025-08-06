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
public class EmprestimoJSONTests {
    @Autowired
    JacksonTester<Emprestimo> jacksonTester;

    private final Emprestimo emprestimo = new Emprestimo(
            1L,
            new Aluno(1L, "Fulano", "202501001", "12345678900", true),
            new Livro(1L, "O Senhor dos Anéis", "9788595084759", "J.R.R. Tolkien", "Fantasia", "Volume Único", LocalDate.of(2019, 11, 1), "HarperCollins Brasil", false),
            LocalDate.of(2025, 7, 1),
            LocalDate.of(2025, 7, 15),
            null,
            true
    );

    @Test
    void EmprestimoSerializationTest() throws IOException, IOException {
        JsonContent<Emprestimo> serializedEmprestimo = jacksonTester.write(emprestimo);
        assertThat(serializedEmprestimo).isNotNull();
        assertThat(serializedEmprestimo).hasJsonPathNumberValue("@.id");
        assertThat(serializedEmprestimo).extractingJsonPathNumberValue("@.id").isEqualTo(emprestimo.getId().intValue());
        assertThat(serializedEmprestimo).hasJsonPathStringValue("@.dataEmprestimo");
        assertThat(serializedEmprestimo).extractingJsonPathStringValue("@.dataEmprestimo").isEqualTo("2025-07-01");
        assertThat(serializedEmprestimo).hasJsonPathStringValue("@.prazoDevolucao");
        assertThat(serializedEmprestimo).extractingJsonPathStringValue("@.prazoDevolucao").isEqualTo("2025-07-15");
        assertThat(serializedEmprestimo).hasJsonPath("@.dataDevolucao");
        assertThat(serializedEmprestimo).extractingJsonPathValue("@.dataDevolucao").isNull();
        assertThat(serializedEmprestimo).hasJsonPathBooleanValue("@.ativo");
        assertThat(serializedEmprestimo).extractingJsonPathBooleanValue("@.ativo").isEqualTo(emprestimo.getAtivo());
        assertThat(serializedEmprestimo).doesNotHaveJsonPath("@.aluno");
        assertThat(serializedEmprestimo).doesNotHaveJsonPath("@.livro");
    }

    @Test
    void testDeserialization() throws IOException {
        String expected = """
        {
            "id": 1,
            "dataEmprestimo": "2025-07-01",
            "prazoDevolucao": "2025-07-15",
            "dataDevolucao": null,
            "ativo": true
        }
        """;
        ObjectContent<Emprestimo> deserializedEmprestimo = jacksonTester.parse(expected);
        assertThat(deserializedEmprestimo.getObject().getId()).isEqualTo(emprestimo.getId());
        assertThat(deserializedEmprestimo.getObject().getDataEmprestimo()).isEqualTo(emprestimo.getDataEmprestimo());
        assertThat(deserializedEmprestimo.getObject().getPrazoDevolucao()).isEqualTo(emprestimo.getPrazoDevolucao());
        assertThat(deserializedEmprestimo.getObject().getDataDevolucao()).isEqualTo(emprestimo.getDataDevolucao());
        assertThat(deserializedEmprestimo.getObject().getAtivo()).isEqualTo(emprestimo.getAtivo());
        assertThat(deserializedEmprestimo.getObject().getAluno()).isNull();
        assertThat(deserializedEmprestimo.getObject().getLivro()).isNull();
    }
}
