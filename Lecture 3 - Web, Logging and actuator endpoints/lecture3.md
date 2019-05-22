# Web

- Old fashioned Spring MVC applications using JSP, thymeleaf, or other templating view resolution technologies had the concept of view resolver and the concept of internationalization. This is still possible though not so much extensively used due to the appearance of modern View frameworks like Angular.

- In modern applications, server focus on business logic and RESTfull API rather than view configuration and internationalization.
  - View/Frontend is served from the same or different servers (apache2, nginx) as pure html, script,... files. No view interceptor is involved.
  - Internationalization is part of frontend and server side is not involved on this

  

## Spring Boot with JSP

Dependencies:

```groovy
plugins {
	id 'org.springframework.boot' version '2.1.5.RELEASE'
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
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'

	implementation 'org.webjars:bootstrap:4.3.1'

}

```

---

## Paths

Default paths:

| Path                                            | Resource type                                                |
| ----------------------------------------------- | ------------------------------------------------------------ |
| src\main\resources\templates                    | templates that view resolver should process                  |
| src\main\resources\static\{css\|image\|js\|...} | static files like css, images, etc<br />Tese are accessible via http://<address:port>/{css\|image\|js}/{filename} |
|                                                 |                                                              |

These are the default paths specified from auto-configuration. If one wishes different paths could modify

```properties
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
```

---

## index.html Template

```html
<!DOCTYPE HTML>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<html xmlns:th="http://www.w3.org/1999/xhtml">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <title>Hello ${name} from Spring Boot</title>
        <link rel="stylesheet" th:href="@{webjars/bootstrap/4.3.1/css/bootstrap.min.css}"/>
        <link rel="stylesheet" th:href="@{/css/style.css}"/>
        <script type="text/javascript" th:src="@{webjars/bootstrap/4.3.1/js/bootstrap.min.js}"></script>
    </head>
    <body>
    <main role="main" class="container">
        <div class="starter-template">
            <h1>Spring Boot Web Thymeleaf Example</h1>
            <h2><span th:text="'Hello, ' + ${name}"></span></h2>
        </div>
        <ol><li class="list" th:each="name : ${names}" th:text="${name}"></li></ol>
    </main>
    </body>
</html>
```

---
## Controller

```java
package gr.rongasa.helllo.web.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
import java.util.List;
@Controller
public class HelloController {
    private List<String> names=new ArrayList<>();
    @GetMapping({"/"})
    public String hello(Model model, @RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        model.addAttribute("name", name);
        if (name.compareToIgnoreCase("Word")!=0){
            names.add(name);
            model.addAttribute("names", names);
        }
        return "index";
    }
}
```

---

## css file

```css
body {
    padding: 15rem;
}
.starter-template {
    padding: 3rem 1.5rem;
    text-align: center;
}

h1{
    color:darkblue;
}

h2{
    color:blue;
}

.list{
    color: darkgray;
}
```



## Notes

- css file is accessible from http://localhost:8080/css/style.css
- Thymeleaf view resolver is handled properly within the spring boot fat jar files. Contrary jsp view resolver is hard to accomplish and propose to avoid.
- Controller has a Model class. You may meet examples with ModelAndView class. This is the same functionality with only difference that inside this object the view name is also specified.

---

## Error page

```java
<!DOCTYPE HTML>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<html xmlns:th="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Something went wrong</title>

    <link rel="stylesheet" th:href="@{webjars/bootstrap/4.3.1/css/bootstrap.min.css}"/>
    <link rel="stylesheet" th:href="@{/css/style.css}"/>

    <script type="text/javascript" th:src="@{webjars/bootstrap/4.3.1/js/bootstrap.min.js}"></script>
</head>
<body>
    <main role="main" class="container">
        <h1>Something went wrong! </h1>
        <h2>Our Engineers are on it</h2>
        <a href="/">Go Home</a>
    </main>
</body>
</html>
```

## Actuator Endpoints

- Spring Boot micro services, need to allow other systems to monitor and interact with the application.  

- Details of the endpoints can be found in Spring boot documentation:

  https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html

  

- In order to enable these

  - Add actuator as dependency (`implementation 'org.springframework.boot:spring-boot-starter-actuator'`)

  - Set into application.yml the following:

   ```yaml
    management:
      endpoints:
        web:
          exposure:
            include: 'health,info'
   ```

---

## Most Useful Actuator Endpoints

| ENDPOINT       | USAGE                                                        |
| -------------- | ------------------------------------------------------------ |
| `/env`         | Returns list of properties in current environment            |
| `/health`      | Returns application health information.                      |
| `/configprops` | Displays a collated list of all `@ConfigurationProperties`   |
| `/beans`       | Returns a complete list of all the Spring beans in your application. |
| `/httptrace`   | Displays HTTP trace information                              |
| `/threaddump`  | It performs a thread dump.                                   |
| `/metrics`     | It shows several useful metrics information like JVM memory used, system CPU usage, open files, and much more. |

---

## Properties

- Application properties support profiles. With profiles developers can have different configuration properties for different environments, most commonly, production (prod), development (dev), unit testing (test).

- Property files per profile can be set using the following application-<profile>.yml or application-<profile>.property. There should always be a file names application.yml as a fallback property file.

- An active profile can be set as a VM options when running the applicationusing: `-Dspring.profiles.active=<profile>`. The default active profile can also be set in application.yml

- Properties of the application are a union of the application.yml and the active application-<activeProfile>.yml.

  In case a property exists in both these files the one in the active profile's property file takes precedence.

- All application properties can also be set from the command starting the application i.e. `-Dserver.port=8026`

  ```yaml
  server:
    port: 8026
  ```

  

---

## Profile based settings

Set active profile (application.yml)
```yaml
spring:
  profiles:
    active: prod
```
Specific properties for dev profile (application-dev.yml)

```yaml
management:
  endpoints:
    web:
      exposure:
        include: '*'
```
Specific properties for prod profile (application-prod.yml)

```yaml
management:
  endpoints:
    web:
      exposure:
        include: 'health,info'
```

---

## Setting Application parameters from command Line

- java -jar <jar file.jar>

  Within the application.yml the default active profile is specified.

- java -Dspring.profiles.active=dev -jar <jar file.jar>

  With this command we set the active profile to be dev. This means that everything specified in application-prod.yml takes precedence. Anything not found there but existing in application.yml will be used as specified in application.yml 

- java -Dspring.profiles.active=dev -Dmanagement.endpoints.web.exposure.include=health,info -jar <jar file.jar>

  Any spring boot parameter can be specified. Externally specified parameters take precedence.

---

## Environment Variables and default variables

- Any environment variable is accessible from application properties files. 
- Any program argument can be used from application properties
- Default values can be specified

```yaml
spring:  
  datasource:
      type: com.zaxxer.hikari.HikariDataSource
      url: jdbc:postgresql://${POSTGRES_IP:127.0.0.1}:5432/${POSTGRES_DB:library}
      username: ${POSTGRES_USER:librarian}
      password: ${POSTGRES_PASSWORD:DeltaAlpha21@@@}
      platform: POSTGRESQL
      driverClassName: org.postgresql.Driver
logging: 
  file: ${library.home.directory:/}
library:
   home:
      directory: ${user.home}
```

---

## Accessing Properties From Code

```yaml
hello:
  config:
    track: true
```



```java
@Controller
public class HelloController {

    @Value("${hello.config.track}")
    private boolean track;
    // ...
}


```

---

## Type-safe Configuration Properties

```yaml
hello:
  name: ${user.name:world}
  config:
    track: true
    list:
      - world
      - alex
```



---

## Type-safe Configuration Properties

Add: `@EnableConfigurationProperties({HelloConfig.class})`

**And**

```java
package gr.rongasa.helllo.config;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.ArrayList;
import java.util.List;
@ConfigurationProperties(prefix = "hello")
@Data
@NoArgsConstructor
public class HelloConfig {
    private String name="World";
    private Config config=new Config();
    @Data
    @NoArgsConstructor
    public static class Config {
        private boolean track=true;
        private List<String> list=new ArrayList<>();
    }
}
```

---

## Summarize Properties Order (non-cloud)

1. Command line arguments.

2. Java System properties (`System.getProperties()`).

3. OS environment variables.

4. `@PropertySource` annotations on your `@Configuration` classes.

   Using PropertySource` one may configure any (.property) file to act as a property source for spring boot application. **Not recomended**

5. Application properties outside of your packaged jar

   1. A `/config` subdirectory of the current directory.
   2. The current directory

6. Application properties packaged inside your jar

---

## Logging



- Enabling debug mode

```yaml
debug: true
```

- Configuring logging via YAML

```yaml
logging:
  level:
    root: warn
    gr.rongasa: info
    com.netflix: error
  pattern:
    file: "%d{yyyy-MM-dd'T'HH:mm:ss'.'SSSZ} %-5level ${spring.application.name} %thread %logger -- %msg%n"

  file: ${user.home}/${spring.application.name}/log/${spring.application.name}.log
  file.max-history: 10
  file.max-size: 10MB
```

---

- Configuring logging via command line arguments

  ```
  -Dlogging.level.gr.rongasa=TRACE 
  ```

- Configuring logging via logging configuration file

  - Logback Configuration Logging: When a file in the classpath has one of the following names, Spring Boot will automatically load it
    - *logback-spring.xml*
    - *logback.xml*
    - *logback-spring.groovy*
    - *logback.groovy*

  

  > Appenders: https://logback.qos.ch/manual/appenders.html

  ---

  - Log4j2 Configuration Logging
    - Remove dependency org.springframework.boot:spring-boot-starter-logging
    - Add Dependency org.springframework.boot:spring-boot-starter-log4j2
    - Configure log4j via
      - log4j2-spring.xml
      - log4j2.xml

---

## logback-spring.xml example

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <springProfile name="!dev">
        <include resource="org/springframework/boot/logging/logback/defaults.xml"/>
        <include resource="org/springframework/boot/logging/logback/console-appender.xml"/>
        <appender name="ROLLING-FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <encoder>
                <pattern>${FILE_LOG_PATTERN:-%d{yyyy-MM-dd'T'HH:mm:ss'.'SSSZ} %-5level ${spring.application.name} %thread %logger -- %msg%n}</pattern>
            </encoder>
            <file>${LOG_FILE:-e-shop.log}</file>
                <rollingPolicy class="ch.qos.logback.core.rolling.FixedWindowRollingPolicy">
                    <fileNamePattern>${LOG_FILE:-e-shop.log}.%i</fileNamePattern>
                    <minIndex>1</minIndex>
                    <maxIndex>${LOG_FILE_MAX_HISTORY:-10}</maxIndex>
                </rollingPolicy>
                <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
                    <maxFileSize>${LOG_FILE_MAX_SIZE:-10MB}</maxFileSize>
                </triggeringPolicy>
        </appender>
        <root level="INFO">
            <appender-ref ref="ROLLING-FILE"/>
            <appender-ref ref="CONSOLE"/>
        </root>
    </springProfile>
</configuration>
```