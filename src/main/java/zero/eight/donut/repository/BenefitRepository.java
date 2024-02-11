package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zero.eight.donut.domain.Benefit;

public interface BenefitRepository extends JpaRepository<Benefit, Long> {
    @Query("SELECT b FROM Benefit b  WHERE b.receiver.id =?1 AND b.year = ?2 AND b.month= ?3")
    Benefit findByReceiverIdAndThisMonth(Long receiverId, Integer year, Integer month);
}
