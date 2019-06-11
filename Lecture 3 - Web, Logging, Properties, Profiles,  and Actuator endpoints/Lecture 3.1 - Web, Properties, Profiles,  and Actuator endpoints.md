# Spring Data Web

---

## Focus

- Old fashioned Spring MVC applications using JSP, thymeleaf, or other templating view resolution technologies had the concept of view resolver and the concept of internationalization. 

  - This is still possible though not so much extensively used due to the appearance of modern View frameworks like Angular.
  - The creation and configuration of view resolver and internationalization is practically done automatically

- In modern applications, server focus on business logic and RESTfull API rather than view configuration and internationalization.
  - View/Frontend is served from the same or different servers (apache2, nginx) as pure html, script,... files. No view interceptor is involved.
  - Internationalization is part of frontend and server side is not involved on this


## Spring Boot with Thymeleaf

**Dependencies:**

```groovy
plugins {
	id 'org.springframework.boot' version '2.1.5.RELEASE'
	id 'java'
}
apply plugin: 'io.spring.dependency-management'
group = 'gr.rongasa'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '1.8'
configurations { compileOnly { extendsFrom annotationProcessor } }
repositories { 	mavenCentral() }
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

| Path                                         | Resource type                                                |
| -------------------------------------------- | ------------------------------------------------------------ |
| src\main\resources\templates                 | Templates that view resolver should process                  |
| src\main\resources\static\{css\image\js\...} | Static files like css, images, etc<br />These are accessible via http://<address:port>/{css\image\js}/{filename} |
|                                              |                                                              |

These are the default paths specified from auto-configuration. If one wishes different paths could modify `application.properties` file

```properties
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
```

---

## index.html Template

**Create a first template:**

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

**Create the controller providing the view name and the dynamic attributes:**

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

**Create the static files:**

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
- Thymeleaf view resolver is handled properly within the spring boot fat jar files. **Contrary** jsp view resolver is hard to accomplish and propose to avoid usage with fat jar of spring boot.
- Controller has a Model class. You may meet examples with ModelAndView class. This is the same functionality with only difference that inside this object the view name is also specified.

---

## Error page

**Create the default error page (error.html):**

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
Alternatevely create a controller that implements `ErrorController` with RequestMapping of `/error` 

---

## Internationalization

In one configuration class we need to configure the following:

- The application's message source

```java
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
```

- The locale resolver in order to be able to determine which locale is currently used into the session

```java
   @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        return slr;
    }
```

---

- The bean through which one can change the selected language

```java
@Bean
public LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
    lci.setParamName("lang");
    return lci;
}
```

- Finally add Locale interceptor into the applications interceptor registry

  ```java
  @Configuration
  public class InternationalizationConfiguration implements WebMvcConfigurer {
      ...
      @Override
          public void addInterceptors(InterceptorRegistry registry) {
              registry.addInterceptor(localeChangeInterceptor());
          }
  }
  ```
---

  - Add messages_xx.properties files in i18n folder (as configured in the message source bean)

  - Use this in view part i.e. 

    ```html
    <span th:text="#{header_label}">
    ```

---

## Actuator Endpoints

- Actuator endpoints are significant for micro-services.

  - Spring Boot micro-services, need to allow other systems to monitor and interact with the application.  
  - Devops need endpoints to monitor the health and status of the system and collect metrics.

- Details of the endpoints can be found in Spring boot documentation:

  https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html

- In order to enable actuator endpoints

  - Add actuator as dependency 

    (`implementation 'org.springframework.boot:spring-boot-starter-actuator'`)
  
  - Set into application.properties the following:

```yaml
  management:
	    endpoints:
	      web:
	        exposure:
	          include: '*'
```

---

## Most Useful Actuator Endpoints

| ENDPOINT       | USAGE                                                        |
| -------------- | ------------------------------------------------------------ |
| `/info`         | Displays arbitrary application info.                         |
| `/env`         | Returns list of properties in current environment            |
| `/health`      | Returns application health information.                      |
| `/configprops` | Displays a collated list of all `@ConfigurationProperties`   |
| `/beans`       | Returns a complete list of all the Spring beans in your application. |
| `/httptrace`   | Displays HTTP trace information                              |
| `/threaddump`  | It performs a thread dump.                                   |
| `/metrics`     | It shows several useful metrics information like JVM memory used, system CPU usage, open files, and much more. |

---

## Actuator Endpoints with Prometheus

- Prometheus is an opensource monitoring and alerting toolkit that integrates well with Grafana and is frequently used.
- Spring boot actuator endpoints expose for Prometheus everything needed very easily.

## Actuator Endpoints with Prometheus

**Dependencies:**

```groovy
	compile 'io.micrometer:micrometer-core:1.1.4'
	compile 'io.micrometer:micrometer-registry-prometheus:1.1.4'
```

**Configuration:**

```yaml
management:
  metrics:
    export:
      prometheus:
        enabled: true
  endpoint:
    metrics:
      enabled: true
    prometheus:
      enabled: true
  endpoints:
    web:
      exposure:
        include: '*'
```



## Properties/Configuration

- Application properties support profiles. With profiles developers can have different configuration properties for different environments, most commonly, production (prod), development (dev), unit testing (test).

- Property files per profile can be set using the following `application-<profile>.yml` or `application-<profile>.property`. There should always be a file names application.yml as a fallback property file.

- An active profile can be set as a VM options when running the applicationusing: `-Dspring.profiles.active=<profile>`. The default active profile can also be set in application.yml

- Properties of the application are a union of the `application.yml` and the active `application-<activeProfile>.yml`.

  In case a property exists in both these files the one in the active profile's property file takes precedence.

- All application properties can also be set from the command starting the application i.e. `-Dserver.port=8026`

```yaml
server:
  port: 8026
```

  

---

## Profile based settings

**Set active profile (application.yml):**

```yaml
spring:
  profiles:
    active: prod
```
**Specific properties for dev profile (application-dev.yml):**

```yaml
management:
  endpoints:
    web:
      exposure:
        include: '*'
```
**Specific properties for prod profile (application-prod.yml):**

```yaml
management:
  endpoints:
    web:
      exposure:
        include: 'health,info'
```

---

## Setting application properties from command Line

- `java -jar <jar file.jar>`

  Within the application.yml the default active profile is specified.

- `java -Dspring.profiles.active=dev -jar <jar file.jar>`

  With this command we set the active profile to be dev. This means that everything specified in application-prod.yml takes precedence. Anything not found there but existing in application.yml will be used as specified in application.yml 

- `java -Dspring.profiles.active=dev -Dmanagement.endpoints.web.exposure.include=health,info -jar <jar file.jar>`

  Any spring boot parameter can be specified. Externally specified parameters take precedence.

---

## Environment Variables And Default Variables

- Any environment variable is accessible from application properties files. 
- Any application property can be set from environment variables
- This is very useful when attributes should be set from a docker-compose file or kubernates or even bare metal deployments.
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

**Add in a configuration class:** `@EnableConfigurationProperties({HelloConfig.class})`

**Create a properties bean that can latter be autowired anywhere in code:**

```java
package gr.rongasa.helllo.config;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.*;
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

## Summarize Properties

1. Command line arguments.

2. Java System properties (`System.getProperties()`).

3. OS environment variables.

4. `@PropertySource` annotations on your `@Configuration` classes.

   Using PropertySource` one may configure any (.property) file to act as a property source for spring boot application. **Not recommended and not described above** 

5. Application properties outside of your packaged jar

   1. A `/config` subdirectory of the current directory.
   2. The current directory

6. Application properties packaged inside your jar