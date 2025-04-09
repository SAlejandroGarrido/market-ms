# 🌟 Spring Market Application

## 🛠️ Tecnologias Utilizadas

- 🖥️ **Java**: Linguagem principal do projeto.
- 🌱 **Spring Boot**: Framework para criação de aplicações Java.
- 📦 **Maven**: Gerenciador de dependências e build.
- 🌐 **Feign**: Cliente HTTP para comunicação com APIs externas.
- ✂️ **Lombok**: Redução de boilerplate no código.
- 📊 **Actuator**: Monitoramento e métricas da aplicação.
- 📈 **Prometheus**: Coleta e armazenamento de métricas.
- 📉 **Grafana**: Visualização e análise de métricas.
- 🐳 **Docker**: Containerização da aplicação para facilitar o deploy e a escalabilidade.
- ✅ **JaCoCo**: Ferramenta para análise de cobertura de código.
- 📜 **Swagger**: Documentação interativa para APIs REST.
- 🔄 **ModelMapper**: Mapeamento de objetos.
- 📜 **Swagger/OpenAPI**: Documentação interativa para APIs REST.
- 🧪 **JUnit 5**: Framework para criação de testes unitários.
- 🛠️ **Mockito**: Framework para criação de mocks em testes.
- 
## 🏗️ Arquitetura

A aplicação segue os princípios da **Arquitetura Hexagonal** (🔌 **Ports and Adapters**).

### 🗂️ Estrutura do Projeto

- 📁 **Application**: Contém as regras de negócio e entidades principais e casos de uso e interfaces que conectam o
  domínio com o mundo externo.
- 📁 **Adapter**: Implementações concretas para comunicação com APIs externas, banco de dados ou interfaces web.

## 🔍 Observabilidade

A aplicação está configurada para monitoramento e visualização de métricas utilizando as seguintes ferramentas:

- 🛠️ **Spring Boot Actuator**: Exposição de métricas e informações de sa��de da aplicação em `/actuator`.
- 📊 **Prometheus**: Coleta e armazenamento de métricas no formato configurado pelo Actuator.
- 📉 **Grafana**: Visualização das métricas coletadas pelo Prometheus em dashboards customizáveis.
- 🐳 **Docker**: Facilita a execução de Prometheus e Grafana em containers.

### ⚙️ Configuração de Observabilidade

1. **Prometheus**:
    - Configurado para coletar métricas da aplicação no endpoint `/actuator/prometheus`.

2. **Grafana**:
    - Conectado ao Prometheus como fonte de dados.
    - Dashboards podem ser criados para monitorar métricas como tempo de resposta, uso de memória e número de
      requisições.

3. **Docker**:
    - Um `docker-compose.yml` pode ser usado para subir a aplicação, Prometheus e Grafana:

## 📊 Cobertura de Código

A aplicação utiliza o **JaCoCo** para análise de cobertura de código. A configuração atual exige uma cobertura mínima de
90% para as classes principais, com exceções configuradas para:

- **Modelos** (`model`)
- **DTOs** (`dto`)
- **Exceções** (`exception`)
- **Configurações** (`config`)
- **Classe principal da aplicação**
- **Utilitários** (`util`)

## 🔮 Próximas Features

- 🛡️ **SonarQube**: Integração para análise de qualidade de código e detecção de vulnerabilidades.
- ☸️ **Kubernetes**: Arquivos de configuração para deploy da aplicação em clusters Kubernetes, garantindo escalabilidade
  e alta disponibilidade.

## 📋 Endpoints Disponíveis

### **👥 Clientes**

Base Path: `/v1/api/market/customers`

- **GET /loyal**  
  Retorna uma lista com o TOP 3 de clientes mais fiéis.  

- **GET /recommendation/type**  
  Retorna lista de recomendações de produtos com base em compras recorrentes.  

### **🛒 Compras**

Base Path: `/v1/api/market/purchases`

- **GET /**  
  Retorna todas uma lista com as compras ordenadas por valor.  

- **GET /{year}**  
  Retorna a maior compra com base ano especificado.  


### Como Gerar o Relatório de Cobertura

1. Execute os testes com o comando:
   ```bash
   mvn clean verify
2. Collection Postman no projeto com testes funcionais   