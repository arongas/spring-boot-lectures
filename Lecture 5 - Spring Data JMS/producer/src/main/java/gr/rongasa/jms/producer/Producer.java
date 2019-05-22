package gr.rongasa.jms.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.jms.TextMessage;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class Producer {
    private final JmsTemplate jmsTemplate;

    @Scheduled(fixedRate = 1)
    private void send(){
        jmsTemplate.send("test.messages", session -> {
            TextMessage message = session.createTextMessage(Instant.now().toString());
            return message;
        });
    }
}
