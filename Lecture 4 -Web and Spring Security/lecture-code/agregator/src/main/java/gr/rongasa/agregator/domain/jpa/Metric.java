package gr.rongasa.agregator.domain.jpa;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table
public class Metric {
    @Id
    private UUID id;
    private String value;
    private Instant time;
    @ManyToOne(cascade = CascadeType.ALL)
    private Sensor sensor;
}
