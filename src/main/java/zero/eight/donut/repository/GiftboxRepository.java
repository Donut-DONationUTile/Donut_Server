package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import zero.eight.donut.domain.Giftbox;

import java.util.List;

@Repository
public interface GiftboxRepository extends JpaRepository<Giftbox, Long> {
    @Query("SELECT gb FROM Giftbox gb JOIN gb.receiver r WHERE r.id = ?1 and gb.amount > 0")
    List<Giftbox> findAllByReceiverId(Long receiver_id);

    @Query("SELECT gb FROM Giftbox gb JOIN gb.giftList g WHERE g.id = ?1")
    Giftbox findByGiftId(Long giftId);
}
