package gr.rongasa.agregator.domain.jpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.Instant;
import java.util.UUID;

@Document(indexName = "metric")
@Getter
@Setter
@NoArgsConstructor
public class Metric {
    @Id
    private UUID id;
    private String value;
    private String time;
    private Sensor sensor;
}
