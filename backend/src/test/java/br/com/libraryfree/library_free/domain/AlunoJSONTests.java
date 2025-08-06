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
public class AlunoJSONTests {
    @Autowired
    JacksonTester<Aluno> jacksonTester;

    private final Aluno aluno = new Aluno(
            1L,
            "Fulano",
            "202501001",
            "12345678900",
            true
    );

    @Test
    public void AlunoSerializationTest() throws IOException {
        JsonContent<Aluno> serializedAluno = jacksonTester.write(aluno);
        assertThat(serializedAluno).isNotNull();
        assertThat(serializedAluno).isStrictlyEqualToJson("Aluno.json");
        assertThat(serializedAluno).hasJsonPathNumberValue("@.id");
        assertThat(serializedAluno).extractingJsonPathNumberValue("@.id").isEqualTo(aluno.getId().intValue());
        assertThat(serializedAluno).hasJsonPathStringValue("@.nome");
        assertThat(serializedAluno).extractingJsonPathStringValue("@.nome").isEqualTo(aluno.getNome());
        assertThat(serializedAluno).hasJsonPathStringValue("@.matricula");
        assertThat(serializedAluno).extractingJsonPathStringValue("@.matricula").isEqualTo(aluno.getMatricula());
        assertThat(serializedAluno).hasJsonPathStringValue("@.cpf");
        assertThat(serializedAluno).extractingJsonPathStringValue("@.cpf").isEqualTo(aluno.getCpf());
        assertThat(serializedAluno).hasJsonPathBooleanValue("@.ativo");
        assertThat(serializedAluno).extractingJsonPathBooleanValue("@.ativo").isEqualTo(aluno.getAtivo());
        assertThat(serializedAluno).hasJsonPathArrayValue("@.emprestimos");
        assertThat(serializedAluno).extractingJsonPathArrayValue("@.emprestimos").isEqualTo(aluno.getEmprestimos());
    }

    @Test
    public void AlunoDeserializationTest() throws IOException {
        String expected = """
        {
            "id": 1,
            "nome": "Fulano",
            "matricula": "202501001",
            "cpf": "12345678900",
            "ativo": true,
            "emprestimos": []
        }
        """;
        ObjectContent<Aluno> deserializedAluno = jacksonTester.parse(expected);
        assertThat(deserializedAluno).isEqualTo(aluno);
        assertThat(deserializedAluno.getObject().getId()).isEqualTo(aluno.getId());
        assertThat(deserializedAluno.getObject().getNome()).isEqualTo(aluno.getNome());
        assertThat(deserializedAluno.getObject().getMatricula()).isEqualTo(aluno.getMatricula());
        assertThat(deserializedAluno.getObject().getCpf()).isEqualTo(aluno.getCpf());
        assertThat(deserializedAluno.getObject().getAtivo()).isEqualTo(aluno.getAtivo());
        assertThat(deserializedAluno.getObject().getEmprestimos()).isEqualTo(aluno.getEmprestimos());
    }
}
