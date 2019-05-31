package gr.rongasa.agregator.service;

import gr.rongasa.agregator.domain.dto.MetricDTO;
import gr.rongasa.agregator.domain.dto.SensorDTO;
import gr.rongasa.agregator.repository.MetricRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = {ElasticsearchAutoConfiguration.class})
@ActiveProfiles("test")
@MockBean(MetricRepository.class)
public class JmsServiceTest {
    private MetricDTO metricDTO;
    private SensorDTO sensordto;

    private CountDownLatch countDownLatch;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private JmsService jmsService;

    @MockBean
    private MetricService metricService;

    @Before
    public void setUp() throws URISyntaxException {
        sensordto = new SensorDTO("sensor1", "mySensor", "this is my sensor");
        metricDTO = new MetricDTO(UUID.randomUUID(), "1", Instant.now().toString(), sensordto);
        doReturn(metricDTO).when(metricService).save(any(MetricDTO.class));
    }


    @Test
    public void readMetric() throws InterruptedException {
        setJmsServiceLatch(1);
        jmsTemplate.convertAndSend("sensor.metric", metricDTO);
        waitForJmsServiceLatch();
        verify(metricService, times(1)).save(any(MetricDTO.class));
    }


    private void setJmsServiceLatch(int count) {
        countDownLatch = new CountDownLatch(count);
        jmsService.setCountDownLatch(countDownLatch);
    }

    private void waitForJmsServiceLatch() throws InterruptedException {
        boolean latchFinishedCorrectly = countDownLatch.await(15, TimeUnit.SECONDS);
        if (!latchFinishedCorrectly) {
            fail("Latch finished due to time out");
        }
    }
}