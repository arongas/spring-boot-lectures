# Spring Boot JMS 

- JMS messages is one of the most commonly used asynchronous means of micro-service communication.
- There are different JMS implementations such as RabbitMQ and ActiveMQ
- Spring Boot unifies to the extend possible the various JMS implementations. However this is not fully possible. 
- It is highly advisable that when selecting/using a JMS implementation the specifics of this technology are known

---

## ActiveMQ

- Queues vs Topics

  Topics are a broadcast mechanism while queues are a unicast mechanism (only one consumer will receive the message)

- Persistent messages vs durable subscriber

  Persistent non delivered messages will not be lost if  Activemq restarts. 

  Persistent topic messages will be lost if a topic consumer is not present at the time that the message was sent from the producer.

  Topic messages (persistent or not), will be delivered to the consumer if and only if the consumer is active when the message is sent or if the consumer has a Durable subscription.

- Be aware of the dead letter queue and Durable subscriptions eviction
- Order is maintained from Activemq IF AND ONLY IF you are at a single threaded application.
- Message Redelivery policy

---

## ActiveMq

**Dependency:**

```groovy
implementation 'org.springframework.boot:spring-boot-starter-activemq'
```

**JmsTemplate:**

JmsTemplate and *JmsMessagingTemplate* (built ontop of JmsTemplate) are the proper way of sending messages to the broker. It handles opening/closing connections/sessions with the broker.

**Connection Management:**

Spring provides the following types of *ConnectionFactory*:

- **SingleConnectionFactory –** Will maintain one connection with the broker. 
- **CachingConnectionFactory –** Will maintain one connection with the broker but also caching the *Sessions*, created with the broker.

## Queue Consumer

Add the dependency

```groovy
implementation 'org.springframework.boot:spring-boot-starter-activemq'
```

Add The configuration:

```yaml
spring:
  activemq:
    in-memory: false
#    broker-url: tcp://localhost:61616
  application:
    name: Consumer
server:
  port: 8181
```

- Only actual required property is to denote spring boot that the broker is not an embedded activemq.
- If broker is running at a different machine, all that is required is the broker's URL

---

## JMS Configuration

```java
@Configuration
@EnableJms
public class JMSConfiguration {
}
```

> `@EnableJms` triggers the discovery of methods annotated with `@JmsListener`.  

---

## JMS Consumer

```java
@Service
public class JmsService {

    @JmsListener(destination = "test.messages")
    public void notificationEvent(@Payload String payload) {

        System.out.println("Received: " + payload);
    }
}
```

Annotated beans with `JMSListener` can retrieve the message sent from the producer.

The default `MessageConverter` is able to convert only basic types (such as `String`, `Map`, `Serializable`). String is of course possible to be received from a Consumer. In case complex types need to be received, JMS Message converter needs to be enhanced. 

## JMS Producer

Again include the aActivemq dependency and specify the properties.

```yaml
spring:
  jms:
    template:
      delivery-mode: persistent
  activemq:
    in-memory: false
  application:
    name: Consumer
server:
  port: 8182
```

- With `spring.jms.template.delivery-mode=persistent` The message persistency is specified. Of course the persistency is specified at message producer side.
- Delivery mode can be specified in the `JmsTemplate` for all messages or can be specified per message
- Retries mechanism can be configured. 

## JMS Producer

```java
@Service
@RequiredArgsConstructor
public class Producer {
    private final JmsTemplate jmsTemplate;

    @Scheduled(fixedRate = 1000)
    private void send(){
        jmsTemplate.send("test.messages", session -> {
            TextMessage message = session.createTextMessage(Instant.now().toString());
            return message;
        });
    }
}

```

- Any method can `JmsTemplate` and use send method, first argument is the destination name and second argument the MessageCreator. 
- The default destination is queue. 
- The default destination can be changed
- If same application needs to send to topics and queues using `JmsTemplate`, there is a sent command that has as Destination object the destination and not as string.

---

## JMS Producer

Another way to send   JMS message is to Autowire and use `JmsMessagingTemplate`

Dont forget that `JmsMessagingTemplate` is just a wrapper so any configuration of `JmsTemplate` applies to `JmsMessagingTemplate` ` as well

``

```java
jmsMessagingTemplate.send("test.messages",
        MessageBuilder
                .withPayload(Instant.now().toString())
                .setHeader("type","instant").build());
```

---

## From Queue to Topic

```yaml
spring:
  jms:
    pub-sub-domain: true
```

This needs to be done in both producer and consumer

---

## Durable Topic subscription

- Producer is not affected

- Only topics have meaning with respect to durable subscription since in queues the point-to-point communication makes it that only one consumer will consume the message and that the message will be stored into the queue until consumer receives the message.

---

```yaml
spring:
  jms:
    pub-sub-domain: true
    cache:
      enabled: false
  activemq:
    in-memory: false
#    broker-url: tcp://localhost:61616
  application:
    name: Consumer
server:
  port: 8181
```

> For durable subscribers, sessions cannot be cached (`CachingConnectionFactory ` shouldn't be used)

---
There is the need to configure the JMS listener connection factory and set the subscription as durable and also set the client id

```java
    factory.setSubscriptionDurable(true);
    factory.setClientId("consumer-subscriber");
```
---

```java
package gr.rongasa.jms.consumer;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
public class JMSConfiguration {  
    @Bean("notificationEventContainerFactory")
    public DefaultJmsListenerContainerFactory notificationEventContainerFactory(
            DefaultJmsListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setSubscriptionDurable(true);
        factory.setClientId("consumer-subscriber");
        factory.setPubSubDomain(true);
        return factory;
    }    
}
```

---

JMS service now needs to use this connection factory. Note that concurrency here cannot be greater than 1. At durable subscriptions, the client id should be unique at **broker**.

```java
package gr.rongasa.jms.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class JmsService {
    @JmsListener(destination = "test.messages",
            containerFactory = "notificationEventContainerFactory",
            concurrency = "1")
    public void notificationEvent(@Payload String payload) {
        System.out.println("Received: " + payload);
    }
}
```

## Redeliveries and Time-To-Live

- When a message is sent to a queue or to a durable topic, it cannot remain there for ever. 
- Broker's settings define the default time to live of messages.
- The time to live can be overwritten at **producer** side through the `JmsTemplate`

```yaml
spring:
  jms:
    template:
      delivery-mode: persistent
      qos-enabled: true
      time-to-live: 100000
```

- After a message expires it is sent to the dead letter queue

## Redeliveries and Time-To-Live

- A Consumer, may fail to consume a message, for instance a required resource is unavailable and consumer throw s exception.
- Broker has a redelivery policy.
- Message redelivery policy can be specified/extended from **consumer**
- After maximum redeliveries are reached, message is moved to dead letter queue.

---

```java
package gr.rongasa.jms.consumer;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.SingleConnectionFactory;
import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
public class JMSConfiguration {
    @Value("${producer.jms.redelivery.backOffMultiplier}")
    double backOffMultiplier;
    @Value("${producer.jms.redelivery.useExponentialBackOff}")
    boolean useExponentialBackOff;
    @Value("${producer.jms.redelivery.maximumRedeliveries}")
    int maximumRedeliveries;
....
}
```

---

```java
package gr.rongasa.jms.consumer;
@Configuration
@EnableJms
public class JMSConfiguration {
    @Bean
    public InitializingBean connectionFactory(ConnectionFactory connectionFactory) {
        return () ->
        {
            if (connectionFactory instanceof ActiveMQConnectionFactory) {
                configureRedeliveryPolicy((ActiveMQConnectionFactory) connectionFactory);
            }else if (connectionFactory instanceof SingleConnectionFactory
                    && ((SingleConnectionFactory) connectionFactory).getTargetConnectionFactory() instanceof ActiveMQConnectionFactory){
                configureRedeliveryPolicy((ActiveMQConnectionFactory) ((SingleConnectionFactory) connectionFactory).getTargetConnectionFactory());
            }
        };
    }

    private void configureRedeliveryPolicy(ActiveMQConnectionFactory connectionFactory) {
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setUseExponentialBackOff(useExponentialBackOff);
        redeliveryPolicy.setBackOffMultiplier(backOffMultiplier);
        redeliveryPolicy.setMaximumRedeliveries(maximumRedeliveries);
        connectionFactory.setRedeliveryPolicy(redeliveryPolicy);
    }
}
```

---

```yaml
producer:
  jms:
    redelivery:
      backOffMultiplier: 3
      useExponentialBackOff: false
      maximumRedeliveries: 2
```

---

**Some notes:**

- `InitializingBean` is one of the spring boot means to initialize/configure programmatically existing beans. This code is called after spring boot properties are set.
- Redelivery policy of connection factory sets the default redelivery policy. There is a method named `setRedeliveryPolicyMap` that sets the redelivery policy per destination.

---

## Json JMS message serialization/deserialization

Assume a DTO of the following structure:

```java
package gr.rongasa.jms.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Content {
    private String name;
    private String lastName;
}
```

---

In both consumer and producer the following bean is needed in order to de/serialize the DTO to json

```java
@Bean
public MessageConverter jacksonJmsMessageConverter() {
    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setTargetType(MessageType.TEXT);
    converter.setTypeIdPropertyName("_type");
    return converter;
}
```

---

At consumer side, listener will no longer receive String but a Message 

```java
@Service
public class JmsService {

    @JmsListener(destination = "test.messages")
    public void notificationEvent(Message<Content> message) {
        System.out.println("Received: " + message.getPayload());
    }
}
```

---

- _type is a message header property that contains a key, typically the class name (FQN) assisting Jackson to Serialize/Deserialize based on a granted class structure.
- Class Content in above Producer and Consumer has the same package. This is not always common. 
- When package cannot be the same, it is needed to have something set in the message header that denotes/sets  the type of the class in the consumer side. This can i.e. be the class name. This can be accomplished as below.

```java
@Bean
public MessageConverter jacksonJmsMessageConverter() {
    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setTargetType(MessageType.TEXT);
    converter.setTypeIdPropertyName("_type");
    Map<String,Class<?>> typeIdMappings = new HashMap<>();
    typeIdMappings.put(Content.class.getSimpleName(), Content.class);
    converter.setTypeIdMappings(typeIdMappings);
    return converter;
}
```

---

## Exercise

From the previous lecture there is an Aggregator and a Sensor using JPA. 

- Remove the registration process of sensors and make the sensors app send messages with sensor data to a Queue. 

- Aggregator will receive these metrics and store these to the database as before.

  

