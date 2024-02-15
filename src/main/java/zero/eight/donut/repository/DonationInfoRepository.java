package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import zero.eight.donut.domain.DonationInfo;

@Repository
public interface DonationInfoRepository extends JpaRepository<DonationInfo, Long> {
    @Query("SELECT d.sum FROM DonationInfo d WHERE d.month = ?1 AND d.year= ?2")
    Long findByMonthAndYear(Integer month, Integer year);

    @Query("SELECT d FROM DonationInfo d WHERE d.month = ?1 AND d.year= ?2")
    DonationInfo findDonationInfoByMonthAndYear(Integer month, Integer year);
}
