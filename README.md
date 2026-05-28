# Coupon API

API REST para gerenciamento de cupons, construída com Java e Spring Boot.

## Sobre o sistema

O **Coupon API** é um serviço backend responsável por cadastrar, consultar e gerenciar cupons promocionais.  
A aplicação segue uma arquitetura em camadas, com separação de responsabilidades entre:

- **Controller**: expõe os endpoints HTTP
- **Service**: contém as regras de negócio
- **Repository**: acesso a dados/persistência
- **Domain**: entidades e modelos da aplicação
- **Mapper**: conversão entre entidades e DTOs
- **Exception**: tratamento centralizado de erros

Pela estrutura do projeto, o sistema está preparado para diferentes ambientes de execução (`dev`, `prod`, `test`) via arquivos de configuração específicos.

## Tecnologias utilizadas

### Linguagem e plataforma
- **Java**
- **Spring Boot**

### Frameworks e módulos principais
- **Spring Web** (criação de API REST)
- **Spring Data JPA** (camada de persistência)
- **Spring Validation** (validação de entradas, se aplicável)
- **Spring Boot Test** (testes automatizados)

> Observação: os módulos exatos dependem das dependências declaradas no `build.gradle`.

### Build e gerenciamento de dependências
- **Gradle** (`build.gradle`)
- **Gradle Wrapper** (`gradlew`, `gradlew.bat`)

### Configuração e ambientes
- Arquivos de configuração por perfil:
    - `src/main/resources/application.yaml`
    - `src/main/resources/application-dev.yaml`
    - `src/main/resources/application-prod.yaml`
    - `src/test/resources/application-test.yaml`

### Testes
- Testes de unidade e integração em:
    - `src/test/java/...`
- Relatórios de teste gerados em:
    - `build/reports/tests/test/index.html`

### Containerização (suporte)
- **Docker Compose** (`docker-compose.yml`) para orquestração de serviços locais (ex.: banco de dados), quando configurado.