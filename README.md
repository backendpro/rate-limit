
# Rate Limiter com Resilience4j, Prometheus e Grafana

Este projeto demonstra o uso de **Resilience4j RateLimiter** em uma aplicação Java com Spring Boot, expondo métricas via **Micrometer** e monitorando com **Prometheus + Grafana**.

---

## 🚀 Tecnologias Utilizadas

- Java 21
- Spring Boot
- Resilience4j
- Micrometer
- Prometheus
- Grafana
- Docker Compose

---

## 📦 Executando o projeto com observabilidade

### 1. Suba Prometheus + Grafana via Docker

```bash
docker-compose up -d
```

Isso iniciará:

- Prometheus em `http://localhost:9090`
- Grafana em `http://localhost:3000` (login padrão: admin / admin)

---

### 2. Execute a aplicação Spring Boot

```bash
./gradlew bootRun
```

Certifique-se de que a aplicação está rodando em `http://localhost:8080`.

---

### 3. Dispare requisições para ativar o Rate Limiter

```bash
for i in {1..50}; do
  curl -s -X POST http://localhost:8080/v1 \
    -H "Content-Type: application/json" \
    -d "{\"identifiers\": [\"item$i\"]}" &
done
wait
```

---

### 4. Visualize no Grafana

#### 📊 Painéis recomendados:

| Métrica Prometheus                                   | Descrição                                      |
|------------------------------------------------------|------------------------------------------------|
| `resilience4j_ratelimiter_available_permissions`     | Permissões disponíveis no RateLimiter         |
| `resilience4j_ratelimiter_waiting_threads`           | Quantidade de requisições aguardando permissão|

---

### 5. Criar Dashboard no Grafana

1. Vá até **http://localhost:3000**
2. Menu lateral → Dashboards → Import ou New → Add an empty panel
3. Adicione os painéis com as métricas:
    - `resilience4j_ratelimiter_available_permissions{name="rateLimiter"}`
    - `resilience4j_ratelimiter_waiting_threads{name="rateLimiter"}`
4. Selecione Prometheus como fonte de dados
5. Salve o dashboard com um nome descritivo, como "RateLimiter"

---

## 🧪 Endpoint de teste

```
POST /v1
Content-Type: application/json

{
  "identifiers": ["id1", "id2", "id3"]
}
```

Esse endpoint simula o processamento paralelo de identificadores, respeitando o limite configurado.

---

## 📈 Configuração do Rate Limiter

```java
.limitForPeriod(5)
.limitRefreshPeriod(Duration.ofSeconds(1))
        .timeoutDuration(Duration.ofMillis(500))
```

- Até 5 requisições por segundo
- Aguarda no máximo 500ms por permissão

---

## 🧰 Créditos e Referências

- [Resilience4j Docs](https://resilience4j.readme.io/)
- [Micrometer](https://micrometer.io/)
- [Prometheus](https://prometheus.io/)
- [Grafana](https://grafana.com/)
