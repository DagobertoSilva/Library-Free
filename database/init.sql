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
  prazo_devolucao DATE NOT NULL,
  data_devolucao TIMESTAMP,
  
  livro_id BIGINT NOT NULL,
  aluno_id BIGINT NOT NULL,
  turma_id BIGINT NOT NULL,
  
  CONSTRAINT fk_emprestimo_livro FOREIGN KEY (livro_id) REFERENCES livro(id) ON DELETE RESTRICT,
  CONSTRAINT fk_emprestimo_aluno FOREIGN KEY (aluno_id) REFERENCES aluno(id) ON DELETE RESTRICT,
  CONSTRAINT fk_emprestimo_turma FOREIGN KEY (turma_id) REFERENCES turma(id) ON DELETE RESTRICT
);

-- Senha hasheada utilizando Bcrypt fator de custo 10, senha real: Libraryfree123!
INSERT INTO usuario (email, senha, tipo) 
VALUES ('admin@library.com', '$2a$10$97WYtlkULJBaLz4Oc31M6OR9PbLMpcnhtqKWxUI1MlHIJh/1lLu9u', 'BIBLIOTECARIO');

INSERT INTO bibliotecario (nome, usuario_id) 
VALUES ('Admin Bibliotecário', (SELECT id FROM usuario WHERE email = 'admin@library.com'));

-- Cursos
INSERT INTO curso (id, nome) VALUES 
(1, 'Mecânica Industrial'),
(2, 'Administração'),
(3, 'Informática'),
(4, 'Eletrotécnica');

-- Turmas
INSERT INTO turma (id, serie, ano_letivo, curso_id) VALUES 
(1, 1, 2025, 1), (2, 2, 2025, 1), (3, 3, 2025, 1), -- Mecânica
(4, 1, 2025, 2), (5, 2, 2025, 2), (6, 3, 2025, 2), -- Admnistração
(7, 1, 2025, 3), (8, 2, 2025, 3), (9, 3, 2025, 3), -- Informática
(10, 1, 2025, 4), (11, 2, 2025, 4), (12, 3, 2025, 4); -- Eletrotécnica

-- Alunos
INSERT INTO aluno (nome, matricula, turma_id, usuario_id) VALUES
('Ana Silva', '2025001', 1, NULL), ('Bruno Souza', '2025002', 1, NULL), ('Carlos Lima', '2025003', 1, NULL), ('Daniela Alves', '2025004', 1, NULL), -- Turma 1°Mecânica
('Eduardo Costa', '2025005', 2, NULL), ('Fernanda Rocha', '2025006', 2, NULL), ('Gabriel Dias', '2025007', 2, NULL), ('Helena Martins', '2025008', 2, NULL), -- Turma 2°Mecânica
('Igor Pereira', '2025009', 3, NULL), ('Julia Santos', '2025010', 3, NULL), ('Kaique Oliveira', '2025011', 3, NULL), ('Larissa Ferreira', '2025012', 3, NULL), -- Turma 3°Mecânica

('Marcos Vinicius', '2025013', 4, NULL), ('Natalia Ribeiro', '2025014', 4, NULL), ('Otavio Mesquita', '2025015', 4, NULL), ('Paula Tejano', '2025016', 4, NULL), -- Turma 1° Administração
('Quiteria Maria', '2025017', 5, NULL), ('Rafael Nogueira', '2025018', 5, NULL), ('Sabrina Boing', '2025019', 5, NULL), ('Thiago Ventura', '2025020', 5, NULL), -- Turma 2° Administração
('Ursula Andress', '2025021', 6, NULL), ('Victor Hugo', '2025022', 6, NULL), ('Wesley Safadão', '2025023', 6, NULL), ('Xuxa Meneghel', '2025024', 6, NULL), -- Turma 3° Administração

('Yuri Alberto', '2025025', 7, NULL), ('Zeca Pagodinho', '2025026', 7, NULL), ('Alan Turing', '2025027', 7, NULL), ('Ada Lovelace', '2025028', 7, NULL), -- Turma 1° Informática
('Bill Gates', '2025029', 8, NULL), ('Steve Jobs', '2025030', 8, NULL), ('Linus Torvalds', '2025031', 8, NULL), ('Grace Hopper', '2025032', 8, NULL), -- Turma 2° Informática
('Elon Musk', '2025033', 9, NULL), ('Mark Zuckerberg', '2025034', 9, NULL), ('Jeff Bezos', '2025035', 9, NULL), ('Tim Cook', '2025036', 9, NULL), -- Turma 3° Informática

('Nikola Tesla', '2025037', 10, NULL), ('Thomas Edison', '2025038', 10, NULL), ('Michael Faraday', '2025039', 10, NULL), ('James Watt', '2025040', 10, NULL), -- Turma 1° Eletrotécnica
('Alessandro Volta', '2025041', 11, NULL), ('Andre Ampere', '2025042', 11, NULL), ('Georg Ohm', '2025043', 11, NULL), ('Heinrich Hertz', '2025044', 11, NULL), -- Turma 2° Eletrotécnica
('Benjamin Franklin', '2025045', 12, NULL), ('Gustav Kirchhoff', '2025046', 12, NULL), ('Werner Siemens', '2025047', 12, NULL), ('Marie Curie', '2025048', 12, NULL); -- Turma 3° Eletrotécnica

-- Editoras
INSERT INTO editora (id, nome) VALUES 
(1, 'O''Reilly'), (2, 'Pearson'), (3, 'Novatec'), (4, 'Alta Books'), (5, 'Rocco');

-- Autores
INSERT INTO autor (id, nome) VALUES 
(1, 'Robert C. Martin'), (2, 'J.K. Rowling'), (3, 'Erich Gamma'), 
(4, 'Idalberto Chiavenato'), (5, 'Boylestad'), (6, 'George R.R. Martin');

-- Gêneros
INSERT INTO genero (id, nome) VALUES 
(1, 'Tecnologia'), (2, 'Administração'), (3, 'Engenharia'), (4, 'Fantasia'), (5, 'Ficção Científica');

-- Livros
INSERT INTO livro (id, isbn, titulo, ano_publicacao, quantidade_total, quantidade_disponivel, editora_id) VALUES
(1, '978-8576082675', 'Clean Code: A Handbook of Agile Software Craftsmanship', 2008, 10, 10, 2),
(2, '978-8575227246', 'Desbravando Java e Orientação a Objetos', 2021, 5, 5, 3),
(3, '978-8550811765', 'Arquitetura Limpa: O Guia do Artesão', 2019, 8, 8, 4),
(4, '978-0201633610', 'Design Patterns: Elements of Reusable Object-Oriented Software', 1994, 3, 3, 2),
(5, '978-8535288220', 'Introdução à Teoria Geral da Administração', 2020, 15, 15, 1),
(6, '978-8522497669', 'Gestão de Pessoas: O Novo Papel dos Recursos Humanos', 2014, 7, 7, 1),
(7, '978-8581431413', 'Introdução à Análise de Circuitos', 2012, 12, 12, 2),
(8, '978-8521617563', 'Dispositivos Eletrônicos e Teoria de Circuitos', 2013, 6, 6, 2),
(9, '978-8532530784', 'Harry Potter e a Pedra Filosofal', 1997, 20, 20, 5),
(10, '978-8532530791', 'Harry Potter e a Câmara Secreta', 1998, 18, 18, 5),
(11, '978-8556510785', 'A Guerra dos Tronos: As Crônicas de Gelo e Fogo', 1996, 10, 10, 5),
(12, '978-8576572348', 'Duna', 1965, 5, 5, 1);

-- Autores e gêneros de cada livro
INSERT INTO livro_autor (livro_id, autor_id) VALUES (1, 1);
INSERT INTO livro_genero (livro_id, genero_id) VALUES (1, 1);

INSERT INTO livro_autor (livro_id, autor_id) VALUES (2, 1);
INSERT INTO livro_genero (livro_id, genero_id) VALUES (2, 1);

INSERT INTO livro_autor (livro_id, autor_id) VALUES (3, 1);
INSERT INTO livro_genero (livro_id, genero_id) VALUES (3, 1);

INSERT INTO livro_autor (livro_id, autor_id) VALUES (4, 3);
INSERT INTO livro_genero (livro_id, genero_id) VALUES (4, 1);

INSERT INTO livro_autor (livro_id, autor_id) VALUES (5, 4);
INSERT INTO livro_genero (livro_id, genero_id) VALUES (5, 2);

INSERT INTO livro_autor (livro_id, autor_id) VALUES (6, 4);
INSERT INTO livro_genero (livro_id, genero_id) VALUES (6, 2);

INSERT INTO livro_autor (livro_id, autor_id) VALUES (7, 5);
INSERT INTO livro_genero (livro_id, genero_id) VALUES (7, 3);

INSERT INTO livro_autor (livro_id, autor_id) VALUES (8, 5);
INSERT INTO livro_genero (livro_id, genero_id) VALUES (8, 3);

INSERT INTO livro_autor (livro_id, autor_id) VALUES (9, 2);
INSERT INTO livro_genero (livro_id, genero_id) VALUES (9, 4);

INSERT INTO livro_autor (livro_id, autor_id) VALUES (10, 2);
INSERT INTO livro_genero (livro_id, genero_id) VALUES (10, 4);

INSERT INTO livro_autor (livro_id, autor_id) VALUES (11, 6);
INSERT INTO livro_genero (livro_id, genero_id) VALUES (11, 4);

INSERT INTO livro_autor (livro_id, autor_id) VALUES (12, 2);
INSERT INTO livro_genero (livro_id, genero_id) VALUES (12, 5);

SELECT setval('usuario_id_seq', (SELECT MAX(id) FROM usuario));
SELECT setval('bibliotecario_id_seq', (SELECT MAX(id) FROM bibliotecario));
SELECT setval('curso_id_seq', (SELECT MAX(id) FROM curso));
SELECT setval('turma_id_seq', (SELECT MAX(id) FROM turma));
SELECT setval('aluno_id_seq', (SELECT MAX(id) FROM aluno));
SELECT setval('editora_id_seq', (SELECT MAX(id) FROM editora));
SELECT setval('autor_id_seq', (SELECT MAX(id) FROM autor));
SELECT setval('genero_id_seq', (SELECT MAX(id) FROM genero));
SELECT setval('livro_id_seq', (SELECT MAX(id) FROM livro));