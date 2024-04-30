package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.config.firebase.FcmUtils;
import zero.eight.donut.domain.FcmToken;
import zero.eight.donut.domain.Gift;
import zero.eight.donut.dto.fcm.FcmMemberDto;
import zero.eight.donut.dto.fcm.FcmTokenRequestDto;
import zero.eight.donut.exception.Success;
import zero.eight.donut.repository.FcmTokenRepository;
import zero.eight.donut.repository.GiftRepository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FcmService {
    private final FcmUtils fcmUtils;
    private final FcmTokenRepository fcmTokenRepository;
    private final GiftRepository giftRepository;

    public ApiResponse<?> registerFcmToken(FcmTokenRequestDto requestDto) throws Exception {
        final String token = requestDto.getToken();
        FcmMemberDto member = fcmUtils.getMemberDto();

        fcmTokenRepository.findByMemberId(member.getId())
                .ifPresentOrElse(
                        it -> it.updateToken(token, member.getRole()),
                        () -> fcmTokenRepository.save(FcmToken.builder()
                                .token(token)
                                .memberId(member.getId())
                                .build())
                );

        return ApiResponse.success(Success.CREATE_FCM_TOKEN_SUCCESS);
    }
    
    // 사용 기한 D-37 push 알림
    @Async
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void imminentWallet(){
        List<Gift> giftList = giftRepository.findAllByNotAssignedAndStatusAndDueDateBetween( "STORED", LocalDateTime.now(), LocalDateTime.now().minusDays(37));
        for (Gift gift : giftList) {
            // 기프티콘의 giverId로 FCM 메세지 전송
        }
    }
}
