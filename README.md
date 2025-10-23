# Library Free

Este projeto, desenvolvido para a disciplina de Projeto Integrador I, do curso de Ciência da Computação da Universidade Federal do Ceará - Campus Crateús, tem como objetivo aplicar os conhecimentos adquiridos ao longo da graduação na construção de uma solução real e com impacto social.

## Um pouco sobre a proposta da nossa interface

Página de Login

<img width="865" alt="Screenshot 2025-06-21 at 18 52 53" src="https://github.com/user-attachments/assets/1a34f164-8dd5-4ee5-b68b-b94a9d1082a4" />

Página Home

<img width="865" alt="Screenshot 2025-06-21 at 18 54 04" src="https://github.com/user-attachments/assets/2ed1cdd4-2e63-438f-ac9c-110c3e45d49d" />

Página Cadastro de Livros

<img width="865" alt="Screenshot 2025-06-21 at 18 54 41" src="https://github.com/user-attachments/assets/ad47d8c2-8613-4183-9dc7-59051b455168" />

Vide as demais telas no link
```
https://www.figma.com/design/MkpSO0QuVsZNEsTGx0SRUc/LibraryFree?t=3hvPOIto5kiEzrHs-0
```

## Utilizando a aplicação

### Pré-requisitos
* Docker

### Configuração
1. Crie um novo diretório para o projeto.
2. Baixe os arquivos docker-compose.yml e init.sql para este novo diretório.
3. No mesmo diretório, crie um arquivo chamado .env.
4. Preencha o arquivo .env com as seguintes variáveis, atribuindo seus próprios valores seguros:
```bash
DB_ROOT_PASSWORD=
DB_USER_PASSWORD=
```

### Executando a aplicação
1. Abra um terminal no diretório raiz do projeto.
2. Execute o seguinte comando para construir as imagens e iniciar os contêineres:
```
docker-compose up -d --build
```

### Parando a aplicação
1. Para parar e remover os contêineres, execute o seguinte comando no diretório raiz do projeto:
```
docker-compose down
```

## Documentação
[Documentação Técnica (PDF)](https://drive.google.com/file/d/11JwY8qXcqxmH8D-NBH5pFenq646Fr6po/view?usp=sharing)

