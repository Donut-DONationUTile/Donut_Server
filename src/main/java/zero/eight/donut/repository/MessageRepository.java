package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zero.eight.donut.domain.Message;

public interface MessageRepository extends JpaRepository<Message,Long> {
    Message findByGiftId(Long giftId);

    @Query(value = "SELECT COUNT(*) FROM message m WHERE m.giver_id = :giverId", nativeQuery = true)
    int countByGiverId(@Param("giverId") Long giverId);
}
