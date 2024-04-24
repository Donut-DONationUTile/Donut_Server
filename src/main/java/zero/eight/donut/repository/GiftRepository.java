package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zero.eight.donut.domain.Gift;
import zero.eight.donut.domain.Giver;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GiftRepository extends JpaRepository<Gift, Long> {
    Optional<Gift> findById(Long giftId);
    List<Gift> findAllByGiftboxId(Long giftbox_id);
    List<Gift> findAllByGiverIdAndCreatedAtBetween(Long giverId, LocalDateTime startDate, LocalDateTime endDate);
    @Query(value = "SELECT * FROM gift g WHERE g.store = :storeName AND g.is_assigned = false", nativeQuery = true)
    List<Gift> findByStoreAndIsAssigned(String storeName);
    @Query(value = "SELECT SUM(g.price) FROM gift g WHERE g.is_assigned = false", nativeQuery = true)
    int sumByNotAssigned();
    @Query(value = "SELECT SUM(g.price) FROM gift g WHERE g.store = :storeName", nativeQuery = true)
    int sumByStoreName(String storeName);

    @Query(value = "SELECT * FROM gift g WHERE g.status = :status AND g.is_assigned = false" +
            "AND g.due_date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<Gift> findAllByNotAssignedAndStatusAndDueDateBetween(String status,LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT * FROM gift g WHERE g.status = 'stored' AND g.giver_id = :giverId AND g.due_date >= :today", nativeQuery = true)
    List<Gift> findAllByGiverAndStatusAndDueDateAfterOrToday(@Param("giverId") Long giverId, @Param("today") LocalDateTime today);
}
