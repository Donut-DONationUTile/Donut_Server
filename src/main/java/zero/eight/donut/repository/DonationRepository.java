package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zero.eight.donut.domain.Donation;

public interface DonationRepository extends JpaRepository<Donation, Long> {
}
