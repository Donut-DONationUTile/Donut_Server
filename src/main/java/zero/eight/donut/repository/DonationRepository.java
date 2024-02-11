package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zero.eight.donut.domain.Donation;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
}
