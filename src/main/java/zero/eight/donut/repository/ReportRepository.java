package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zero.eight.donut.domain.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
