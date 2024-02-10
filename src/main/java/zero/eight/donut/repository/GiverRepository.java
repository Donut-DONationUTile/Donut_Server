package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zero.eight.donut.domain.Giver;

import java.util.Optional;

public interface GiverRepository extends JpaRepository <Giver, Long> {
    Optional<Giver> findByEmail(String email);
}
