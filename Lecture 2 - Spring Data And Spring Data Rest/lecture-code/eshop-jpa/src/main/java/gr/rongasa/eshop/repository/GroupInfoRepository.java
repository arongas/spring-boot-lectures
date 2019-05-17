package gr.rongasa.eshop.repository;

import gr.rongasa.eshop.domain.GroupInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface GroupInfoRepository extends JpaRepository<GroupInfo,Long> {
    @EntityGraph(value = "members")
    Page<GroupInfo> findAllByMembersName(@Param("name") String name, Pageable page);
}
