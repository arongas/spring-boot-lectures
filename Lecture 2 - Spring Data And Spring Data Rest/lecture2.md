# Spring Boot Introduction

## Spring Data framework provides

Spring Data: https://spring.io/projects/spring-data

Spring Data’s mission is to provide a familiar and consistent, Spring-based programming model for data access while still retaining the special traits of the underlying data store.

It makes it easy to use data access technologies, relational and non-relational databases, map-reduce frameworks, and cloud-based data services. 

### Features

- Dynamic query derivation from repository method names

- Support for transparent auditing (created, last changed)

- Possibility to integrate custom repository code

- Advanced integration with Spring MVC controllers

  ---

  

## Most common Spring Data projects

- [Spring Data JPA](https://spring.io/projects/spring-data-jpa) - Spring Data repository support for JPA.

- [Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb) - Spring based, object-document support and repositories for MongoDB.

- [Spring Data REST](https://spring.io/projects/spring-data-rest) - Exports Spring Data repositories as hypermedia-driven RESTful resources.

- [Spring Data Elasticsearch](https://spring.io/projects/spring-data-elasticsearch) - Spring Data module for Elasticsearch.

  

  # Spring Data JPA

## Features

- Ready made CRUD provided functionality
- Dynamic query derivation from repository method names
- Manual queries `@Query` annotated queries at bootstrap time
- Support for transparent auditing (created, last changed)
- Support for [Querydsl](http://www.querydsl.com/) predicates and thus type-safe JPA queries
- Pagination support, dynamic query execution, ability to integrate custom data access code
- 