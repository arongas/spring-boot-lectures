package gr.rongasa.jms.producer;

import gr.rongasa.jms.domain.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Producer {
    //    private final JmsTemplate jmsTemplate;
    private final JmsMessagingTemplate jmsMessagingTemplate;

    @Scheduled(fixedRate = 1000)
    private void send() {
        jmsMessagingTemplate.send("test.messages",
                MessageBuilder
                        .withPayload(Content.builder().name("Alex").lastName("Rongas").build()).build());
//        jmsTemplate.send("test.messages", session -> {
//            TextMessage message = session.createTextMessage(Instant.now().toString());
//            return message;
//        });
    }
}
