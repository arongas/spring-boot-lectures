package gr.rongasa.agregator.domain.jpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
public class Sensor {
    private String sensorId;

    private String name;

    private String description;

}
