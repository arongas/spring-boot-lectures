package gr.rongasa.eshop.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;

@Document(indexName = "inventory")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Inventory {
    @Id
    private String id;
    private String name;
    private Long amount;
    private String description;
    private String type;
    private BigDecimal cost;
}
