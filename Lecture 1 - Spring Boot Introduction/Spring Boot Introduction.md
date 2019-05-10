# Spring Boot Introduction



## Spring projects

| Projects               | Description                                                  |
| :--------------------- | ------------------------------------------------------------ |
| Spring Framework       | Core support for dependency injection, transaction management, web application development, data access, messaging, etc<br /> |
| Spring Boot            | Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run". It is build on top of Spring framework<br /> |
| Spring Data            | Provides a consistent approach to database access<br />      |
| Spring Cloud           | Spring Cloud provides tools to quickly build some of the common patterns in distributed systems<br /> |
| Spring Integration     | Extends the Spring programming model to support the well-known Enterprise Integration Patterns<br /> |
| Spring Cloud Data Flow | Spring Cloud Data Flow is a toolkit for building data integration and real-time data processing pipelines.<br /> |

---


| Projects        | Description                                                  |
| :-------------- | ------------------------------------------------------------ |
| Spring Batch    | Simplifies and optimizes the work of accessing, processing and exporting high volume batch data |
| Spring Security | Provides the means of application authentication and authorization. |
| Spring HATEOAS  | Provides the support of creating rest interface that follows the HATEOAS principle |


* Others like Spring Shell, Spring Session, Spring LDAP, Spring AMQP, Spring Rest Docs, Spring Kafka, etc

* Nothing of the above works alone but everything is connected. 

  * Spring Boot uses Spring core.

  * Spring Boot uses Spring Data or Spring LDAP or Spring Security or ..

  * Spring Cloud uses Spring Boot

  * Spring cloud data flow uses Spring boot and Spring cloud

    

---

## Spring Boot significance

* Auto Configuration 

* Dependency management using spring boot starters

* Rich feature set

* Integration with 3rd party libraries

* Community support

* Production ready applications

  * logging
  * actuator
  * embedded server
  
  

> All these make Spring boot, something that enables developers focus on business logic of the application and not  on setting up the project or usage of complex frameworks

---

## Spring Initializr

- Quickly setup a project

![](assets\initializr.png)

---

## Spring Boot Starters

- Starters contain the dependencies needed by including all consistent, supported dependencies.
- All starters follow a similar naming pattern; spring-boot-starter-*, where * is a particular type of application. 
- All starters start from spring-boot-starter-parent
  - Dependency Management - Version of dependencies
  - Default Plugin Configuration
- Increase dependency manageability
- Production-ready, tested & supported dependency configurations
- Decrease the overall configuration time for the project
- More easy version upgrade

---

### Common Spring Boot Starters 

- Spring starter Web
- Spring Starter Rest
- Spring Starter Security
- Spring Starter Data JPA
- Spring Starter Data Rest
- Spring Starter Actuator
- Spring Starter Logging

## Spring Boot Auto Configuration

- Spring Boot auto-configuration attempts to automatically configure your Spring application based on the jar dependencies that you have added. 
- Auto-configuration is non-invasive. At any point, you can start to define your own configuration to replace specific parts of the auto-configuration.
- Auto Configuration uses sensible defaults 
- Auto Configuration uses user configuration to override defaults

---

## Spring Boot Embedded Servers

- By default, spring boot creates an executable jar file using an embedded application server.
- Server configuration is applicable via spring application properties
  - Server port
  - context path
  - ssl and cypher configuration
  - Multiple connectors

- For servlet stack applications, spring-boot-starter-web includes Tomcat by default, but you can use jetty or undertow instead.
- For reactive stack applications, the spring-boot-starter-webflux includes Reactor Netty by default but you can use tomcat, jetty, or undertow instead.
- You can finally create a war and run a spring boot application inside another application server.

---

## Spring Boot annotations

| Annotation               | Description                                                  |
| ------------------------ | ------------------------------------------------------------ |
| @SpringBootApplication   | Marks the main class of the application. Encapsulates the following annotations.<br />@EnableAutoConfiguration, @Configuration and @ComponentScan<br /> |
| @EnableAutoConfiguration | Enables that spring boot will look for auto configuration beans on its classpath<br /> |
| @Configuration           | Spring Configuration annotation indicates that the class has @Bean definition methods. Spring will instantiate these beans  and use them in dependency injection.<br /> |
| @ComponentScan           | Enables component scan. Spring will scan for annotated classes<br /> |

---

| Annotation                                                   | Description                                                  |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| @Conditional                                                 | All annotations below derive from this one. It can be used to create own custom conditions.<br />We can place the annotations in this section on *@Configuration* classes or *@Bean* methods and indicate that only when condition is met the bean or the configuration class will be used.<br /> |
| @ConditionalOnClass  <br />and @ConditionalOnMissingClass<br /> | Conditions referring to the existence or not of a class in the class path |
| @ConditionalOnBean <br />and @ConditionalOnMissingBean<br /> | Conditions referring to the existence or not of a bean       |

---

| Annotation              | Description                                                                           |
| ----------------------- | ------------------------------------------------------------------------------------- |
| @ConditionalOnProperty<br /> | Conditions referring to the existence or not of a property                            |
| @ConditionalOnResource<br /> | Conditions referring to the existence or not of a resource on the classpath i.e. file |
| @ConditionalExpression<br /> | Conditions using SpeL expressions |

----

## Spring Boot Application Example

- Start a project with Spring initilizr (<https://start.spring.io/>)
- Use:
  - Java 8
  - Spring boot 2.1
  - Gradle
  - Add as single dependency the Web
- Import to Intellij

---

### Gradle

```groovy
plugins {
	id 'org.springframework.boot' version '2.1.4.RELEASE'
	id 'java'
}

apply plugin: 'io.spring.dependency-management'

group = 'gr.rongasa'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '1.8'

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-web'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

```

---

### Auto Configuration

* Notice how few are the dependencies
* Spring boot plugin is:
  * Package executable jar or war archives
  * Run Spring Boot applications
  * Use the dependency management provided by `spring-boot-dependencies`
* Since we added Web, all required libraries for a simple web application are added.


- With web mvc jars inside the application context, auto configuration configures the application as a web mvc application
  No need for web.xml or servlet.xml 

  Key behaviors such as setting up a DispatcherServlet are automatically activated.

  

  > This magic happens inside WebMvcAutoConfiguration class

---

## Rest Controller

```java
package gr.rongasa.inventory.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingsController {


    @GetMapping
    public ResponseEntity<String> greetings(){
        return ResponseEntity.ok("Hello Spring boot!");
    }
}

```

---

| Controller Annotation |                   Description                                           |
| --------------------- | ------------------------------------------------------------ |
| @Controller           | Spring annotation denoting that this class serves web interface |
| @RestController       | Introduced in Spring 4.0 to simplify the creation of RESTful web services. It’s a convenience annotation that combines @Controller and @ResponseBody |
| @RequestMapping       | At controller/class level denotes the original path of the controller. At method level denotes the path, what this method consumes or produces along with the http method type. |
| @ResponseBody         | Automatic serialization of the return object into the HttpResponse |
| @GetMapping           | Convenient annotation for RequestMapping with method GET.    |
| @PostMapping          | Convenient annotation for RequestMapping with method POST.   |
| @PutMapping           | Convenient annotation for RequestMapping with method GET.    |
| @PathVariable         | Denotes a java method property as http path parameter        |
| @RequestParam         | Denotes a java method property as query parameter            |
| @RequestBody          | Denotes a java method property as request body               |

---

## Build and Run

- gradlew clean build
- java –jar build/libs/inventory-0.0.1-SNAPSHOT.jar

----

## Connection with Elastic Search and expose domain object via Rest Interface



### Gradle

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
repositories {	mavenCentral() }
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-actuator'
	implementation 'org.springframework.boot:spring-boot-starter-data-elasticsearch'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

---

- ​	implementation 'org.springframework.boot:spring-boot-starter-data-elasticsearch'

This adds everything needed for elastic search

- ​	implementation 'org.springframework.boot:spring-boot-starter-actuator'

This adds everything needed for web application monitoring

- Lombok

	configurations {
		compileOnly {
			extendsFrom annotationProcessor
		}
	}
	...
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'
This adds everything needed for lombok to run

---

> Note: In Intellij for lombok to run, you need to:
>
> 1. Add the Lombok plugin
> 2. Enable annotation processing in intelliJ Settings (Build, Execution, Deployment >Compiler > Annotation Processors)

---

### Project Structure

Spring does not have any special requirements for the structure of the project. You do!

Not keeping a clean structure will make code not easily maintainable since you will need to check in every class for it annotation to identify the class purpose. Propose to keep the following rules:

- Create a root package (i.e. gr.rongasa.eshop)
- Create a package where you keep all your auto configuration classes i.e. gr.rongasa.eshop.configuration
- Create a package where you keep all your domain model classes i.e. gr.rongasa.eshop.domain
- Create a package where you keep all your repository classes i.e. gr.rongasa.eshop.repository
  - Ensure your repository classes end with the word Repository

---

- Create a package where you keep all your service/business logic classes i.e. gr.rongasa.eshop.service
  - Ensure that for every service class you have also an interface 
- Create a package where you keep all your web classes i.e. gr.rongasa.eshop.web
- Create a package where you keep all your web rest interface i.e. gr.rongasa.eshop.web.rest
- Ensure that rest interface uses only services and services use only repository
- It is a good practice that domain objects are transformed to DTO objects inside the services
  - I tend to add these DTO classes either in gr.rongasa.eshop.web.model or in gr.rongasa.eshop.model
  - Propose the use of frameworks like mapstruct or Dozer for the conversion.

---

### Application properties

- Can be either either in yml format or in properties format
- YAML format is here to stay so lets learn using it
- Less typing
- YAML is more readable 
- YAML format can hold multiple profiles in a single file
- Config Server will favor .yml  over .properties (in spring cloud)

---

```yaml
spring:
  application:
    name: e-shop
  data:
    elasticsearch:
      cluster-nodes: localhost:9300
```

```properties
spring.application.name=eshop
spring.data.elasticsearch.cluster-nodes=localhost:9300
```

---

### Domain object

```java
...
@Document(indexName = "inventory")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Inventory {
    @Id
    private String id;
    private String name;
    private Long amount;
    private String description;
    private String type;
    private BigDecimal cost;
}
```

---

- Every domain object represents what will be written into the database
- Lombok is not needed, simply used to reduce written code
- Document and id annotations make this an elastic search  database entity

---

### Repository

- Spring data repository provides seamless access to database entities.
- ElasticsearchRepository comes from starter-elastic and provides CUD and pagination  methods to access Inventory
- One can create his own queries either by name convention or by Query annotation
- On top of spring data one can find spring data rest. 

> Note:  Will focus more in these but not just yet.

---

```java
package gr.rongasa.eshop.repository;

import gr.rongasa.eshop.domain.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface InventoryRepository extends ElasticsearchRepository<Inventory,String> {
    Page<Inventory> findAllByType(Pageable pageable, String type);
    Page<Inventory> findAllByName(Pageable pageable, String type);
}
```

---

### Service

```java
@RequiredArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    @Override
    public Page<Inventory> findAll(Pageable pageable) {
        return inventoryRepository.findAll(pageable);
    }
    @Override
    public Page<Inventory> findAllByName(Pageable pageable, String name) {
        return inventoryRepository.findAllByName(pageable, name);
    }
    @Override
    public Page<Inventory> findAllByType(Pageable pageable, String type) {
        return inventoryRepository.findAllByType(pageable, type);
    }
   ...
}
```
---

```java
@RequiredArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {
...
    @Override
    public Inventory save(Inventory entity) {
        return inventoryRepository.save(entity);
    }
    @Override
    public Optional<Inventory> findById(String id) {
        return inventoryRepository.findById(id);
    }
    @Override
    public Optional<Inventory> deleteById(String id) {
        return inventoryRepository.findById(id).map(inventory -> {
            inventoryRepository.delete(inventory);
            return Optional.of(inventory);
        }).orElseGet(Optional::empty);
    }
}
```

---

### Rest Controller


```java
package gr.rongasa.eshop.web.rest;

@RestController
@RequestMapping(value = "inventory", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class InventoryResource {
    private final InventoryService inventoryService;
    ...
}
```

---

```java

public class InventoryResource {
    private final InventoryService inventoryService;

    @GetMapping
    public Page<Inventory> findAll(Pageable pageable) {
        return inventoryService.findAll(pageable);
    }

    @GetMapping("name")
    public Page<Inventory> findAllByName(Pageable pageable, @RequestParam("name") String name) {
        return inventoryService.findAllByName(pageable,name);
    }

    @GetMapping("type")
    public Page<Inventory> findAllByType(Pageable pageable, @RequestParam("type") String type) {
        return inventoryService.findAllByType(pageable,type);
    }

  ...
}
```

---

```java

    ...
    @PostMapping
    public ResponseEntity<Inventory> save(@RequestBody Inventory entity) {
        return ResponseEntity.ok(inventoryService.save(entity));
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<Inventory> update(@PathVariable("id") String id, @RequestBody Inventory entity) {
        entity.setId(id);
        return ResponseEntity.ok(inventoryService.save(entity));
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<Inventory> findById(@PathVariable("id") String id) {
        return inventoryService.findById(id).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Inventory> deleteById(@PathVariable("id") String id) {
        return inventoryService.deleteById(id).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
}
```