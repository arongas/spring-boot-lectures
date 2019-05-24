package gr.rongasa.agregator.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorDTO {
    @NotNull
    @NotBlank
    String sensorId;
    @NotNull
    @NotBlank
    String name;
    @NotNull
    @NotBlank
    String location;
    String description;
}
