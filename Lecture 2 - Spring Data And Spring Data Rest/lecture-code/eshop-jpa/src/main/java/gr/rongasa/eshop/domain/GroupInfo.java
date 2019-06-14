package gr.rongasa.eshop.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@NamedEntityGraph(name = "GroupInfo.detail",
        attributeNodes = @NamedAttributeNode("members"))
public class GroupInfo {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany(mappedBy="infoSet")
    @Builder.Default
    List<GroupMember> members = new ArrayList<>();

}