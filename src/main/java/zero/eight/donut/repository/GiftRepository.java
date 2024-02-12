package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zero.eight.donut.domain.Gift;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GiftRepository extends JpaRepository<Gift, Long> {
    Optional<Gift> findById(Long giftId);
    List<Gift> findAllByGiftboxId(Long giftbox_id);
    List<Gift> findAllByGiverIdAndCreatedAtBetween(Long giverId, LocalDateTime startDate, LocalDateTime endDate);
    List<Gift> findByStore(String store);
    @Query(value = "SELECT SUM(g.sum) FROM Gift g WHERE g.store = :storeName", nativeQuery = true)
    int sumByStoreName(String storeName);

}
