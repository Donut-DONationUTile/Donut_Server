package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zero.eight.donut.domain.Giftbox;
import zero.eight.donut.domain.enums.Store;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GiftboxRepository extends JpaRepository<Giftbox, Long> {
    Optional <Giftbox> findById(Long boxId);

    @Query("SELECT gb FROM Giftbox gb  WHERE gb.receiver.id = ?1")
    List<Giftbox> findAllByReceiverId(Long receiver_id);

    @Query("SELECT gb FROM Giftbox gb JOIN gb.giftList g WHERE g.id = ?1")
    Giftbox findByGiftId(Long giftId);

    @Query("SELECT gb FROM Giftbox gb  WHERE gb.receiver.id = ?1 and gb.isAvailable = true")
    List<Giftbox> findAllByReceiverIdAndIsAvailable(Long receiver_id);

    @Query(value = "SELECT * FROM Giftbox gb WHERE gb.isAvailable = true AND gb.dueDate = :endDate", nativeQuery = true)
    List<Giftbox> findAllByIsAvailableAndDueDate(@Param("endDate") LocalDateTime endDate);

//    @Query("SELECT SUM(gb.amount) FROM Giftbox gb WHERE gb.store = ?1")
//    Integer getSumByStore(Store store);

    @Query("SELECT gb.store, SUM(gb.amount) FROM Giftbox gb WHERE gb.receiver.id = ?1 GROUP BY gb.store ")
    List<Object[]> findGiftboxSumsByStore(@Param("receiverId") Long receiverId);
    default Map<Store, Integer> getGiftboxSumsByStore(Long receiverId) {
        List<Object[]> results = findGiftboxSumsByStore(receiverId);
        Map<Store, Integer> storeGiftBoxSums = new HashMap<>();
        for (Object[] result : results) {
            storeGiftBoxSums.put((Store) result[0], ((Long) result[1]).intValue());
        }
        return storeGiftBoxSums;
    }
}
