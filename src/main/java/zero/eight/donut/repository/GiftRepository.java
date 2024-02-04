package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zero.eight.donut.domain.Gift;

@Repository
public interface GiftRepository extends JpaRepository<Gift, Long> {
}
