CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT current_timestamp
);

CREATE TABLE bibliotecario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    
    usuario_id BIGINT UNIQUE NOT NULL,
    
    CONSTRAINT fk_bibliotecario_usuario FOREIGN KEY(usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);

CREATE TABLE curso (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL
);

CREATE TABLE turma (
    id BIGSERIAL PRIMARY KEY,
    serie INT NOT NULL,
    ano_letivo INT NOT NULL,
    
    curso_id BIGINT NOT NULL,
    
    CONSTRAINT fk_turma_curso FOREIGN KEY (curso_id) REFERENCES curso(id) ON DELETE RESTRICT
);

CREATE TABLE aluno (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    matricula VARCHAR(20) UNIQUE NOT NULL,
    
    turma_id BIGINT NOT NULL,
    usuario_id BIGINT UNIQUE,
    
    CONSTRAINT fk_aluno_turma FOREIGN KEY(turma_id) REFERENCES turma(id) ON DELETE RESTRICT,
    CONSTRAINT fk_aluno_usuario FOREIGN KEY(usuario_id) REFERENCES usuario(id) ON DELETE SET NULL
);

CREATE TABLE editora (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(255) NOT NULL
);

CREATE TABLE livro (
  id BIGSERIAL PRIMARY KEY,
  isbn VARCHAR(20) UNIQUE,
  titulo VARCHAR(255) NOT NULL,
  ano_publicacao INT,
  quantidade_total INT NOT NULL DEFAULT 0,
  quantidade_disponivel INT NOT NULL DEFAULT 0,
  
  editora_id BIGINT,
  
  CONSTRAINT fk_livro_editora FOREIGN KEY (editora_id) REFERENCES editora(id) ON DELETE SET NULL
);

CREATE TABLE autor (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(255) NOT NULL
);

CREATE TABLE livro_autor (
  livro_id BIGINT NOT NULL,
  autor_id BIGINT NOT NULL,
  
  PRIMARY KEY(livro_id, autor_id),
  
  CONSTRAINT fk_la_livro FOREIGN KEY (livro_id) REFERENCES livro(id) ON DELETE CASCADE,
  CONSTRAINT fk_la_autor FOREIGN KEY (autor_id) REFERENCES autor(id) ON DELETE CASCADE
);

CREATE TABLE genero (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(255) NOT NULL
);

CREATE TABLE livro_genero (
  livro_id BIGINT NOT NULL,
  genero_id BIGINT NOT NULL,
  
  PRIMARY KEY(livro_id, genero_id),
  
  CONSTRAINT fk_lg_livro FOREIGN KEY (livro_id) REFERENCES livro(id) ON DELETE CASCADE,
  CONSTRAINT fk_lg_genero FOREIGN KEY (genero_id) REFERENCES genero(id) ON DELETE CASCADE
);

CREATE TABLE emprestimo (
  id BIGSERIAL PRIMARY KEY,
  data_emprestimo TIMESTAMP NOT NULL DEFAULT current_timestamp,
  prazo_devolucao TIMESTAMP NOT NULL,
  data_devolucao TIMESTAMP,
  
  livro_id BIGINT NOT NULL,
  aluno_id BIGINT NOT NULL,
  turma_id BIGINT NOT NULL,
  
  CONSTRAINT fk_emprestimo_livro FOREIGN KEY (livro_id) REFERENCES livro(id) ON DELETE RESTRICT,
  CONSTRAINT fk_emprestimo_aluno FOREIGN KEY (aluno_id) REFERENCES aluno(id) ON DELETE RESTRICT,
  CONSTRAINT fk_emprestimo_turma FOREIGN KEY (turma_id) REFERENCES turma(id) ON DELETE RESTRICT
);

-- Senha hasheada utilizand Bcrypt fator de custo 10, senha real: Libraryfree123!
INSERT INTO usuario (email, senha, tipo) 
VALUES ('admin@library.com', '$2a$10$97WYtlkULJBaLz4Oc31M6OR9PbLMpcnhtqKWxUI1MlHIJh/1lLu9u', 'BIBLIOTECARIO');

INSERT INTO bibliotecario (nome, usuario_id) 
VALUES ('Admin Bibliotecário', (SELECT id FROM usuario WHERE email = 'admin@library.com'));