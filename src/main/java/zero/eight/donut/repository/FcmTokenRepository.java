package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zero.eight.donut.domain.FcmToken;

import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    Optional<FcmToken> findByMemberId(Long memberId);
}