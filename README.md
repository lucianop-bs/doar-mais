# Doar+ - Sistema de Gerenciamento de Doações e Cestas Básicas

Este projeto consiste em um software desktop completo desenvolvido para a disciplina de **Programação III** do Instituto Federal de Goiás (IFG) - Campus Luziânia. O sistema visa facilitar o gerenciamento de doações de itens alimentícios, controle de estoque dinâmico e a distribuição de cestas básicas para beneficiários.

## 🛠 Tecnologias Utilizadas

*   **Linguagem:** Java 21 (LTS)
*   **Interface Gráfica:** JavaFX 21
*   **Banco de Dados:** PostgreSQL
*   **Arquitetura:** MVC (Model-View-Controller) com Padrão BO (Business Object)
*   **Gerenciador de Dependências:** Maven

## 📋 Requisitos Atendidos

1.  **Autenticação de Usuário:** Login por e-mail e senha com controle de privilégios.
2.  **Gestão de Usuários (CRUD):** Administradores podem gerenciar contas de usuários.
3.  **Catálogo Dinâmico de Itens:** Gerenciamento de tipos de doações via banco de dados (sem enums fixos).
4.  **Entrada em Lote (Carrinho):** Interface agilizada para receber múltiplas doações de uma só vez.
5.  **Controle de Estoque:** Atualização automática e cálculo real da capacidade de cestas básicas.
6.  **Registro de Beneficiários:** Histórico de para quem as cestas foram distribuídas.
7.  **Rastreabilidade e Auditoria:** Registro de ações em `log_de_auditoria.txt`.
8.  **Tratamento de Exceções:** Logs técnicos detalhados em `log_de_erros.txt`.

## 🚀 Funcionalidades Principais

### 1. Recebimento Inteligente
O sistema utiliza um modelo de "Carrinho de Entrada", onde o usuário adiciona itens a uma lista temporária, podendo inclusive usar o atalho de **Cesta Completa** para preencher todos os itens essenciais com um clique, finalizando o processo de uma única vez.

### 2. Distribuição e Rastreio
Ao entregar cestas, o sistema exige o nome do beneficiário, decrementa o estoque de forma proporcional e registra a transação vinculada ao operador atual.

## ⚙️ Como Rodar Localmente

### Pré-requisitos
*   Java 21 instalado.
*   PostgreSQL instalado e rodando.
*   Maven configurado.

### Passo 1: Configurar o Banco de Dados
Crie um banco de dados chamado `doarmais_db` no PostgreSQL e execute o script abaixo:

```sql
-- Tabela de Usuários
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(100) NOT NULL,
    isAdmin BOOLEAN DEFAULT FALSE,
    criadoEm DATE DEFAULT CURRENT_DATE
);

-- Usuário Administrador Padrão
INSERT INTO usuarios (nome, email, senha, isAdmin) 
VALUES ('Administrador', 'admin', 'admin', TRUE)
ON CONFLICT (email) DO NOTHING;

-- Tabela de Catálogo de Itens (Substituiu o Enum)
CREATE TABLE tipos_item (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) UNIQUE NOT NULL,
    descricao VARCHAR(100) NOT NULL
);

-- Carga inicial do catálogo
INSERT INTO tipos_item (nome, descricao) VALUES 
('ARROZ', 'Arroz'), ('FEIJAO', 'Feijão'), ('MACARRAO', 'Macarrão'), 
('CUSCUZ', 'Flocão de Milho / Cuscuz'), ('OLEO', 'Óleo de Soja'), 
('CAFE', 'Café'), ('ACUCAR', 'Açúcar'), ('SAL', 'Sal'), 
('LEITE', 'Leite'), ('BISCOITO', 'Biscoito')
ON CONFLICT (nome) DO NOTHING;

-- Histórico de Doações Recebidas
CREATE TABLE doacoes (
    id SERIAL PRIMARY KEY,
    nome_item VARCHAR(50) NOT NULL REFERENCES tipos_item(nome),
    quantidade INTEGER NOT NULL,
    usuario_id INTEGER REFERENCES usuarios(id),
    data_doacao DATE DEFAULT CURRENT_DATE
);

-- Controle de Estoque Atual
CREATE TABLE estoque (
    nome_item VARCHAR(50) PRIMARY KEY REFERENCES tipos_item(nome),
    quantidade INTEGER NOT NULL DEFAULT 0
);

-- Registro de Entregas (Beneficiários)
CREATE TABLE distribuicoes (
    id SERIAL PRIMARY KEY,
    beneficiario VARCHAR(100) NOT NULL,
    quantidade_cestas INTEGER NOT NULL,
    usuario_id INTEGER REFERENCES usuarios(id),
    data_distribuicao DATE DEFAULT CURRENT_DATE
);
```

### Passo 2: Configurar Conexão
Verifique ou edite o arquivo `src/main/java/com/doarmais/model/infra/contexto/ConnectionFactory.java` com suas credenciais:
```java
private static final String URL = "jdbc:postgresql://localhost:5432/doarmais_db";
private static final String USER = "postgres";
private static final String PASSWORD = "admin";
```

### Passo 3: Compilar e Executar
No terminal, dentro da pasta do projeto, execute:
```bash
mvn clean compile
mvn javafx:run
```

## 📄 Logs
*   **Auditoria:** `log_de_auditoria.txt` registra quem realizou cada doação ou distribuição.
*   **Erros:** `log_de_erros.txt` captura o stacktrace de qualquer falha inesperada.

---
**Desenvolvido por:** Luciano Oliveira Borges Souza
**Professor:** Dr. Daniel Lucena
**Instituição:** IFG - Campus Luziânia
