CREATE TABLE `bibliotecario` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL UNIQUE,
  `senha` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `livro` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `titulo` VARCHAR(255) NOT NULL,
  `isbn` VARCHAR(20) NOT NULL UNIQUE,
  `autor` VARCHAR(255),
  `genero` VARCHAR(100),
  `edicao` VARCHAR(100),
  `publicacao` DATE,
  `editora` VARCHAR(100),
  `emprestado` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
);

CREATE TABLE `aluno` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(255) NOT NULL,
  `matricula` VARCHAR(50) NOT NULL UNIQUE,
  `cpf` VARCHAR(14) NOT NULL UNIQUE,
  `ativo` TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
);

CREATE TABLE `aluno_livro` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `aluno_id` BIGINT NOT NULL,
  `livro_id` BIGINT NOT NULL,
  `data_emprestimo` DATE NOT NULL,
  `prazo_devolucao` DATE NOT NULL,
  `data_devolucao` DATE,
  `ativo` TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`aluno_id`) REFERENCES `aluno`(`id`),
  FOREIGN KEY (`livro_id`) REFERENCES `livro`(`id`)
);

INSERT INTO bibliotecario (nome, email, senha)
VALUES ('João', 'admin@libraryfree.com', '$2a$12$k.8wU8lPYz5R/0kYoyID2OoAF91rtjJ0HsId9fuUSsTjBMYuQcG7u');

INSERT INTO livro (titulo, isbn, autor, genero, edicao, publicacao, editora, emprestado) VALUES
('O Senhor dos Anéis', '9788595084759', 'J.R.R. Tolkien', 'Alta Fantasia', 'Volume Único', '2019-11-01', 'HarperCollins Brasil', FALSE),
('Dom Casmurro', '9788535914833', 'Machado de Assis', 'Romance', 'Edição de Bolso', '2009-07-13', 'Companhia das Letras', FALSE),
('1984', '9788535914840', 'George Orwell', 'Distopia', 'Edição Clássica', '2009-07-27', 'Companhia das Letras', FALSE),
('Grande Sertão: Veredas', '9788535932554', 'João Guimarães Rosa', 'Modernismo', 'Edição Comemorativa', '2019-09-16', 'Companhia das Letras', FALSE),
('O Pequeno Príncipe', '9788595081512', 'Antoine de Saint-Exupéry', 'Fábula', 'Capa Dura', '2017-09-25', 'HarperCollins Brasil', FALSE),
('Harry Potter e a Pedra Filosofal', '9788532530783', 'J.K. Rowling', 'Fantasia', 'Edição Padrão', '2017-08-10', 'Rocco', FALSE),
('O Alquimista', '9788575427583', 'Paulo Coelho', 'Ficção', 'Edição de Luxo', '2012-06-01', 'Sextante', FALSE),
('Cem Anos de Solidão', '9788501012149', 'Gabriel García Márquez', 'Realismo Mágico', 'Edição Padrão', '2003-05-15', 'Record', FALSE),
('A Revolução dos Bichos', '9788535909556', 'George Orwell', 'Sátira Política', 'Edição de Bolso', '2007-04-16', 'Companhia das Letras', FALSE);

INSERT INTO aluno (nome, matricula, cpf, ativo) VALUES
('João da Silva', '20230001', '12345678900', TRUE),
('Maria Oliveira', '20230002', '23456789011', TRUE),
('Carlos Pereira', '20230003', '34567890122', TRUE),
('Ana Souza', '20230004', '45678901233', FALSE),
('Pedro Martins', '20230005', '56789012344', TRUE),
('Beatriz Costa', '20230006', '67890123455', TRUE),
('Lucas Almeida', '20230007', '78901234566', TRUE),
('Juliana Lima', '20230008', '89012345677', FALSE),
('Rafael Gonçalves', '20230009', '90123456788', TRUE);