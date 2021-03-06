# Spring Boot Lectures



## Prerequisites

1. IntelliJ IDEA
2. Java 8
3. ElasticSearch 
4. ActiveMQ
5. PostgreSQL
6. MongoDB
7. Postman 
8. GIT

## GIT

The presentations and code can be found at git: https://github.com/arongas/spring-boot-lectures.git 



Useful GIT commands:

* Download complete lecture material from GIT: 

`git clone https://github.com/arongas/spring-boot-lectures.git`



# Lectures

## Lecture 1 - Introduction to Spring boot

Purpose of this lecture is to get a first/quick introduction of what is Spring Boot and its features. 

Two small projects will be created during this course, via Spring Initializr.  The first project will be a hello word web application (eshop). Within this project the basic spring boot capabilities will be discussed and explained.

The second project will be made slightly more complex by connecting with Elasticsearch as a database and exposing a domain object over rest (inventory). The purpose here will not be to understand spring data or spring data rest but introduce a proposed project structure and explain further auto configuration and spring boot property files.

By the end of the lecture audience is expected to be able to create it own project understand the structure of spring boot and be able to run it.

Lecture 1 also has 1 exercise which should be done after the completion of 1st lecture.  In this exercise, audience will also be introduced with the proper way of mapping DTO to database entities using MapStruct.

---

## Lecture 2 - Spring Data And Spring Data Rest

Purpose of this lecture is to introduce user with Spring Data and Spring Data Rest dependencies/functionality.  User will be introduced with the means to connect with JPA (postgresql) and with NoSQL (MongoDB) database. A set of queries will be used/exposed here via spring repositories. As a next step, audience will be introduced with the concept of Spring Data Rest.

By the end of the lecture audience is expected to have a good understanding of Spring Data and Spring Data Rest. 

Lecture 2 also has one exercise which should be done after the completion of lecture 2. In this exercise, audience will be introduced with the proposed/proper way of creating and updating application's schema and initial data.

---

## Lecture 3 - Web, Logging, Properties, Profiles,  and Actuator Endpoints

Purpose of this lecture is to introduce User with the capabilities of Spring Boot of handling MVC web application development with the help of Thymeleaf. Being aware that most probably Spring Boot applications will be Restfull web applications serving Vue or Angular frontend, this lecture acts as an introduction and as a stepping Stone for Lecture 4.  Apart from Spring boot web, in this lecture logging spring boot property files and actuator endpoints features are explained.

---

## Lecture 4 - Web

Purpose of this lecture is to provide information on how a simple Rest interface can be exposed and how rest communication between two different services can also be accomplished. Also scheduling inside a spring boot application is presented.

---

## Lecture 5 - Sprint Security

In this lecture, spring security is explained. Audience is provided with the information of how to secure a web application using users which are stored into a database. The difference between authentication and authorization is explained. Spring security framework is connected with a JPA database to provide user management. Finally Http security and web security filters configuration is examined.

---

## Lecture 6 - Spring Data JMS

This lecture introduces how JMS messages can be sent and received using activemq broker.  

---

## Lecture 7 - Unit Testing

This lecture explains how one can unit test spring boot applications