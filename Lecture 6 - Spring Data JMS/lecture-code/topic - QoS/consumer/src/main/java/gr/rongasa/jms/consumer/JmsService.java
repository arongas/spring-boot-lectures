package gr.rongasa.jms.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class JmsService {

    @JmsListener(destination = "test.messages")
    public void notificationEvent(@Payload String payload) {
        Integer.parseInt(payload); //throw exception...
        System.out.println("Received: " + payload);
    }
}
