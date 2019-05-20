package gr.rongasa.library.domain.jpa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JpaResource {
    @Id
    private String trackingId;
    private String type;
    private String name;
    private String author;
    private String description;
    private String url;
}
