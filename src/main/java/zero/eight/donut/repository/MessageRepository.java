package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zero.eight.donut.domain.Message;

public interface MessageRepository extends JpaRepository<Message,Long> {
    Message findByGiftId(Long giftId);
}
