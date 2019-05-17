package gr.rongasa.eshop.repository;

import gr.rongasa.eshop.domain.GroupMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface GroupMemberRepository extends JpaRepository<GroupMember,Long> {
    @EntityGraph(attributePaths = {"infoSet"})
    Page<GroupMember> findAllByInfoSetName(@Param("name") String name, Pageable page);
}
