### AI Translation
The project provides an AI-powered translation service built using Spring Boot, Apache Kafka, and MongoDB, leveraging Ollama API for translation. The system is designed using microservices architecture with synchronous communication over Kafka.


## The project consists of two microservices

  1) API Service

  - Exposes a REST API (/api/translate) for translation requests.

  - Sends translation requests to Kafka topic (translation-requests).

  - Waits for the translated response and returns it synchronously.

  2) Worker Service

  - Listens to translation requests from Kafka.

  - Uses Ollama API and a language model for actual translation.

  - Stores translation history in MongoDB.

  - Sends the translation result back to Kafka (translation-responses).

##  Features
Microservices Architecture (API Service + Worker Service)

  - Synchronous Communication via Apache Kafka

  - AI-Powered Translation using Ollama API (LLM)

  - REST API for external integration

  - Translation Logging in MongoDB

  - Docker Compose for Kafka & Zookeeper setup



## Technologies Used
  - Java, Spring Boot

  - Apache Kafka

  - MongoDB

  - Docker, Docker Compose

  - Ollama API (LLM for Translation)

  - REST API

  - Maven
