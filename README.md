# Pedidos - Microsserviço de Gestão de Pedidos

Microsserviço responsável pelo gerenciamento completo do ciclo de vida de pedidos dentro do ecossistema Bytecooks.

## Visão Geral

Este serviço centraliza todas as operações relacionadas a pedidos, desde a criação até a finalização, incluindo validações de negócio, controle de status, integração com o serviço de pagamentos e gestão do histórico completo de pedidos.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Data JPA** - Persistência de dados
- **Spring Web** - APIs RESTful
- **Spring Cloud Netflix Eureka Client** - Service Discovery
- **Spring Cloud OpenFeign** - Comunicação declarativa entre microsserviços
- **Hibernate** - ORM e validação
- **MySQL** - Banco de dados relacional
- **Lombok** - Redução de código boilerplate
- **Spring Boot DevTools** - Ferramentas de desenvolvimento
- **Bean Validation** - Validação de entrada de dados

## Pré-requisitos

- JDK 17 ou superior
- Maven 3.6+
- MySQL 8.0+
- Eureka Server rodando em `http://localhost:8761`
- IDE de sua preferência (IntelliJ IDEA, Eclipse, VS Code)

## Configuração do Ambiente

### Banco de Dados

Crie um banco de dados MySQL para a aplicação:

```sql
CREATE DATABASE pedidos_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Variáveis de Ambiente

Configure as seguintes variáveis de ambiente ou ajuste o `application.properties`:

```properties
DB_HOST=localhost
DB_PORT=3306
DB_NAME=pedidos_db
DB_USERNAME=root
DB_PASSWORD=sua_senha
EUREKA_SERVER_URL=http://localhost:8761/eureka/
```

## Instalação e Execução

### Clonar o Repositório

```bash
git clone git@github.com:WaldirJuniorGPN/cybercooks-pedidos.git
cd pedidos
```

### Compilar o Projeto

```bash
mvn clean install
```

### Executar a Aplicação

```bash
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8082`

## Arquitetura

O microsserviço segue os princípios de **Clean Architecture** e **Domain-Driven Design (DDD)**, organizando o código em camadas bem definidas:

### Estrutura de Pacotes

```
br.com.cybercooks.pedidos
├── controller          # Recebe requisições HTTP
├── service            # Lógica de negócio e regras de domínio
├── repository         # Acesso aos dados
├── domain
│   ├── entity         # Entidades JPA
│   ├── valueobject    # Value Objects (CPF, Email, etc)
│   └── enums          # Enumerações de domínio
├── dto
│   ├── request        # DTOs de entrada
│   └── response       # DTOs de saída
├── client             # Clientes Feign para outros microsserviços
├── config             # Configurações
└── exception          # Exceções customizadas e handler
```

### Princípios Aplicados

- **SOLID**: Todos os princípios são seguidos rigorosamente
    - **SRP**: Cada classe tem uma única responsabilidade
    - **OCP**: Extensível através de abstrações, não modificações
    - **LSP**: Subtipos substituíveis sem quebrar o comportamento
    - **ISP**: Interfaces específicas e coesas
    - **DIP**: Dependência de abstrações, não implementações
- **Object Calisthenics**: Código limpo e manutenível
    - Apenas um nível de indentação por método
    - Evitar uso de ELSE
    - Value Objects para primitivos
    - Tell, Don't Ask
- **DRY**: Evitar duplicação de código
- **YAGNI**: Implementar apenas o necessário

## Integração com Outros Microsserviços

### Comunicação com Pagamentos

O serviço de Pedidos se comunica com o serviço de Pagamentos através do **OpenFeign Client**:

```java
@FeignClient(name = "pagamentos")
public interface PagamentosClient {
    
    @PostMapping("/api/v1/pagamentos")
    PagamentoResponse criarPagamento(@RequestBody PagamentoRequest request);
    
    @GetMapping("/api/v1/pagamentos/{id}")
    PagamentoResponse buscarPorId(@PathVariable Long id);
    
    @PatchMapping("/api/v1/pagamentos/{id}/status")
    void atualizarStatus(@PathVariable Long id, @RequestBody StatusPagamento status);
}
```

### Descoberta de Serviços

O microsserviço se registra automaticamente no **Eureka Server**, permitindo que outros serviços o descubram dinamicamente. A comunicação entre microsserviços acontece através dos nomes lógicos registrados no Eureka, sem necessidade de configuração manual de endereços.

### Fluxo de Criação de Pedido

1. Cliente envia requisição POST para criar pedido
2. Serviço valida os dados do pedido
3. Pedido é persistido com status PENDENTE
4. Serviço comunica com microsserviço de Pagamentos via Feign
5. Pagamento é criado e associado ao pedido
6. Status do pedido é atualizado conforme resultado do pagamento
7. Resposta é retornada ao cliente

## API Endpoints

### Pedidos

```http
POST   /api/v1/pedidos              # Criar novo pedido
GET    /api/v1/pedidos/{id}         # Buscar pedido por ID
GET    /api/v1/pedidos               # Listar pedidos (paginado)
PATCH  /api/v1/pedidos/{id}/status  # Atualizar status do pedido
DELETE /api/v1/pedidos/{id}         # Cancelar pedido
```

### Exemplos de Requisições

#### Criar Pedido

```json
POST /api/v1/pedidos
Content-Type: application/json

{
  "clienteId": 1,
  "itens": [
    {
      "produtoId": 10,
      "quantidade": 2,
      "valorUnitario": 25.90
    },
    {
      "produtoId": 15,
      "quantidade": 1,
      "valorUnitario": 45.00
    }
  ],
  "endereco": {
    "logradouro": "Rua das Flores",
    "numero": "123",
    "complemento": "Apto 45",
    "bairro": "Centro",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01234-567"
  },
  "formaPagamento": "CARTAO_CREDITO"
}
```

#### Resposta de Sucesso

```json
{
  "id": 1,
  "clienteId": 1,
  "status": "AGUARDANDO_PAGAMENTO",
  "valorTotal": 96.80,
  "itens": [
    {
      "produtoId": 10,
      "quantidade": 2,
      "valorUnitario": 25.90,
      "subtotal": 51.80
    },
    {
      "produtoId": 15,
      "quantidade": 1,
      "valorUnitario": 45.00,
      "subtotal": 45.00
    }
  ],
  "endereco": {
    "logradouro": "Rua das Flores",
    "numero": "123",
    "complemento": "Apto 45",
    "bairro": "Centro",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01234-567"
  },
  "pagamentoId": 42,
  "dataCriacao": "2025-10-26T14:30:00",
  "dataAtualizacao": "2025-10-26T14:30:00"
}
```

## Estados do Pedido

O ciclo de vida de um pedido segue a seguinte máquina de estados:

```
PENDENTE → AGUARDANDO_PAGAMENTO → PAGO → EM_PREPARACAO → PRONTO → ENTREGUE
                                    ↓
                                 CANCELADO
```

### Transições Permitidas

- `PENDENTE` → `AGUARDANDO_PAGAMENTO`: Quando pedido é criado
- `AGUARDANDO_PAGAMENTO` → `PAGO`: Quando pagamento é confirmado
- `AGUARDANDO_PAGAMENTO` → `CANCELADO`: Quando pagamento falha ou expira
- `PAGO` → `EM_PREPARACAO`: Quando pedido entra na cozinha
- `EM_PREPARACAO` → `PRONTO`: Quando pedido fica pronto
- `PRONTO` → `ENTREGUE`: Quando pedido é entregue ao cliente
- Qualquer estado (exceto `ENTREGUE`) → `CANCELADO`: Cancelamento manual

## Validações de Negócio

### Regras Implementadas

1. **Pedido mínimo**: Valor total deve ser maior que R$ 10,00
2. **Quantidade de itens**: Cada item deve ter quantidade entre 1 e 99
3. **Produtos duplicados**: Não permitir produto duplicado no mesmo pedido
4. **CEP válido**: Validação de formato de CEP brasileiro
5. **Estado válido**: Apenas UFs brasileiras válidas
6. **Cancelamento**: Pedido só pode ser cancelado se não estiver ENTREGUE
7. **Atualização de status**: Transições devem seguir o fluxo permitido

### Exemplo de Value Object

```java
public class Cep {
    
    private final String valor;
    
    public Cep(String valor) {
        validar(valor);
        this.valor = limpar(valor);
    }
    
    private void validar(String cep) {
        if (cep == null || !cep.matches("\\d{5}-?\\d{3}")) {
            throw new CepInvalidoException("CEP inválido");
        }
    }
    
    private String limpar(String cep) {
        return cep.replaceAll("-", "");
    }
    
    public String getValor() {
        return valor;
    }
}
```

## Tratamento de Exceções

### Exceções Customizadas

```java
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PedidoNaoEncontradoException extends RuntimeException {
    public PedidoNaoEncontradoException(Long id) {
        super("Pedido não encontrado com ID: " + id);
    }
}

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TransicaoStatusInvalidaException extends RuntimeException {
    public TransicaoStatusInvalidaException(StatusPedido atual, StatusPedido novo) {
        super(String.format(
            "Transição inválida de %s para %s", 
            atual, 
            novo
        ));
    }
}
```

### Handler Global

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(PedidoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> handlePedidoNaoEncontrado(
        PedidoNaoEncontradoException ex
    ) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErroResponse(ex.getMessage()));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroValidacaoResponse> handleValidationErrors(
        MethodArgumentNotValidException ex
    ) {
        Map<String, String> erros = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                FieldError::getDefaultMessage
            ));
            
        return ResponseEntity
            .badRequest()
            .body(new ErroValidacaoResponse(erros));
    }
}
```

## Testes

### Executar Testes Unitários

```bash
mvn test
```

### Executar Testes de Integração

```bash
mvn verify
```

### Cobertura de Testes

```bash
mvn jacoco:report
```

O relatório será gerado em `target/site/jacoco/index.html`

### Estrutura de Testes

```
src/test/java
├── controller         # Testes de controller (MockMvc)
├── service           # Testes de serviço (mockito)
├── repository        # Testes de repositório (DataJpaTest)
├── integration       # Testes de integração end-to-end
└── util              # Classes utilitárias para testes
```

## Boas Práticas Implementadas

### Código Limpo

- Métodos pequenos e focados (máximo 20 linhas)
- Nomes descritivos e auto-explicativos
- Ausência de comentários desnecessários (código auto-documentado)
- Injeção de dependências via construtor
- Uso de Value Objects para primitivos
- Imutabilidade sempre que possível

### Segurança

- Validação rigorosa de entrada
- Proteção contra SQL Injection (uso de JPA)
- Sanitização de dados sensíveis nos logs
- Princípio do menor privilégio

### Performance

- Lazy loading configurado adequadamente
- Índices de banco de dados nas colunas mais consultadas
- Paginação em listagens
- Cache de consultas frequentes (quando aplicável)
- Connection pool otimizado

### Resiliência

- Circuit breaker para chamadas ao serviço de Pagamentos
- Retry com backoff exponencial
- Timeouts configurados
- Fallbacks para operações críticas

## Configuração Avançada

### Circuit Breaker com Resilience4j

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>
```

```properties
resilience4j.circuitbreaker.instances.pagamentos.failure-rate-threshold=50
resilience4j.circuitbreaker.instances.pagamentos.wait-duration-in-open-state=30s
resilience4j.circuitbreaker.instances.pagamentos.sliding-window-size=10
```

### Logging Estruturado

```properties
logging.level.br.com.cybercooks.pedidos=DEBUG
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %logger{36} - %msg%n
```

## Monitoramento

### Spring Boot Actuator

Endpoints disponíveis:

```
GET /actuator/health       # Status de saúde
GET /actuator/metrics      # Métricas da aplicação
GET /actuator/info         # Informações da aplicação
```

### Métricas Customizadas

O serviço expõe métricas específicas:

- Total de pedidos criados
- Taxa de pedidos cancelados
- Tempo médio de processamento
- Erros de comunicação com Pagamentos

## Deploy

### Docker

```dockerfile
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/pedidos-*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Kubernetes

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: pedidos-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: pedidos
  template:
    metadata:
      labels:
        app: pedidos
    spec:
      containers:
      - name: pedidos
        image: cybercooks/pedidos:latest
        ports:
        - containerPort: 8082
        env:
        - name: EUREKA_SERVER_URL
          value: "http://eureka-server:8761/eureka/"
```

## Contribuindo

1. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
2. Siga os princípios SOLID e Object Calisthenics
3. Escreva testes para toda nova funcionalidade
4. Commit suas mudanças (`git commit -m 'feat: adiciona nova funcionalidade'`)
5. Push para a branch (`git push origin feature/nova-funcionalidade`)
6. Abra um Pull Request

### Convenção de Commits

Utilize [Conventional Commits](https://www.conventionalcommits.org/):

```
feat: adiciona endpoint de busca por status
fix: corrige validação de CEP
docs: atualiza documentação da API
refactor: melhora estrutura do serviço de pedidos
test: adiciona testes para validação de transição de status
perf: otimiza consulta de listagem de pedidos
```

### Code Review Checklist

- [ ] Código segue princípios SOLID
- [ ] Métodos têm no máximo um nível de indentação
- [ ] Não há uso de ELSE
- [ ] Primitivos estão encapsulados em Value Objects
- [ ] Classes têm no máximo duas variáveis de instância
- [ ] Todos os testes passam
- [ ] Cobertura de testes acima de 80%
- [ ] Documentação atualizada

## Troubleshooting

### Serviço não se registra no Eureka

**Problema**: Pedidos não aparece no dashboard do Eureka

**Solução**:
- Verifique se o Eureka Server está rodando
- Confirme a URL do Eureka em `application.properties`
- Verifique logs para erros de conexão
- Aguarde até 30 segundos para registro inicial

### Erro de comunicação com Pagamentos

**Problema**: FeignException ao chamar serviço de Pagamentos

**Solução**:
- Verifique se o serviço de Pagamentos está rodando
- Confirme se está registrado no Eureka
- Verifique configuração do circuit breaker
- Analise logs para detalhes do erro

### Erro de validação de CEP

**Problema**: CEP válido sendo rejeitado

**Solução**:
- Verifique o formato: deve ser 12345-678 ou 12345678
- Confirme a regex de validação
- Teste com CEPs conhecidos

## Roadmap

- [x] CRUD básico de pedidos
- [x] Integração com microsserviço de Pagamentos
- [x] Validações de negócio
- [x] Máquina de estados para status
- [ ] Integração com serviço de Notificações
- [ ] Cálculo automático de frete
- [ ] Integração com estoque
- [ ] Relatórios de pedidos
- [ ] Dashboard administrativo

## Recursos Adicionais

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Cloud Netflix](https://spring.io/projects/spring-cloud-netflix)
- [OpenFeign Documentation](https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Domain-Driven Design](https://martinfowler.com/bliki/DomainDrivenDesign.html)

## Autores

- **Bytecooks Team**

## Licença

MIT License

---

**Status do Projeto**: Em Desenvolvimento 🚧