package gr.rongasa.library.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceDTO {
    @NotEmpty
    @JsonProperty("tracking_id")
    private String trackingId;
    private String type;
    private String name;
    private String author;
    @JsonProperty("abstract")
    private String description;
    private String url;
}
