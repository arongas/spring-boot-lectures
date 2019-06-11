# Spring Data and Spring Data Rest

## Spring Data framework 

- Spring Data: https://spring.io/projects/spring-data

- Spring Data’s mission is to provide a familiar and consistent, spring-based programming model for data access while still retaining the special traits of the underlying data store.

- It makes it easy to use data access technologies, relational and non-relational databases, map-reduce frameworks, and cloud-based data services. 

---

### Features

- Dynamic query derivation from repository method names

- Support for transparent auditing (created, last changed)

- Data Pagination

---


## Most common Spring Data projects

- [Spring Data JPA](https://spring.io/projects/spring-data-jpa) - Spring Data repository support for JPA.

- [Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb) - Spring based, object-document support and repositories for MongoDB.

- [Spring Data REST](https://spring.io/projects/spring-data-rest) - Exports Spring Data repositories as hypermedia-driven RESTful resources.

- [Spring Data Elasticsearch](https://spring.io/projects/spring-data-elasticsearch) - Spring Data module for Elasticsearch.

---

# Spring Data JPA

## Features

- Ready made CRUD provided functionality
- Dynamic query derivation from repository method names
- Manual queries `@Query` annotated queries
- Support for transparent auditing (created, last changed)
- Support for [Querydsl](http://www.querydsl.com/) predicates and thus type-safe JPA queries
- Pagination support, dynamic query execution, ability to integrate custom data access code

  

---

## Development time

> Start from eshop code of first lecture. Alternatively one may wish to start from spring initilizr as an exercise. 
>
> At any case please add  `JPA`, `Postgresql`, `Web` and `Lombok`.
>
> Next slides focus more at changes compared to the application of the eshop application of the previous lecture.

**The build.gradle ends up as follows**:

---

```groovy
plugins {
	id 'org.springframework.boot' version '2.1.4.RELEASE'
	id 'java'
}
apply plugin: 'io.spring.dependency-management'
group = 'gr.rongasa'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '1.8'
configurations {
	compileOnly {
		extendsFrom annotationProcessor
	}
}
repositories {
	mavenCentral()
}
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-actuator'
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.postgresql:postgresql'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

---

## Application Configuration (Postgresql Connection and JPA)

```yaml
server:
  port: 9090 # Set the server's port
  servlet:
    context-path: /jpa  # Set context path of application server
        ...
```

---

```yaml
...
spring:
  application:
    name: e-shop  # Set the application name.
  data:
    jpa:
      repositories:
        enabled: true
  datasource:  # Setup datasource
    hikari:
      connection-timeout: 20000
      maximum-pool-size: 5
    url: jdbc:postgresql://localhost:5432/eshop
    username: eshop
    password: eshop@@@
  jpa:   #JPA properties
    hibernate:
      ddl-auto: update
    show-sql: true
    database: postgresql
    database-platform: org.hibernate.dialect.PostgreSQL95Dialect
    open-in-view: false   # Significant attribute. Dont open transaction at controller entry point
    properties:
      hibernate:
        jdbc:
          lob:
            non_contextual_creation: true
```

---

## The Domain object

```java
package gr.rongasa.eshop.domain;
import lombok.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Inventory {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Long amount;
    private String description;
    private String type;
    private BigDecimal cost;
}
```

---

## Repository

```java
import gr.rongasa.eshop.domain.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    Page<Inventory> findAllByType(Pageable pageable, String type);
    Page<Inventory> findAllByName(Pageable pageable, String type);
}
```

---

## Database Configuration

```java
package gr.rongasa.eshop.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"gr.rongasa.eshop.repository"})
@Slf4j
public class DatabaseConfiguration {

    @Bean
    public CommandLineRunner stupidBean(){
        return args -> log.info("JPA java code started");
    }
}
```

## Result

- The conversion from Spring Data elasticSearch to spring data JPA is done. This is what is meant with familiar and **consistent** model for data layer access. 
  Consider how few changes were made to move from two completely different database technologies.
- We could allow the concurrent existence  of elasticSearch and JPA connection
- We changed domain id from String to Long. Elastic search performs better/easier with String id while 
- Be careful with the @Id and from which path this is imported from. `javax.persistence.Id;` is for JPA and `org.springframework.data.annotation.Id;` is for non relational databases.

---

## Means of Database Queries

The repository proxy has two ways to derive a store-specific query from the method name:

  - By deriving the query from the method name directly.
  - By using a manually defined query.

---

## Query from method name

- 
  findFirst/ findTop

- findAllBy or findBy

- distinct

- existsBy

- delete

- count

- orderBy

  - asc, desc

  


e.g.

```java
Page<Inventory> findAllByType(Pageable pageable, String type);
Page<Inventory> findAllByName(Pageable pageable, String type);
void deleteByType(String type);
boolean existsByType(String type);
long countByType(String type);
```
----

  ## Supported method Names keywords

  

| Keyword    | Keyword          | Keyword           | Keyword |
| ---------- | ---------------- | ----------------- | -------|
| And        | LessThan         | After             | Like   |
| Or         | LessThanEqual    | Before            | NotLike |
| Is, Equals | GreaterThan      | IsNull            | StartingWith |
| Between    | GreaterThanEqual | IsNotNull,NotNull | EndingWith   |
| Containing | OrderBy          | Not               | In         |
| NotIn      | True             | False             | IgnoreCase   |



```java

    Page<Inventory> findByTypeAndAmountGreaterThanOrderByCost(Pageable p, String type, Long amount);
    Optional<Inventory> findFirstByNameAndTypeOrderByCost(String name, String type);
    Page<Inventory> findDistinctTop2ByAmountLessThanOrderByAmountAsc(Pageable pageable, Long amount);
    Page<Inventory> findDistinctTop2ByAmountLessThanOrderByAmountDesc(Pageable pageable, Long amount);
    Page<Inventory> findByNameContaining(Pageable pageable, String name);
    Page<Inventory> findByNameEndingWith(Pageable pageable, String name);
    Page<Inventory> findByAmountIsBetween(Pageable pageable, Long min, Long max);
```

---

## Query 

JPQL or native queries are supported in Spring data repositories

e.g.

```java
@Query("select i from Inventory i where i.description=?1")
Inventory getInventory(String details);

@Query(value = "select * from Inventory where i.description=?1", nativeQuery = true)
Inventory getInventoryByNativeQuery(String details);

@Query("select i from Inventory i where i.description=descr")
Inventory getInventoryByNamedParam(@Param("descr") String description);

```

---

### Query 

Propose to avoid Queries and even more native queries. However, although spring data provides practically almost everything, sometimes JPA query may be the only way.

* Hard to move from one database engine to another

* Less readable

* More difficult to detect issues before running the application

One reason that query may be needed is when we wish to query something like the following 
`(entity.x=:a OR entity.x=:b) AND (entity.y=:c OR entity.y=:d)`

---

## Entity Graph

- *ToMany relationships are lazy fetch. This means you cannot get i.e. the members without a transaction

- Repository methods open a transaction inside method call and close it after method exit.

- EntityGraph is a performant way of eagerly fetching inner entities after repository method returns

  

```java
@Entity
public class GroupInfo {

  // default fetch mode is lazy.
  @ManyToMany
  List<GroupMember> members = new ArrayList<GroupMember>();
  …
}
```



```java
@Repository
public interface GroupRepository extends CrudRepository<GroupInfo, String> {

  @EntityGraph(attributePaths = { "members" })
  GroupInfo getByGroupName(String name);
}
```

---

## Named Entity Graph

Instead of constructing and using EntityGraph inside repository one create named entity graphs and then use these inside the repository

```java
@Entity
@NamedEntityGraph(
  name = "members-graph",
  attributeNodes = {
    @NamedAttributeNode("members")
  }
)
public class GroupInfo {
  // default fetch mode is lazy.
  @ManyToMany
  List<GroupMember> members = new ArrayList<GroupMember>();
  …
}
```

```java
@Repository
public interface GroupRepository extends CrudRepository<GroupInfo, String> {
  @EntityGraph(value = "members-graph")
  GroupInfo getByGroupName(String name);
}
```

---

## Common Mistakes/Improvements

JPA prerequisite information

- `@OneToOne`: Eager fetch
- `@OneToMany`: Lazy fetch
- Typically relationships are bidirectional (when possibly make them unidirectional).

Attention:

- Bidirectional relationships during update and submit and first level cache. One needs to update both directions to avoid problems.
- Take special care with equals, hashCode and toString overrides at entity level when having relationships (**especially now with lombok**)
  - Danger of stack overflow
  - Danger of performance issues simply when adding just logging
- private methods annotated with transaction will not cope into opening a transaction.

---

## Common Mistakes/Improvements

  

- Use transactions at Service level at most Avoid opening transaction at controller.
  
- Mapping of database entities into data transfer objects is a good way of controlling transactions 
  
  - Only get from entity what is needed
  - Set into entity before saving into database bidirectional relationships
  
- Usage of EntityGraph a good way of keeping transaction as small as possible

- When using relational database utilize database versioning and migration tools like Liquidbase or Flyway

  - Don't skip this step as migrating from one version to another will be problematic without these tools and also it is harder to introduce these tools at a latter stage.

---

# Spring Data Rest

---

Spring Data Rest is an 'extension' of Spring Data that exposes over REST the spring data methods that exist in spring data repositories.

> - Impressive at first look and useful for rapid development results.
> - Use it only for testing, development prototype or only when product is really simple
>   - If used in bigger products you bind domain model with rest consumer. Architecturally wise this is huge mistake that sooner or latter it will add huge complexity.
>   - Spring data rest is the best demonstration of HATEOAS.
>

---

## Spring Data Rest

Add in gradle the following dependency

```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-rest'
```

Add in all custom repository methods that you wish to expose over rest and have method parameters the @Param annotation.

```java
public interface GroupInfoRepository extends JpaRepository<GroupInfo,Long> {
    @EntityGraph(value = "members")
    Page<GroupInfo> findAllByMembersName(@Param("name") String name, Pageable page);
}
```

Add the spring data rest path in application.yml

```yaml
data:
  rest:
    base-path: api
```

---

## Spring Data Rest - HATEOAS 

```cobol
  curl -X GET http://localhost:9090/jpa/api
```

Notice that spring data repositories are exposed over rest.

```json
{
    "_links": {
        "groupInfoes": {
            "href": "http://localhost:9090/jpa/api/groupInfoes{?page,size,sort}",
            "templated": true
        },
        "groupMembers": {
            "href": "http://localhost:9090/jpa/api/groupMembers{?page,size,sort}",
            "templated": true
        },
        "inventories": {
            "href": "http://localhost:9090/jpa/api/inventories{?page,size,sort}",
            "templated": true
        },
        "profile": {
            "href": "http://localhost:9090/jpa/api/profile"
        }
    }
}
```

---
## HATEOAS/HAL

> 
>
> - Spring data REST exposes all entities using HATEOAS.
> - HATEOAS is a REST API architecture concept which defines that REST resource models include hypermedia links in such a way that clients can navigate to the complete Rest Interface.
> - Clients may start from root context path and by following hateoas link names expose the complete API.

---

## Spring Data REST

```json
  "inventories": {
        "href": "http://localhost:9090/jpa/api/inventories{?page,size,sort}",
        "templated": true
    },
```
tempalated means that page, size, sort are part of query template. 

i.e. http://localhost:9090/jpa/api/inventories?page=1&size=10&sort=id,asc

---

```json
{
    "_embedded": { "inventories": [] },
    "_links": {
        "first": {
            "href": "http://localhost:9090/jpa/api/inventories?page=0&size=10&sort=id,asc"
        },
        "prev": {
            "href": "http://localhost:9090/jpa/api/inventories?page=0&size=10&sort=id,asc"
        },
        "self": {
            "href": "http://localhost:9090/jpa/api/inventories"
        },
        "last": {
            "href": "http://localhost:9090/jpa/api/inventories?page=0&size=10&sort=id,asc"
        },
        "profile": {
            "href": "http://localhost:9090/jpa/api/profile/inventories"
        },
        "search": {
            "href": "http://localhost:9090/jpa/api/inventories/search"
        }
    },
    "page": {
        "size": 10,
        "totalElements": 0,
        "totalPages": 0,
        "number": 1
    }
}
```

---

## Create new object

```javascript
curl -X POST http://localhost:9090/jpa/api/inventories -H 'Content-Type: application/json' 

  -d '{
	"name": "book",
    "amount": 10,
    "description": "custom rwritten book",
    "type": "book",
    "cost": 10.45
}'
```

---

Result

```json
{
    "name": "book",
    "amount": 10,
    "description": "custom rwritten book",
    "type": "book",
    "cost": 10.45,
    "_links": {
        "self": {
            "href": "http://localhost:9090/jpa/api/inventories/3"
        },
        "inventory": {
            "href": "http://localhost:9090/jpa/api/inventories/3"
        }
    }
}
```

## Update new Object

```javascript
curl -X PUT \
  http://localhost:9090/jpa/api/inventories/3 \
  -H 'Content-Type: application/json' \
  -d '{
	"name": "book",
    "amount": 10,
    "description": "custom rwritten book",
    "type": "book",
    "cost": 10.45
}'
```

---



```json
{
    "name": "book",
    "amount": 10,
    "description": "custom rwritten book",
    "type": "book",
    "cost": 10.45,
    "_links": {
        "self": {
            "href": "http://localhost:9090/jpa/api/inventories/3"
        },
        "inventory": {
            "href": "http://localhost:9090/jpa/api/inventories/3"
        }
    }
}
```

---

## Search Hypermedia Link

- Ensure you have added in spring data custom repository method `@Param` annotation at method parameters

- After creating an Inventory object locate hypermedia link named search and make a GET request.

  i.e. http://localhost:9090/jpa/api/inventories/search

  

  ---

  All custom methods created are exposed and can be used

```java
{
    "_links": {
        "updateMediaType": {
            "href": "http://localhost:9090/jpa/api/inventories/search/updateMediaType{?press,book}",
            "templated": true
        },
        "findByAmountIsBetween": {
            "href": "http://localhost:9090/jpa/api/inventories/search/findByAmountIsBetween{?page,size,sort}",
            "templated": true
        },
        "findByTypeAndAmountGreaterThanOrderByCost": {
            "href": "http://localhost:9090/jpa/api/inventories/search/findByTypeAndAmountGreaterThanOrderByCost{?page,size,sort}",
            "templated": true
        },
        ...
        "self": {
            "href": "http://localhost:9090/jpa/api/inventories/search"
        }
    }
}
```

---

# Spring Data REST Tips And Tricks

---

- You can annotate as `@RestResource(exported = false)` a Repository (method or class) to define it as not exposed over web
- You can configure Repositoriies (i.e. expose id) by using RepositoryRestConfigurer

```java
@Configuration
public class RestConfiguration {
    
    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer() {
        return new RepositoryRestConfigurer() {
            @Override
            public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
                config.exposeIdsFor(Inventory.class);
            }
        };
    }
}
```

---

- You can enable HAL Browser to assist with spring data rest experience:

```groovy
implementation 'org.springframework.data:spring-data-rest-hal-browser'
```

> Now open the Spring Data Rest base url via browser and check the result.
>
> A UI regarding exposed rest methods is provided.

# Spring Data Tips And Tricks

---

You can load initial data via CommandLineRunner

```java
@Component
@RequiredArgsConstructor
public class DatabaseDataInitialization implements CommandLineRunner {
    private final InventoryRepository inventoryRepository;

    @Override
    public void run(String... args)  {
        if (inventoryRepository.countByType("book")==0){
            inventoryRepository.saveAll(Stream.of("Spring Boot", "Spring Cloud").map(book->Inventory.builder().name(book).cost(BigDecimal.valueOf(150)).amount(100L).build())
                    .collect(Collectors.toList()));
        }
    }
}
```

# Spring Data MongoDB

---

- Lets switch this project now to NoSQL/MongoDB.
- Then we have seen the same project in ElasticSearch (1st lecture), in JPA (Postgresql) and MongoDB. 

---

### Prepare Dependencies

- In gradle remove 'org.springframework.boot:spring-boot-starter-data-jpa' and 'org.postgresql:postgresql'  from dependencies

- In gradle add 'org.springframework.boot:spring-boot-starter-data-mongodb' as dependency

  ### Prepare Domain model

- In Domain objects replace @Entity, @Table with @Document

- In Domain objects remove @GeneratedValue and @ManyToMany, @OneToOne

- In Domain objects replace id of type Long with String, BigInteger, or ObjectID

- Remove from Domain objects and repositories anything that has to do with EntityGraph (@EntityGraph and @NamedEntityGraph)

- Ensure @Id is from import org.springframework.data.annotation.Id

  

  ---

  ### Prepare Repositories

- In repositories replace JpaRepository interface with MongoRepository

- In repositories remove Modifying queries

- In Repositories native queries are not considered (all queries are native)

- In Repositories modify jpa queries to match mongodb query language 

- In database configuration replace EnableJpaRepositories with EnableMongoRepositories

  
---
# Exercise 1

Purpose: Gain experience with Spring Data and Spring Data Rest. 



## Description

You need to extend the simple Library Management System (exercise of first lecture) and add the same information added into elasticsearch into postgresql database.  This means that it has been decided to support two databases in parallel. 

- Postgresql will not be exposed over web using Spring Data Rest. 
- Elasticsearch database will be exposed over REST.
- One extra method supported over web will be the post method ''/synch' which will synchronize all data from Postgresql to elastic search.
- From elasticSearch repositories it has been decided to expose over rest all the get methods but hide/disable the database modifying methods and allow modification only via the existing controller.
- It is decided to use flyway as a migration tool for JPA database.