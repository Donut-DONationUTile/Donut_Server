package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zero.eight.donut.domain.Giver;

public interface GiverRepository extends JpaRepository <Giver, Long> {
}
