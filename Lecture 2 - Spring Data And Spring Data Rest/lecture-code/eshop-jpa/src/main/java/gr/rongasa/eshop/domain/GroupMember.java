package gr.rongasa.eshop.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class GroupMember {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name="group_infos", joinColumns= {@JoinColumn(name="group_id")},  inverseJoinColumns= {@JoinColumn(name="info_id")})
    private Set<GroupInfo> infoSet= new HashSet<>();
}
