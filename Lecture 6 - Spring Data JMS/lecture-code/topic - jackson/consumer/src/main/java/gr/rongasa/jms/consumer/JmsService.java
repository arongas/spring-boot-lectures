package gr.rongasa.jms.consumer;

import gr.rongasa.jms.domain.Content;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class JmsService {

    @JmsListener(destination = "test.messages")
    public void notificationEvent(Message<Content> message) {
        System.out.println("Received: " + message.getPayload());
    }
}
