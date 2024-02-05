package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zero.eight.donut.domain.Giftbox;

import java.util.List;

@Repository
public interface GiftboxRepository extends JpaRepository<Giftbox, Long> {
    List<Giftbox> findAllByReceiverId(Long receiver_id);
}
