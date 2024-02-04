package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zero.eight.donut.domain.Giver;

@Repository
public interface GiverRepository extends JpaRepository<Giver, Long> {
}
