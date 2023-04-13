# AntiFraudSystem

<!-- TOC -->
* [AntiFraudSystem](#antifraudsystem)
  * [Description](#description)
    * [Transaction validation and feedbacks](#transaction-validation-and-feedbacks)
    * [Security](#security)
  * [Endpoints](#endpoints)
  * [Role model](#role-model)
  * [Build and run](#build-and-run)
<!-- TOC -->

## Description

Anti-Fraud System is a REST API service which helps to manage money transactions. Transactions passing through the
system receive a status that indicates whether the transaction passed the verification.

Verification is based on all previous transactions — the size of transactions, regions and IP addresses from which the
transaction was made.

### Transaction validation and feedbacks

Also, the system has feature to receive feedbacks on transaction — these feedbacks can
change the status of transaction and have influence on current transaction validation limits.

Feedback will be carried out manually by a SUPPORT specialist for completed transactions. Based on the feedback results,
the system will change the limits of fraud detection algorithms following the special rules. Take a look at the table
below that shows the logic of the feedback system:

| Transaction Feedback →<br/>Transaction Validity ↓ | ALLOWED                        | MANUAL_PROCESSING | PROHIBITED                     |
|---------------------------------------------------|--------------------------------|-------------------|--------------------------------|
| ALLOWED                                           | Exception                      | ↓ max ALLOWED     | ↓ max ALLOWED<br/>↓ max MANUAL |
| MANUAL_PROCESSING                                 | ↑ max ALLOWED                  | Exception         | ↓ max MANUAL                   |
| PROHIBITED                                        | ↑ max ALLOWED<br/>↑ max MANUAL | ↑ max MANUAL      | Exception                      |

### Security

This project uses a Spring Security framework for managing users and their permissions. Users can have only one role
which define users' permissions. And user with an Administrator role can manage other users' roles and their access
to the system.

---

This project is written in Java 17. It uses [Spring Boot](https://spring.io/projects/spring-boot#overview) framework,
[Spring Security](https://docs.spring.io/spring-security/reference/index.html) framework,
and [H2](https://www.h2database.com/html/main.html) as a database.
It also has [OpenAPI 3.0](https://swagger.io/specification/v3/) documentation.

I used [Postman](https://www.postman.com/) for testing web pages and REST API endpoints.

This project is a part
of [JetBrains Spring Security for Java Backend Developers course](https://hyperskill.org/tracks/38?category=2).

## Endpoints

This project has OpenAPI 3.0 documentation in .yaml and .html format which includes information about all api
endpoints — you can check it by going to server page by "**/api/doc**" path.

## Role model

In this table, you can see which role have access to which path.

|                                 | Anonymous | MERCHANT | ADMINISTRATOR | SUPPORT |
|---------------------------------|-----------|----------|---------------|---------|
| GET api/doc                     | +         | +        | +             | +       |
| POST /api/auth/user             | +         | +        | +             | +       |
| DELETE /api/auth/user           | -         | -        | +             | -       |
| GET /api/auth/list              | -         | -        | +             | +       |
| POST /api/auth/access           | -         | -        | +             | -       |
| PUT /api/auth/role              | -         | -        | +             | -       |
| POST /api/antifraud/transaction | -         | +        | -             | -       |
| /api/antifraud/suspicious-ip    | -         | -        | -             | +       |
| /api/antifraud/stolen card      | -         | -        | -             | +       |
| GET /api/antifraud/history      | -         | -        | -             | +       |
| PUT /api/antifraud/transaction  | -         | -        | -             | +       |

## Build and run

- Clone repo and go to project directory

```shell
git clone https://github.com/ex-neskoro/AntiFraudSystem.git 
```

- You can already start the app by command

```shell
./gradlew bootRun
```

- Or you can create .jar first

```shell
./gradlew bootJar
java -jar AntiFraudSystem/build/libs/AntiFraudSystem-1.0.jar
```
