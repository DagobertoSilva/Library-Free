# Library Free

Este projeto foi desenvolvido para a disciplina de **Projeto Integrador I e II**, do curso de **Ciência da Computação** da **Universidade Federal do Ceará – Campus Crateús**, com o objetivo de aplicar os conhecimentos adquiridos ao longo da graduação na construção de uma solução real e com impacto social.

O **Library Free** é um sistema de gerenciamento de bibliotecas voltado para instituições de ensino, permitindo o controle de acervo, usuários e empréstimos de forma informatizada, segura e eficiente.

---

## Funcionalidades do Sistema

- Autenticação de bibliotecário
- Cadastro, leitura, edição e exclusão de livros
- Cadastro, leitura, edição e exclusão de alunos
- Consulta ao acervo por título, autor ou gênero
- Controle de empréstimos e devoluções
- Controle automático de disponibilidade de exemplares

---

## Tecnologias Utilizadas

### Front-end
-   HTML5
-   CSS3
-   JavaScript
-   Bootstrap
    

### Back-end
-   Java
-   Spring Boot
    
### Banco de Dados
-   PostgreSQL
    

### Infraestrutura
-   Docker
-   Docker Compose
-   Maven
-   NGINX

---

## Guia de instalação

### Pré-requisitos

- Docker
- Docker Compose

---

### 1. Clonar o repositório

```bash
git clone https://github.com/DagobertoSilva/Library-Free.git
cd Library-Free
```

### 2. Executando a aplicação
    
```bash
docker-compose up -d
```

### 3. Acesse a aplicação no navegador
    
```bash
http://localhost:5051
```
### 4. Utilize as credenciais de teste
    
-   **Email:** admin@library.com
-   **Senha:** Libraryfree123!
    
----------

## Estrutura do Projeto
```
.
|-- backend                      <- Aplicação Spring Boot (API)
|   |-- src
|   |   `-- main
|   |       |-- java/libraryfree/app
|   |       |   |-- auth         <- Lógica de autenticação
|   |       |   |-- controller   <- Endpoints da API (REST)
|   |       |   |-- domain       <- Entidades do banco de dados (JPA)
|   |       |   |-- dto          <- Data Transfer Objects
|   |       |   |-- enumerator   <- Enums do sistema
|   |       |   |-- exception    <- Tratamento de erros customizados
|   |       |   |-- mapper       <- Conversores (Entity <-> DTO)
|   |       |   |-- repository   <- Interfaces de acesso a dados
|   |       |   |-- security     <- Configurações de segurança (Spring Security)
|   |       |   |-- service      <- Regras de negócio
|   |       |   `-- LibraryFreeApplication.java <- Classe principal
|   |       `-- resources
|   |           `-- application.properties <- Configurações do Spring
|   |-- .gitignore               <- Arquivos ignorados pelo Git no backend
|   |-- Dockerfile               <- Instruções para criar a imagem Java
|   |-- mvnw / mvnw.cmd          <- Wrappers do Maven
|   `-- pom.xml                  <- Dependências do projeto (Maven)
|
|-- database                     <- Scripts de banco de dados
|   `-- init.sql                 <- Script inicial
|
|-- frontend                     <- Aplicação Web
|   |-- .vscode                  <- Configurações do editor
|   |-- css                      <- Arquivos css
|   |-- img                      <- Imagens
|   |-- javascript               <- Arquivos JavaScript
|   |-- pages                    <- Arquivos HTML
|   |-- .gitignore               <- Arquivos ignorados pelo Git no frontend
|   |-- Dockerfile               <- Instruções para criar a imagem docker
|   `-- nginx.conf               <- Configuração do servidor web reverso
|
|-- docker-compose.yml           <- Orquestração dos containers
|-- LICENSE                      <- Licença do projeto
`-- README.md                    <- Este arquivo
```

----------

## Documentação

[Documentação Técnica (PDF)](https://drive.google.com/file/d/1vwcsn1PwyPpeR9MhCIIEXNs7v5aye57O/view)
