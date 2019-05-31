package gr.rongasa.agregator.service;

import gr.rongasa.agregator.domain.dto.MetricDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
@RequiredArgsConstructor
@Slf4j
public class JmsService {
    public final MetricService metricService;

    @Getter
    @Setter
    CountDownLatch countDownLatch=null;

    @JmsListener(destination = "sensor.metric", containerFactory = "metricsListenerFactory", concurrency = "1-10")
    public void readMetric(Message<MetricDTO> message) {
        log.info("Received message {}, {}",message.getHeaders(), message.getPayload());
        metricService.save(message.getPayload());
        if (countDownLatch!=null){
            countDownLatch.countDown();
        }
    }

}
