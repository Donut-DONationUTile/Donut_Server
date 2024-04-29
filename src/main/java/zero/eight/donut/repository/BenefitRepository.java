package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zero.eight.donut.domain.Benefit;
import zero.eight.donut.domain.Receiver;

import java.util.Optional;

public interface BenefitRepository extends JpaRepository<Benefit, Long> {
    Optional<Benefit> findByReceiver(Receiver receiver);

    @Query("SELECT b FROM Benefit b  WHERE b.receiver.id =?1 AND b.year = ?2 AND b.month= ?3")
    Benefit findByReceiverIdAndThisMonth(Long receiverId, Integer year, Integer month);

    @Query(value = "SELECT SUM(b.sum) FROM benefit b WHERE b.receiver_id = :receiverId", nativeQuery = true)
    Integer sumBenefitByReceiverId(@Param("receiverId") Long receiverId);
}
