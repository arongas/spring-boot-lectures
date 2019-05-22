package gr.rongasa.agregator.domain.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table
public class Sensor {
    @Id
    String sensorId;
    String name;
    String location;
    String description;
    @OneToMany(mappedBy = "sensor")
    Set<Metric> metrics;
}
