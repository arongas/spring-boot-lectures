package gr.rongasa.library.domain.jpa;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Media {
    @Id
    private String trackingId;
    private String type;
    private String name;
    private String author;
    private String description;
    @ManyToOne
    private Customer customer;
}
