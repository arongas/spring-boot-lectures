package gr.rongasa.jms.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.jms.TextMessage;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class Producer {
    private final JmsTemplate jmsTemplate;
private final JmsMessagingTemplate jmsMessagingTemplate;
    @Scheduled(fixedRate = 1000)
    private void send(){
//        jmsMessagingTemplate.send("test.messages",
//                MessageBuilder
//                        .withPayload(Instant.now().toString())
//                        .setHeader("type","instant").build());
        jmsTemplate.send("test.messages", session -> {
            TextMessage message = session.createTextMessage(Instant.now().toString());
            return message;
        });
    }
}
