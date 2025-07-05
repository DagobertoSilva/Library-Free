INSERT INTO Bibliotecario (nome, email, senha)
VALUES ('Felipe Ferreira', 'admin@libraryfree.com', '$2a$12$oNyraVm7M8IjdcP8m9EUj.iKkPUBLeIcp8XZYJ1Ghz2xO7C3AHPbu');

INSERT INTO Livro (titulo, isbn, autor, genero, edicao, publicacao, editora, emprestado) VALUES
('O Senhor dos Anéis', '9788595084759', 'J.R.R. Tolkien', 'Alta Fantasia', 'Volume Único', '2019-11-01', 'HarperCollins Brasil', FALSE),
('Dom Casmurro', '9788535914833', 'Machado de Assis', 'Romance', 'Edição de Bolso', '2009-07-13', 'Companhia das Letras', FALSE),
('1984', '9788535914840', 'George Orwell', 'Distopia', 'Edição Clássica', '2009-07-27', 'Companhia das Letras', FALSE),
('Grande Sertão: Veredas', '9788535932554', 'João Guimarães Rosa', 'Modernismo', 'Edição Comemorativa', '2019-09-16', 'Companhia das Letras', TRUE);

INSERT INTO Aluno (nome, matricula, cpf, ativo) VALUES
('João da Silva', '20230001', '123.456.789-00', TRUE),
('Maria Oliveira', '20230002', '234.567.890-11', TRUE),
('Carlos Pereira', '20230003', '345.678.901-22', TRUE),
('Ana Souza', '20230004', '456.789.012-33', FALSE);

INSERT INTO Aluno_Livro (aluno_id, livro_id, data_emprestimo, prazo_devolucao, data_devolucao, ativo) VALUES
(1, 1, '2025-07-01', '2025-07-15', NULL, TRUE);
INSERT INTO Aluno_Livro (aluno_id, livro_id, data_emprestimo, prazo_devolucao, data_devolucao, ativo) VALUES
(1, 2, '2025-07-03', '2025-07-17', NULL, TRUE);


