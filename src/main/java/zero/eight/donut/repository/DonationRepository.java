package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zero.eight.donut.domain.Donation;
import zero.eight.donut.domain.Giver;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    @Query("SELECT d.sum FROM Donation d JOIN d.giver g WHERE g.id = ?1")
    Long getSumByGiverId(Long giverId);

    Donation findByGiver(Giver giver);
}
