Soccer League Manager

A full-stack desktop application to manage soccer tournaments. Built using Spring Boot for backend logic and JavaFX for the desktop GUI. Supports player, referee, and team registration, with championship creation and match tracking.

---

Tech Stack

- Java
- Spring Boot (REST)
- JavaFX (UI)
- PostgreSQL
- Maven, Docker

---

Features

- Register teams, players, referees
- Create and manage championships
- Set match scores and track standings
- Data stored in PostgreSQL via ORM mapping
- System design includes:
  - Relational DB planning
  - Use-case diagram
  - Class diagram
  - Domain model
- Built with Maven and deployed using Docker

---

How to Run

1. Clone the repo:

```bash
git clone https://github.com/GuilhermeMD10/springboot-tournament-manager.git
cd soccer-league-manager
```

2. Start PostgreSQL via Docker:

```bash
docker-compose up --build
```

Run the JavaFX GUI from your IDE or command line.
```bash
mvn clean javafx:run
```

> DB credentials and connection details can be configured in `application.properties`.

---

Folder Structure

```
/JavaFX     #gui
/SoccerNow  #backend 
/docs     #uml diagrams and Demo
pom.xml
```

---

UML Diagrams

- Use-case diagram
- Class diagram
- Domain model

Located in `/docs/`

---

Demo

[Watch Video Demo] In /docs/

---
