package gr.rongasa.jms.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class JmsService {

    @JmsListener(destination = "test.messages",
            containerFactory = "notificationEventContainerFactory",
            concurrency = "1" )
    public void notificationEvent(@Payload String payload) {

        System.out.println("Received: "+ payload);
    }
}
