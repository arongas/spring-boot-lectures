package gr.rongasa.agregator.domain.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table
public class MetricDTO {
    private UUID id;
    private String value;
    private String source;
    private Instant time;
}
