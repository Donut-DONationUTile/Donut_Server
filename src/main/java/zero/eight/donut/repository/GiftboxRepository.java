package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import zero.eight.donut.domain.Giftbox;

import java.util.List;

@Repository
public interface GiftboxRepository extends JpaRepository<Giftbox, Long> {
    //@Query("SELECT gb.sum FROM Giftbox gb WHERE gb.receiver = ?1 AND gb.= ?2")
    List<Giftbox> findAllByReceiverId(Long receiver_id);
}
