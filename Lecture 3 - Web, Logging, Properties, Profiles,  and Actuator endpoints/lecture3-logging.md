## Logging

- Enabling debug mode

```yaml
debug: true
```
---

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