package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zero.eight.donut.domain.Benefit;
import zero.eight.donut.domain.Receiver;

import java.util.Optional;

@Repository
public interface BenefitRepository extends JpaRepository<Benefit, Long> {
    Optional<Benefit> findByReceiver(Receiver receiver);
}
