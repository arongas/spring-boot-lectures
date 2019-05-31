package gr.rongasa.agregator.web.rest;

import gr.rongasa.agregator.domain.dto.MetricDTO;
import gr.rongasa.agregator.domain.dto.SensorDTO;
import gr.rongasa.agregator.service.MetricService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MetricServiceResource.class)
public class MetricServiceResourceTest {
    @MockBean
    MetricService metricService;
    List<MetricDTO> metricDTOList;
    MetricDTO metricDTO;
    SensorDTO sensordto;
    @Autowired
    private MockMvc mockMvc;
    private String baseUri = "/";

    @Before
    public void setUp() throws URISyntaxException {
        metricDTOList = new ArrayList<>();
        sensordto = new SensorDTO("sensor1", "mySensor", "this is my sensor");
        metricDTO = new MetricDTO(UUID.randomUUID(), "1", Instant.now().toString(), sensordto);
        metricDTOList.add(metricDTO);
        PageImpl<MetricDTO> page = new PageImpl<>(metricDTOList);
        doReturn(page).when(metricService).getMetrics(any(Pageable.class));
        doReturn(Optional.of(metricDTO)).when(metricService).getMetric(metricDTO.getId());
    }

    @Test
    public void getMetrics() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUri + "metric")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", notNullValue()))
                .andExpect(jsonPath("$.content[0].value", is("1")))
                .andExpect(jsonPath("$.content[0].time", notNullValue()))
                .andExpect(jsonPath("$.content[0].sensor.sensorId", is("sensor1")))
                .andExpect(jsonPath("$.content[0].sensor.name", is("mySensor")))
                .andExpect(jsonPath("$.content[0].sensor.description", is("this is my sensor")));
    }

    @Test
    public void getMetric() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUri + "metric/" + metricDTO.getId().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.value", is("1")))
                .andExpect(jsonPath("$.time", notNullValue()))
                .andExpect(jsonPath("$.sensor.sensorId", is("sensor1")))
                .andExpect(jsonPath("$.sensor.name", is("mySensor")))
                .andExpect(jsonPath("$.sensor.description", is("this is my sensor")));

    }

    @Test
    public void getMetricsBySensor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUri + "metric")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", notNullValue()))
                .andExpect(jsonPath("$.content[0].value", is("1")))
                .andExpect(jsonPath("$.content[0].time", notNullValue()))
                .andExpect(jsonPath("$.content[0].sensor.sensorId", is("sensor1")))
                .andExpect(jsonPath("$.content[0].sensor.name", is("mySensor")))
                .andExpect(jsonPath("$.content[0].sensor.description", is("this is my sensor")));
    }
}