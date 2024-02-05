package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zero.eight.donut.domain.DonationInfo;

public interface DonationInfoRespository extends JpaRepository<DonationInfo, Long> {

    Double findByMonthAndYear(Integer month, Integer year);
}
