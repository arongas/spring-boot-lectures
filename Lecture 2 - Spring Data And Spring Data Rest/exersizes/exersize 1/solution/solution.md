# Exercise Solution

## Setting up the project step by step

Use Spring Initialzr, to setup the project.  

## Dependencies

You need to add the following dependencies:

1. Web
2. Elasticsearch
3. JPA
4. REST
5. Postgresql
6. Lombok
7. Flyway

---

## Setup Intellij 

* Setup lombok annotation processing


* Open build.gradle and add mapstruct support

---

```groovy
plugins {
	id 'net.ltgt.apt' version '0.15'
    ...
    }

...
configurations {
	compileOnly {
		extendsFrom annotationProcessor
	}
}

....

dependencies {
...
	compileOnly 'org.projectlombok:lombok'
	compileOnly 'org.mapstruct:mapstruct-jdk8:1.3.0.Final'
	compileOnly group: 'org.mapstruct', name: 'mapstruct-processor', version: '1.3.0.Final'
	annotationProcessor 'org.projectlombok:lombok'
	annotationProcessor 'org.mapstruct:mapstruct-processor:1.3.0.Final'
	testAnnotationProcessor 'org.mapstruct:mapstruct-processor:1.3.0.Final'
    ...
}
```

---


* eate the domain object for Elastic Search and a different for JPA. Use different entity name and different package.


   * Use  the following annotations for elastic search

      ```java
      import org.springframework.data.annotation.Id;
      import org.springframework.data.elasticsearch.annotations.Document;
      @Document
      @Id
      ```

       and the following annotations for JPA

      ```java
      import javax.persistence.Entity;
      import javax.persistence.Id;
      import javax.persistence.Table;
      @Entity
      @Table
      ```

---

* Create the DTO/view object

   ```java
   package gr.rongasa.library.web.dto;
   
   import com.fasterxml.jackson.annotation.*;
   import lombok.*;
   
   @AllArgsConstructor@NoArgsConstructor
   @Getter
   @Setter
   @JsonIgnoreProperties(ignoreUnknown = true)
   public class ResourceDTO {
    ....
   }
   
   ```
---

* Create the spring data elastic search repository enabling database query access.

   At this point you will need to setup elasticsearch connection in application properties

* Create the spring data JPA repository enabling database query access.

   At this point you will need to setup connection with postgresql in application properties

* ***Note*** that you need to configure spring boot where to find JPA repositories and where to find Elastic search repositories.

   ```
   @Configuration
   @EnableJpaRepositories(value = "gr.rongasa.library.repository.jpa")
   @EnableElasticsearchRepositories (value = "gr.rongasa.library.repository.elasticsearch")
   public class SpringDataConfiguration {
   
   }
   ```

   ---

* Create the service which retrieves database entities, converts these to data transfer objects and provides these to the caller bean.

> You should use mapstruct here and also understand why this is useful in real life applications. Imagine the case where you needed to map differently the dto into JPAcompared to elasticsearch database.

* Create the Rest controller and provide the required rest interface.
* Start your application and use postman to troubleshoot possible issues. 

---

## Flyway 

- The best possible way of loading and maintaining the schema and the database data of a spring boot application is via tools like LiquidBase and Flyway.
- Spring Boot has a very good support of these tools

```yaml
spring:
  flyway:
    locations: classpath:db/migration
```

```sql
-- resources\db\migration\V1_init.sql 
CREATE TABLE jpa_resource
(
    tracking_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    author character varying(255) COLLATE pg_catalog."default",
    description character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    type character varying(255) COLLATE pg_catalog."default",
    url character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT jpa_resource_pkey PRIMARY KEY (tracking_id)
);

INSERT INTO jpa_resource(tracking_id, author, description, name, type, url) VALUES ('R-0001', 'PUBLIC AUTHOR', 'MY FIRST BOOK IN SPRING BOOT', 'SPRING BOOT', 'BOOK', 'http://mybook.com');
```