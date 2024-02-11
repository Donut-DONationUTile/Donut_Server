package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zero.eight.donut.domain.Gift;

import java.time.LocalDateTime;
import java.util.List;

public interface GiftRepository extends JpaRepository<Gift, Long> {
    List<Gift> findAllByGiftboxId(Long giftbox_id);

    List<Gift> findAllByGiverIdAndCreatedAtBetween(Long giverId, LocalDateTime startDate, LocalDateTime endDate);

}
