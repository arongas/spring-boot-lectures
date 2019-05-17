package gr.rongasa.eshop.repository;

import gr.rongasa.eshop.domain.GroupInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

public interface GroupInfoRepository extends MongoRepository<GroupInfo, String> {
    Page<GroupInfo> findAllByMembersName(@Param("name") String name, Pageable page);
}
