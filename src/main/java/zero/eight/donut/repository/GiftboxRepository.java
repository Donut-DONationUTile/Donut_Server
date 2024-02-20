package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zero.eight.donut.domain.Giftbox;
import zero.eight.donut.domain.enums.Store;

import java.util.List;
import java.util.Optional;

public interface GiftboxRepository extends JpaRepository<Giftbox, Long> {
    Optional <Giftbox> findById(Long boxId);

    @Query("SELECT gb FROM Giftbox gb  WHERE gb.receiver.id = ?1")
    List<Giftbox> findAllByReceiverId(Long receiver_id);

    @Query("SELECT gb FROM Giftbox gb JOIN gb.giftList g WHERE g.id = ?1")
    Giftbox findByGiftId(Long giftId);

    @Query("SELECT gb FROM Giftbox gb  WHERE gb.receiver.id = ?1 and gb.isAvailable = true")
    List<Giftbox> findAllByReceiverIdAndIsAvailable(Long receiver_id);

    @Query("SELECT SUM(gb.amount) FROM Giftbox gb " +
            "WHERE gb.store = ?1 AND gb.isAvailable = true AND gb.receiver.id = ?2")
    Integer getSumByStore(Store store, Long id);
}
