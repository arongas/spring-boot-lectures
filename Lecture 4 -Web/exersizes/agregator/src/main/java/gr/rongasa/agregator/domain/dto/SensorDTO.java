package gr.rongasa.agregator.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorDTO {
    @NotBlank
    String sensorId;
    @NotBlank
    String name;
    @NotBlank
    String location;
    String description;
}
