package gr.rongasa.agregator.service;

import gr.rongasa.agregator.domain.dto.MetricDTO;
import gr.rongasa.agregator.domain.dto.SensorDTO;
import gr.rongasa.agregator.domain.jpa.Metric;
import gr.rongasa.agregator.domain.jpa.Sensor;
import gr.rongasa.agregator.repository.MetricRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = ElasticsearchAutoConfiguration.class)
@ActiveProfiles("test")
public class MetricServiceImplTest {

    private List<Metric> metrics = new ArrayList<>();
    @MockBean
    MetricRepository metricRepository;
    @Autowired
    MetricService metricService;

    @Before
    public void init() {
        Sensor sensor = new Sensor("sensor1", "mySensor", "this is my sensor");
        metrics.add(new Metric(UUID.randomUUID(), "1", Instant.now().toString(), sensor));
        metrics.add(new Metric(UUID.randomUUID(), "5", Instant.now().toString(), sensor));
    }

    @Test
    public void getMetrics() {
        when(metricRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(metrics));
        Page<MetricDTO> metricDTOS = metricService.getMetrics(Pageable.unpaged());
        //verify everything with assertions
        verify(metricRepository, times(1)).findAll(Pageable.unpaged());
    }

    @Test
    public void getMetric() {
        when(metricRepository.findById(any(UUID.class))).thenAnswer(new Answer<Optional<Metric>>() {
            @Override
            public Optional<Metric> answer(InvocationOnMock invocation) throws Throwable {
                return metrics.stream().filter(m -> m.getId().equals(invocation.getArguments()[0])).findFirst();
            }
        });
        UUID id = metrics.get(0).getId();
        Optional<MetricDTO> metricDTO = metricService.getMetric(id);
        //verify everything with assertions
        verify(metricRepository, times(1)).findById(id);
    }

    @Test
    public void getMetricNotFound() {
        when(metricRepository.findById(any(UUID.class))).thenAnswer(new Answer<Optional<Metric>>() {
            @Override
            public Optional<Metric> answer(InvocationOnMock invocation) throws Throwable {
                return metrics.stream().filter(m -> m.getId().equals(invocation.getArguments()[0])).findFirst();
            }
        });
        UUID id = UUID.randomUUID();
        Optional<MetricDTO> metricDTO = metricService.getMetric(id);
        assertThat(metricDTO).isEmpty();
        verify(metricRepository, times(1)).findById(id);
    }

    @Test
    public void getMetricsBySensorId() {
    }


    @Test
    public void save() {
        UUID id = UUID.randomUUID();
        when(metricRepository.save(any(Metric.class))).thenAnswer(new Answer<Metric>() {
            @Override
            public Metric answer(InvocationOnMock invocation) throws Throwable {
                Metric metric = (Metric) invocation.getArguments()[0];
                metric.setId(id);
                return metric;
            }
        });
        SensorDTO sensordto = new SensorDTO("sensor1", "mySensor", "this is my sensor");
        MetricDTO metricDTO = metricService.save(new MetricDTO(null, "1", Instant.now().toString(), sensordto));
        assertThat(metricDTO).isNotNull();
        verify(metricRepository, times(1)).save(any(Metric.class));
    }
}