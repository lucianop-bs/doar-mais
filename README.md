# Doar+ Desktop 🤝 📦

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![JavaFX](https://img.shields.io/badge/JavaFX-21-blue?style=for-the-badge&logo=java)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge&logo=postgresql)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?style=for-the-badge&logo=apache-maven)

O **Doar+ Desktop** é uma solução robusta para o gerenciamento de doações de alimentos e montagem de cestas básicas. Desenvolvido para a disciplina de **Programação III** no IFG Luziânia, o sistema foca em performance, rastreabilidade e facilidade de uso em ambientes de balcão/estoque.

---

## 📸 Demonstração
*(Espaço para GIF ou Screenshots do Sistema)*
> **Dica:** Adicione imagens da tela de Login, Dashboard e o Carrinho de Entrada aqui.

---

## ✨ Funcionalidades Principais

- **🔐 Autenticação Segura:** Sistema de login com criptografia simples e níveis de acesso (Admin/Operador).
- **🛒 Carrinho de Entrada:** Agilidade no recebimento de múltiplas doações em uma única transação.
- **📦 Gestão de Estoque Dinâmica:** Controle em tempo real com cálculo automático de quantas cestas podem ser montadas.
- **👥 Cadastro de Beneficiários:** Registro completo de entregas para garantir que a ajuda chegue a quem precisa.
- **📜 Auditoria Completa:** Logs detalhados de todas as ações críticas em `log_de_auditoria.txt`.
- **🚨 Error Handling:** Sistema de logs técnicos para fácil manutenção em `log_de_erros.txt`.

---

## 🏗️ Arquitetura e Padrões

O projeto segue rigorosamente os padrões da engenharia de software:
- **MVC (Model-View-Controller):** Separação clara entre interface, lógica e dados.
- **DAO (Data Access Object):** Abstração completa da camada de persistência.
- **BO (Business Object):** Centralização das regras de negócio.
- **Singleton:** Garantia de conexão única com o banco de dados.

---

## 📂 Estrutura do Projeto

```text
doarmais/
├── src/main/java/com/doarmais/
│   ├── controller/      # Controladores JavaFX (Eventos e Telas)
│   ├── model/
│   │   ├── dao/         # Comunicação com Banco de Dados
│   │   ├── bo/          # Regras de Negócio e Validações
│   │   ├── entity/      # Classes de Modelo (POJOs)
│   │   └── infra/       # Configurações de Conexão e Logs
│   └── view/            # Arquivos FXML e Estilização CSS
├── src/main/resources/  # Assets, Imagens e Configurações
├── lib/                 # Dependências externas (Driver JDBC)
└── pom.xml              # Configuração Maven
```

---

## ⚙️ Como Rodar Localmente

### 1. Pré-requisitos
*   **JDK 21** ou superior.
*   **PostgreSQL** rodando localmente.
*   **Maven** instalado e configurado no PATH.

### 2. Preparação do Banco de Dados
Crie um banco chamado `doarmais_db` e execute o script de inicialização:

```sql
-- Script simplificado para setup rápido
CREATE TABLE usuarios (id SERIAL PRIMARY KEY, nome VARCHAR(100), email VARCHAR(100) UNIQUE, senha VARCHAR(100), isAdmin BOOLEAN);
INSERT INTO usuarios (nome, email, senha, isAdmin) VALUES ('Administrador', 'admin', 'admin', TRUE);

CREATE TABLE tipos_item (id SERIAL PRIMARY KEY, nome VARCHAR(50) UNIQUE, descricao VARCHAR(100));
INSERT INTO tipos_item (nome, descricao) VALUES ('ARROZ', 'Arroz'), ('FEIJAO', 'Feijão'), ('OLEO', 'Óleo');

CREATE TABLE estoque (nome_item VARCHAR(50) PRIMARY KEY REFERENCES tipos_item(nome), quantidade INTEGER DEFAULT 0);
CREATE TABLE doacoes (id SERIAL PRIMARY KEY, nome_item VARCHAR(50) REFERENCES tipos_item(nome), quantidade INTEGER, usuario_id INTEGER, data_doacao DATE DEFAULT CURRENT_DATE);
CREATE TABLE distribuicoes (id SERIAL PRIMARY KEY, beneficiario VARCHAR(100), quantidade_cestas INTEGER, usuario_id INTEGER, data_distribuicao DATE DEFAULT CURRENT_DATE);
```

### 3. Configuração de Acesso
Ajuste as credenciais em: `src/main/java/com/doarmais/model/infra/contexto/ConnectionFactory.java`

### 4. Execução
```bash
mvn clean compile
mvn javafx:run
```

---

## 👨‍💻 Autor
**Luciano Oliveira Borges Souza**
*   Professor: Dr. Daniel Lucena
*   Instituição: IFG - Campus Luziânia

---
## 📄 Licença
Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para detalhes.
