package gr.rongasa.eshop.repository;

import gr.rongasa.eshop.domain.GroupMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

public interface GroupMemberRepository extends MongoRepository<GroupMember, String> {
    Page<GroupMember> findAllByInfoSetName(@Param("name") String name, Pageable page);
}
