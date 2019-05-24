package gr.rongasa.agregator.domain.jpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "sensor")
@Getter
@Setter
@NoArgsConstructor
public class Sensor {
    @Id
    String sensorId;
    String name;
    String location;
    String description;
}
