# Doar+ - Sistema de Gerenciamento de Doações e Cestas Básicas

Este projeto consiste em um software desktop completo desenvolvido para a disciplina de **Programação III** do Instituto Federal de Goiás (IFG) - Campus Luziânia. O sistema visa facilitar o gerenciamento de doações de itens alimentícios e a organização de cestas básicas para caridade.

## 🛠 Tecnologias Utilizadas

*   **Linguagem:** Java 22
*   **Interface Gráfica:** JavaFX 23
*   **Banco de Dados:** PostgreSQL
*   **Arquitetura:** MVC (Model-View-Controller)
*   **Gerenciador de Dependências:** Maven

## 📋 Requisitos Atendidos (Conforme Projeto Prático)

1.  **Autenticação de Usuário:** Sistema de login seguro por e-mail e senha.
2.  **Manter Usuário:** CRUD completo (Cadastro, Consulta, Edição e Remoção) de usuários.
3.  **Navegação:** Interface intuitiva com troca dinâmica de cenas (FXML).
4.  **Rastreabilidade e Auditoria:** Registro de todas as ações dos usuários em `log_de_auditoria.txt`.
5.  **Log de Exceções:** Registro detalhado de erros do sistema em `log_de_erros.txt`.
6.  **Persistência:** Integração robusta com PostgreSQL.

## 🚀 Casos de Uso Principais (Domínio do Problema)

O sistema vai além de cadastros simples, focando em:

### 1. Gestão Inteligente de Doações
O usuário pode registrar a entrada de itens específicos (Arroz, Feijão, Macarrão, etc.). O sistema valida as quantidades e associa a doação ao usuário logado, mantendo um histórico auditável.

### 2. Formação Automática de Cestas Básicas
O sistema analisa o estoque atual de itens doados e calcula automaticamente quantas cestas básicas completas podem ser montadas com base em regras de composição pré-definidas. Isso permite uma visão real da capacidade de atendimento da instituição.

## ⚙️ Como Rodar Localmente

### Pré-requisitos
*   Java 22+ instalado.
*   PostgreSQL instalado e rodando.
*   Maven configurado.

### Passo 1: Configurar o Banco de Dados
Crie um banco de dados no PostgreSQL e execute o script abaixo (ou verifique a classe `DbContext.java` para ajustar as credenciais):

```sql
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(100) NOT NULL,
    isAdmin BOOLEAN DEFAULT FALSE,
    criadoEm DATE DEFAULT CURRENT_DATE
);

CREATE TABLE doacoes (
    id SERIAL PRIMARY KEY,
    item VARCHAR(50) NOT NULL,
    quantidade INTEGER NOT NULL,
    usuario_id INTEGER REFERENCES usuarios(id),
    criadoEm DATE DEFAULT CURRENT_DATE
);
```

### Passo 2: Configurar Conexão
Edite o arquivo `src/main/java/com/doarmais/model/infra/contexto/DbContext.java` com seu usuário e senha do PostgreSQL:
```java
private final String url = "jdbc:postgresql://localhost:5432/doarmais";
private final String user = "seu_usuario";
private final String password = "sua_senha";
```

### Passo 3: Compilar e Executar
No terminal, dentro da pasta do projeto, execute:
```bash
mvn clean compile
mvn javafx:run
```

## 📄 Logs
*   **Auditoria:** `log_de_auditoria.txt` registra quem fez o quê e quando.
*   **Erros:** `log_de_erros.txt` registra falhas técnicas para suporte e correção.

---
**Desenvolvido por:** Luciano Oliveira Borges Souza
**Professor:** Dr. Daniel Lucena
**Instituição:** IFG - Campus Luziânia
