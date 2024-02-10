package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zero.eight.donut.domain.Receiver;

import java.util.Optional;

public interface ReceiverRepository extends JpaRepository<Receiver, Long> {
    Integer countBy();

    Optional<Receiver> findByName(String name);
}
