package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import zero.eight.donut.domain.Gift;

import java.util.List;

@Repository
public interface GiftRepository extends JpaRepository<Gift, Long> {
    List<Gift> findAllByGiftboxId(Long giftbox_id);
    List<Gift> findByStore(String store);
    @Query(value = "SELECT SUM(g.sum) FROM Gift g WHERE g.store = :storeName", nativeQuery = true)
    int sumByStoreName(String storeName);
}
