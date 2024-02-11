package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import zero.eight.donut.domain.Donation;
import zero.eight.donut.domain.Giftbox;

import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    @Query("SELECT d.sum FROM Donation d JOIN d.giver g WHERE g.id = ?1")
    Long getSumByGiverId(Long giverId);
}
