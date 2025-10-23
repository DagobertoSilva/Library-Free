# Library Free

Este projeto, desenvolvido para a disciplina de Projeto Integrador I, do curso de Ciência da Computação da Universidade Federal do Ceará - Campus Crateús, tem como objetivo aplicar os conhecimentos adquiridos ao longo da graduação na construção de uma solução real e com impacto social.

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

```bash
docker-compose up -d --build
```

3. Após os contêineres iniciarem, acesse a aplicação no seu navegador através do seguinte link:

```bash
http://127.0.0.1:5500/pages/login.html
```

4. Utilize as seguintes credenciais de teste para acessar o sistema:
* Email: admin@libraryfree.com
* Senha: libraryfree

### Parando a aplicação
1. Para parar e remover os contêineres, execute o seguinte comando no diretório raiz do projeto:

```bash
docker-compose down
```

## Documentação
[Documentação Técnica (PDF)](https://drive.google.com/file/d/11JwY8qXcqxmH8D-NBH5pFenq646Fr6po/view?usp=sharing)
