package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zero.eight.donut.domain.Receiver;

@Repository
public interface ReceiverRepository extends JpaRepository<Receiver, Long> {
}
