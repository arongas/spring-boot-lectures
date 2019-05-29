package gr.rongasa.sensor.service;

import gr.rongasa.sensor.domain.MetricDTO;
import gr.rongasa.sensor.domain.SensorDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class Producer {

    private final JmsMessagingTemplate jmsMessagingTemplate;
    private final ActiveMQTopic topic = new ActiveMQTopic("sensor.metric");
    @Value("${server.port}")
    String sensorId;

    @Scheduled(fixedRate = 5000)
    private void send() {
        MetricDTO metricDTO = MetricDTO.builder()
                .id(UUID.randomUUID())
                .time(Instant.now().toString())
                .sensor(SensorDTO.builder().sensorId(sensorId).name("bedroom").description("").build())
                .value(ThreadLocalRandom.current().nextDouble() + "")
                .build();
        MessageBuilder<MetricDTO> messageBuilder = MessageBuilder.withPayload(metricDTO);
        log.info("Sending metric {} at topic {}", metricDTO, topic);
        jmsMessagingTemplate.send(topic, messageBuilder.build());
    }
}
