# Architecture Lab

A hands-on software architecture laboratory focused on building and evolving a distributed system using modern backend technologies and production-oriented patterns.

The goal of this repository is not only to build microservices, but to understand the architectural decisions behind distributed systems: communication, consistency, resilience, observability, scalability, and infrastructure.

## Objectives

This laboratory is designed to explore and practice:

* Microservices architecture
* Domain-driven design principles
* Hexagonal / Ports and Adapters architecture
* Synchronous and asynchronous communication
* Event-driven architecture
* Apache Kafka
* Transactional Outbox Pattern
* Database per service
* Eventual consistency
* Idempotent consumers
* Retry and Dead Letter Topics
* Distributed tracing
* Application metrics
* Centralized observability
* Containerized infrastructure
* Production-oriented engineering practices

## Technology Stack

### Backend

* Java 25
* Spring Boot
* Spring Web
* Spring Data JPA
* PostgreSQL
* Flyway

### Messaging

* Apache Kafka

### Observability

* Spring Boot Actuator
* Micrometer
* Prometheus
* Grafana
* OpenTelemetry
* Jaeger

### Infrastructure

* Docker
* Docker Compose

## Architecture

The laboratory starts with two independent services:

```text
architecture-lab/
├── order-service/
├── payment-service/
├── infrastructure/
├── docker-compose.yml
└── README.md
```

### Order Service

Responsible for managing orders.

Its responsibilities will progressively include:

* Creating orders
* Persisting order data
* Publishing domain events
* Implementing the Transactional Outbox Pattern
* Producing Kafka events
* Exposing operational metrics and traces

### Payment Service

Responsible for processing payments associated with orders.

Its responsibilities will progressively include:

* Consuming order events
* Creating payments
* Preventing duplicated processing
* Handling retries and failures
* Dead Letter Topic processing
* Exposing operational metrics and traces

## High-Level Flow

The expected interaction between services will evolve toward the following architecture:

```text
Client
   |
   v
Order Service
   |
   | PostgreSQL transaction
   v
Order Database
   |
   v
Outbox
   |
   v
Kafka
   |
   v
Payment Service
   |
   v
Payment Database
```

The services should remain independently deployable and own their respective data.

## Architectural Principles

This project follows several principles commonly used in distributed systems.

### Database per Service

Each microservice owns its database.

Services should not directly query another service's database.

```text
order-service
    |
    +-- orders database

payment-service
    |
    +-- payments database
```

### Asynchronous Communication

Kafka will be used when immediate synchronous responses are not required.

For example:

```text
Order Created
      |
      v
orders.created
      |
      v
Payment Service
```

This reduces temporal coupling between services and allows the system to continue operating when consumers are temporarily unavailable.

### Eventual Consistency

Because services maintain independent data stores, distributed transactions across services are avoided.

Instead, the system relies on events and eventual consistency.

### Idempotency

Consumers must be able to safely receive the same event more than once.

Event processing will therefore be designed to detect previously processed events.

### Observability

The system will expose three primary observability signals:

```text
Metrics  -> Prometheus -> Grafana

Traces   -> OpenTelemetry -> Jaeger

Logs     -> Application / Infrastructure logs
```

These signals will help analyze system behavior across service boundaries.

## Learning Roadmap

The laboratory will be implemented incrementally.

```text
01. Project initialization
02. Order domain
03. Persistence
04. REST API
05. Database migrations
06. Payment service
07. Service communication
08. Kafka
09. Event-driven communication
10. Transactional Outbox Pattern
11. Idempotent consumers
12. Retry strategy
13. Dead Letter Topics
14. Metrics
15. Prometheus
16. Grafana
17. OpenTelemetry
18. Distributed tracing
19. Jaeger
20. Resilience and failure scenarios
21. Scalability experiments
```

Each step should introduce a specific architectural concept instead of adding complexity without a clear purpose.

## Repository Philosophy

This repository is intentionally developed incrementally.

Architecture decisions, experiments, failures, and improvements are part of the learning process.

The objective is not to create a production product, but to build a realistic environment for understanding how modern distributed systems behave.

## Requirements

The local environment will progressively require:

```text
Java 25
Docker
Docker Compose
Git
```

Additional tools may be introduced as the laboratory evolves.

## Running the Project

Execution instructions will be added as each service and infrastructure component is introduced.

Eventually, the complete infrastructure will be started with Docker Compose:

```bash
docker compose up -d
```

Individual Spring Boot services can be executed independently during development.

## Git Strategy

The repository uses `main` as its stable development history.

Changes should be committed incrementally so architectural evolution remains visible.

Example commits:

```text
chore: initialize architecture lab

feat: add order domain model

feat: add postgres persistence

feat: add order creation endpoint

feat: publish order created event

feat: implement transactional outbox

feat: consume order created events

feat: add idempotent event processing

feat: add kafka retry strategy

feat: add dead letter topic

feat: expose application metrics

feat: add distributed tracing
```

## Status

🚧 Work in progress.

This repository is continuously evolving as new architecture concepts and distributed systems patterns are explored.

