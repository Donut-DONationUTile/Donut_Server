package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zero.eight.donut.domain.Receiver;

public interface ReceiverRepository extends JpaRepository<Receiver, Long> {
    Integer countBy();
}
