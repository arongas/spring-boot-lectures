# RESTfull web services



## Lecture Description

- Rest fits better with modern frontend technologies like Angular.
- Server side has no concerns on the view part of the application.
- Internationalization is mostly (and actually not even there) limited to error handling
- This lecture will recap a lot of things we have learned already. It is based actually on two projects and multiple instances starting (very slowly) to mimic cloud development.



> A friend bought a set of smart sensors and installed these around the house. We wish to grab the sensor readings from time to time and collect/aggregate these into an 'aggregation' database. 
>
> In future these readings will be processed. For the time being we need to create the sensor's data reading application and the aggregation application which will collect and store metrics from the sensors.

---

## Sensor Reading Application (akka sensors)

A Spring boot application with simply a web interface will be created using spring initializer.

**Use the following dependencies :**

```yaml
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-actuator'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

**Set Properties:**

```properties
spring.application.name=Sensor
server.port=9090
```

---

**Create Metric Domain Object:**

```java
package gr.rongasa.sensor.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class MetricDTO {
    private UUID id;
    private String value;
    private Instant time;
    private String sensorId;
}
```

---

**Create the Rest Interface:**

```java
package gr.rongasa.sensor.web.rest;
import gr.rongasa.sensor.domain.MetricDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequiredArgsConstructor
public class MeasurementResource {
    @Value("${server.port}")
    String sensorId;

    @GetMapping
    public ResponseEntity<List<MetricDTO>> getMeasurements(HttpServletRequest request) {
        List<MetricDTO> metricDTOList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {            metricDTOList.add(MetricDTO.builder().id(UUID.randomUUID()).sensorId(sensorId).time(Instant.now()).value(ThreadLocalRandom.current().nextDouble() + "").build());
            try {Thread.sleep(1000);} catch (InterruptedException ignored) {}
        }
        return ResponseEntity.ok(metricDTOList);
    }
}
```

---

**Start 5 sensors at ports 9090-9095:**

- java -Dserver.port=9090 -jar sensor.jar
- java -Dserver.port=9091 -jar sensor.jar
- ...
- java -Dserver.port=9095 -jar sensor.jar

---

**Aggregator Service:**

- The Aggregator will store data into a relational database
- The Aggregator will have a list of monitored sensors and the rest interface to CRUD these sensors
- The Aggregator will retrieve and store over rest interface for every sensor every x seconds a list of metrics. 
- The Aggregator will have a rest interface to expose these metrics over REST.

----

**Domain Model:**

- DTO and JPA entities are defined.

- Unidirectional relationship between metric and sensor is defined.

- Validation and JSON decisions are made in the DTO

- Database decisions are made in the entity object level

- DTO in this 'microservice' is not exchanged as code BUT as rest interface.

  

> Exchanging java code in micro-services is relatively bad idea but even worse idea is to create common libraries.



---
**Create the Domain Model (DTO) -Sensor-:**

```java
package gr.rongasa.agregator.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorDTO {
    @NotBlank
    String sensorId;
    @NotBlank
    String name;
    @NotBlank
    String location;
    String description;
}
```

---
**Create the Domain Model (DTO) -Metric-:**

```java
package gr.rongasa.agregator.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetricDTO {
    private UUID id;
    private String value;
    private Instant time;
    private String sensorId;
}
```

---
**Create the Domain Model (Entity-Metric):**

```java
package gr.rongasa.agregator.domain.jpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Metric {
    @Id
    private UUID id;
    private String value;
    private Instant time;
    @ManyToOne(optional = false)
    @JoinColumn(name="sensor_id")
    private Sensor sensor;
}
```

---
**Create the Domain Model (Entity/Sensor):**

```java
package gr.rongasa.agregator.domain.jpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Sensor {
    @Id
    String sensorId;
    String name;
    String location;
    String description;
}
```

---
**Create the Spring Data Repositories**

```java
package gr.rongasa.agregator.repository;

import gr.rongasa.agregator.domain.jpa.Metric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MetricRepository extends JpaRepository<Metric, UUID> {
    Page<Metric> findBySensorSensorId(Pageable page, String sensorId);
}


```
```java
package gr.rongasa.agregator.repository;

import gr.rongasa.agregator.domain.jpa.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, String> {
}
```

---

**Map Entity to DTO and vise versa**:

- Mapstruct mapper can commonly be interface or abstract classes. Interface in simply classes abstract when after-mapping actions are needed.
- Hard to configure originally but it pays off in clean code, agility and ease of adaptation
- Mapper

```java
  @Mapper(componentModel = "spring", uses = {SensorRepository.class})
```
  - `componentModel` Should be `spring` to be able to autowire.
  - `uses` enables to specify other mappers (or spring beans) the given mapper needs to use 
  - Methods:

```java
  Sensor sensorDTOToSensor(SensorDTO sensorDTO);
  SensorDTO sensorToSensorDTO(Sensor sensor);
```

*Don't forget to add the mapstruct plugin*

---

**Map Entity to DTO and vise versa**:


- Simply defining the methods is as if creating the methods is enough. 
  Only mapping exceptions need to be defined

-   Define what will be ignored and how to map things together

```java
  @Mapping(target = "sensor", ignore = true)
  @Mapping(target = "sensorId", source = "sensor.sensorId")
```

- Connect hibernate relationships together or any other special logic needed. 
```java
  @AfterMapping
  void addBackReference(@MappingTarget Metric metric, MetricDTO metricDTO) {...}
```

- Examples and documentation are helpful since using mapstruct is not trivial exercise but over time pays off.
  
  https://github.com/mapstruct/mapstruct-examples
---

**Map Entity to DTO and vise versa:**

```java
package gr.rongasa.agregator.service.mapper;

import gr.rongasa.agregator.domain.dto.SensorDTO;
import gr.rongasa.agregator.domain.jpa.Sensor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SensorMapper {

    Sensor sensorDTOToSensor(SensorDTO sensorDTO);

    SensorDTO sensorToSensorDTO(Sensor sensor);

}
```

---

**Map Entity to DTO and vise versa:**

```java
package gr.rongasa.agregator.service.mapper;

import gr.rongasa.agregator.domain.dto.MetricDTO;
import gr.rongasa.agregator.domain.jpa.Metric;
import gr.rongasa.agregator.repository.SensorRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Objects;

@Mapper(componentModel = "spring", uses = {SensorRepository.class})
public abstract class MetricMapper {
    @Autowired
    private SensorRepository sensorRepository;
    @Mapping(target = "sensor", ignore = true)
    public abstract Metric metricDTOToMetric(MetricDTO metricDTO);

    @Mapping(target = "sensorId", source = "sensor.sensorId")
    public abstract MetricDTO metricToMetricDTO(Metric metric);

    @AfterMapping
    void addBackReference(@MappingTarget Metric metric, MetricDTO metricDTO) {
        if (!Objects.isNull(metricDTO.getSensorId())) {            sensorRepository.findById(metricDTO.getSensorId()).ifPresent(metric::setSensor);
        }
    }
}
```

---
**Specify Property configuration:**

- Dynamic Environment values and OS or Docker

- Dynamic values from command line

- Profiles

  

> In future in the picture cloud configuration server will also appear. For the time being avoid thinking about cloud/microservices

---

**Specify Property configuration (cont.):**

```yaml
server:
  port: 8080
spring:
  application:
    name: aggregator
  data:
    jpa:
      repositories:
        enabled: true
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    url: jdbc:postgresql://${POSTGRES_IP:127.0.0.1}:5432/${POSTGRES_DB:aggregator}
    username: ${POSTGRES_USER:aggregator}
    password: ${POSTGRES_PASSWORD:aggregator@@@}
    platform: POSTGRESQL
    driverClassName: org.postgresql.Driver
.....
```

---

**Specify Property configuration (cont.):**

```yaml
spring:
...
  jpa:
    hibernate:
      ddl-auto: validate
      naming:
        physical-strategy: org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy
        implicit-strategy: org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl
    database: postgresql
    database-platform: org.hibernate.dialect.PostgreSQL94Dialect
    open-in-view: false
    show-sql: false
    properties:
      hibernate.temp.use_jdbc_metadata_defaults: false
      hibernate.jdbc.lob.non_contextual_creation: false
  liquibase:
    change-log: classpath:migration/master.xml
```

---

**Specify Service layer:**

- What is the need of Service layer
- Interfaces, Dependency injection
- Mapstruct Repositories and Services: How these bind together
- Where transactional can be applied

---

**Specify Service layer (cont.):**

```java
....
@Service
@RequiredArgsConstructor
@Transactional
public class MetricServiceImpl implements MetricService {
    public final MetricRepository metricRepository;
    public final MetricMapper metricMapper;
    @Override
    public Page<MetricDTO> getMetrics(Pageable page) {
        return metricRepository.findAll(page).map(metricMapper::metricToMetricDTO);
    }
    @Override
    public Optional<MetricDTO> getMetric(UUID uuid) {
        return metricRepository.findById(uuid).map(metricMapper::metricToMetricDTO);
    }
    @Override
    public Page<MetricDTO> getMetricsBySensorId(Pageable page, String sensorId) {
        return metricRepository.findBySensorSensorId(page, sensorId).map(metricMapper::metricToMetricDTO);
    }
    @Override
    public MetricDTO save(MetricDTO metric) {
        return metricMapper.metricToMetricDTO(metricRepository.save(metricMapper.metricDTOToMetric(metric)));
    }

}
```

---

```java
@Service
@RequiredArgsConstructor
public class SensorServiceImpl implements SensorService {
    private final SensorRepository sensorRepository;
    private final SensorMapper sensorMapper;
    @Override
    public Page<SensorDTO> getSensors(Pageable page) {
        return sensorRepository.findAll(page).map(sensorMapper::sensorToSensorDTO);
    }
    @Override
    public Optional<SensorDTO> getSensor(String sensorId) {
        return sensorRepository.findById(sensorId).map(sensorMapper::sensorToSensorDTO);
    }
    @Override
    public SensorDTO saveSensor(SensorDTO sensor) throws SensorException {
        if (!sensorRepository.findById(sensor.getSensorId()).isPresent()) {
            return sensorMapper.sensorToSensorDTO(sensorRepository.save(sensorMapper.sensorDTOToSensor(sensor)));
        } else {
            throw new SensorException("Sensor already exists");
        }
    }
    @Override
    public SensorDTO updateSensor(String sensorId, SensorDTO sensor) throws SensorException {
        if (sensorRepository.findById(sensorId).isPresent()) {
            return sensorMapper.sensorToSensorDTO(sensorRepository.save(sensorMapper.sensorDTOToSensor(sensor)));
        } else { throw new SensorException("Sensor "+sensorId+" does not exist"); }
    }
}
```

---

**Specify Web Interface:**

- Separate controller per functionality

- Controllers should not have business logic and should be connected with service layer

- Use properly Rest methods and status code

- Use Validation 

- Use proper error handling

- Don't necessary use HATEOAS (compile group: 'org.springframework.boot', name: 'spring-boot-starter-hateoas') but consider the front end user.

  Use clean Rest Interface

> HATEOAS and Rest Docs cost in effort (like any other functionality). Use these when/if the rest interface developed is public or if there is enough capacity.
>
> This of course does not mean that proper Rest method usage and proper Rest interface design is not needed.

---

**Specify Web Interface (cont.):**

```java
package gr.rongasa.agregator.web.rest;
...
@RestController
@RequestMapping("/metric")
@RequiredArgsConstructor
public class MetricServiceResource {

    private final MetricService metricService;

    @GetMapping
    public ResponseEntity<Page<MetricDTO>> getMetrics(Pageable page) {
        return ResponseEntity.ok(metricService.getMetrics(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetricDTO> getMetric(@PathVariable("id") UUID uuid) {
        return metricService.getMetric(uuid).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("sensor/{id}")
    public ResponseEntity<Page<MetricDTO>> getMetrics(Pageable page, @PathVariable("id") String sensorId) {
        return ResponseEntity.ok(metricService.getMetricsBySensorId(page, sensorId));
    }
}
```

---

**Specify Web Interface (cont.):**

```java
package gr.rongasa.agregator.web.rest;
...

@RestController
@RequestMapping("/sensor")
@RequiredArgsConstructor
public class SensorServiceResource {
    private final SensorService sensorService;
    @GetMapping
    public ResponseEntity<Page<SensorDTO>> getSensors(Pageable page) {
        return ResponseEntity.ok(sensorService.getSensors(page));
    }
    @GetMapping("/{sensorId}")
    public ResponseEntity<SensorDTO> getSensor(@PathVariable("sensorId") String sensorId) {
        return sensorService.getSensor(sensorId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<SensorDTO> saveSensor(@RequestBody @Valid SensorDTO sensor) {
        return ResponseEntity.ok(sensorService.saveSensor(sensor));
    }
    @PutMapping("/{sensorId}")
    public ResponseEntity<SensorDTO> saveSensor(@PathVariable("sensorId") String sensorId, @Valid @RequestBody SensorDTO sensor) {
        return ResponseEntity.ok(sensorService.updateSensor(sensorId, sensor));
    }
}

```

---

**Rest Template:**

- Application needs to communicate over rest with Sensor application. 

- Create a Rest Template bean

- RestTemplateBuilder is autowired and can be used to configure rest template. i.e.

   `builder.basicAuthorization("username", "password").setConnectTimeout(Duration.ofMillis(30));`

  

```java
package gr.rongasa.agregator.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClient {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
```

---

**Enable Scheduling:**

- Create an Executor of a 10 threads
  - 10 can/should be configurable of application.yml
- Enable scheduling (`@EnableScheduling`)

```java
package gr.rongasa.agregator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
public class SensorTasksConfig {

    @Bean
    public ExecutorService sensorsExecutorService() {
        return Executors.newFixedThreadPool(10);
    }
}
```

---

**Communication with Sensors**:

- Annotation of  `@Scheduled(fixedRate = 3600)` makes service run every 3600 ms. 
  
- Scheduling can work with crone jobs or can be dynamic. Usage of scheduling is easy. Good bye Quartz! 
  
- To use property file based fixedRate, etc use the string alternative of fixedRate and "${my.property}"
  
- Rest template is easy to use as well. 

  - Exchange method used below is made because the rest interface used here returns list of objects

  - Commonly getForEntity, postForEntity and put methods are used.

  - Uri variables feature is also important to know

    ```java
    Map<String, Object> params = new HashMap<>();
    params.put("sensorId", <dynamic value>);
    return restTemplate.getForObject("/metric/sensor/{sensorId}", String.class, params);
    ```

    

---

**Communication with Sensors (code):**

```java
package gr.rongasa.agregator.service;

....

@Service
@RequiredArgsConstructor
public class ScheduledTasks {
    private final SensorService sensorService;
    private final MetricService metricService;
    private final ExecutorService sensorsExecutorService;
    private final RestTemplate restTemplate;


    @Scheduled(fixedRate = 3600)
    public void collectMetrics() {
        int pageNumber = 0;
        Page<SensorDTO> page;
        do {
            page = sensorService.getSensors(PageRequest.of(pageNumber++, 50));
            page.stream().forEach(sensor ->
                    sensorsExecutorService.submit(() -> collectMetricsForSensor(sensor))
            );
        } while (page.hasNext());
    }
...
}
```

---
**Communication with Sensors (code):**

```java
private void collectMetricsForSensor(SensorDTO sensor) {
    //todo add pagination
    ResponseEntity<List<MetricDTO>> response = restTemplate.exchange(
            sensor.getLocation(),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<MetricDTO>>() {
            });
    if (response.getBody()!=null){
        response.getBody().forEach(metricService::save);
    }
}
```
---

**Exception handling in spring boot:**

<u>Entity Validation:</u>

- Add `javax.validation.constraints` wherever needed in the DTO objects, i.e. `@Min, @Max, @NotNull`.

- Add `@Valid` in rest interface

- Frontend now gets the validation errors in the best possible way.

<u>Exception handling:</u>

- Centrally handle Exceptions using `@ControllerAdvice/@ExceptionHandler`
- Prefer Custom Runtime exceptions
- Avoid overriding ResponseEntityExceptionHandler unless very custom logic is required. 

---
**Exception handling in spring boot (code):**

```java
package gr.rongasa.agregator.web.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerImpl  /*extends ResponseEntityExceptionHandler */ {

    @ExceptionHandler(value = {SensorException.class})
    protected ResponseEntity<Object> handleSensorException(SensorException sensorException, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(sensorException.getMessage(), null), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    
}
```

---

## Schema initialization

**properties file:** Enable liquidbase and set base changelog

```yaml
spring:
  liquibase:
    change-log: classpath:migration/master.xml
```
**Dependencies: ** Add the dependencies

```groovy
apply from: 'gradle/liquibase.gradle'
implementation 'org.liquibase:liquibase-core'
```

---

## Schema initialization



**Liquibase gradle task that can create the schema changelog files:**

`'gradle/liquibase.gradle'` Points to a gradle file that runs the main command of Liquidbase.

*This Points to a gradle task definition that runs liquidbase Main class with specific input that compares database with hibernate entities and produces the next changelog.*

**gradle.properties**

```properties
liquibase_hibernate5_version=3.6
```

---
**master.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<databaseChangeLog
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.4.xsd">

    <include file="changelog/initial_schema_changelog.xml"/>
    <include file="changelog/<feature>_schema_changelog.xml"/>
    ...

</databaseChangeLog>
```

---

**changelog.xml**

```xml
<?xml version="1.1" encoding="UTF-8" standalone="no"?>
<databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog" xmlns:ext="http://www.liquibase.org/xml/ns/dbchangelog-ext" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog-ext http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.6.xsd">
    <changeSet author="rongasal (generated)" id="1558682095036-1">
        <createTable tableName="metric">
            ...
        </createTable>
    </changeSet>
    ...
    <changeSet author="rongasal (generated)" id="1558682095036-3">
          ...
    </changeSet>
</databaseChangeLog>
```

### Exercise

It become quite soon apparent that aggregator stores a significant number of data. Thus it is decided that you should switch aggregator "microservice" to elasticsearch.

Tip: Spring data elastic does not behave well with Instant. I propose not to spend time with this. Try to use String for date-time.

