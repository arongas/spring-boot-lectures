package gr.rongasa.eshop.repository;

import gr.rongasa.eshop.domain.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InventoryRepository extends MongoRepository<Inventory, String> {
    Page<Inventory> findAllByType(Pageable pageable, @Param("type") String type);

    Page<Inventory> findAllByName(Pageable pageable, @Param("type") String type);

    Page<Inventory> findByTypeAndAmountGreaterThanOrderByCost(Pageable pageable, @Param("type") String type, @Param("amount") Long amount);

    Optional<Inventory> findFirstByNameAndTypeOrderByCost(@Param("name") String name, @Param("type") String type);

    Page<Inventory> findDistinctTop2ByAmountLessThanOrderByAmountAsc(Pageable pageable, @Param("amount") Long amount);

    Page<Inventory> findDistinctTop2ByAmountLessThanOrderByAmountDesc(Pageable pageable, @Param("amount") Long amount);

    Page<Inventory> findByNameContaining(Pageable pageable, @Param("name") String name);

    Page<Inventory> findByNameEndingWith(Pageable pageable, @Param("name") String name);

    Page<Inventory> findByAmountIsBetween(Pageable pageable, @Param("min") Long min, @Param("max") Long max);

    Optional<Inventory> deleteByType(@Param("type") String type);

    boolean existsByType(@Param("type") String type);

    long countByType(@Param("type") String type);

//    @Query("select i from Inventory i where i.description=?1")
//    Inventory getInventory(@Param("details") String details);

//    @Query(value = "select * from Inventory where i.description=?1", nativeQuery = true)
//    Inventory getInventoryByNativeQuery(@Param("details")String details);

//    @Query("select i from Inventory i where i.description=:descr")
//    Inventory getInventoryByNamedParam(@Param("descr") String description);


//    @Modifying
//    @Query("update Inventory i set i.type=:press where i.type=:book")
//    int updateMediaType(@Param("press") String press, @Param("book") String book);

}
