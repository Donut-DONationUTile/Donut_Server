package zero.eight.donut.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.config.firebase.FcmUtils;
import zero.eight.donut.domain.FcmToken;
import zero.eight.donut.domain.Gift;
import zero.eight.donut.domain.Giftbox;
import zero.eight.donut.domain.Receiver;
import zero.eight.donut.dto.fcm.FcmMemberDto;
import zero.eight.donut.dto.fcm.FcmTokenRequestDto;
import zero.eight.donut.exception.Success;
import zero.eight.donut.repository.FcmTokenRepository;
import zero.eight.donut.repository.GiftRepository;
import zero.eight.donut.repository.GiftboxRepository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FcmService {
    private final FcmUtils fcmUtils;
    private final FcmTokenRepository fcmTokenRepository;
    private final GiftRepository giftRepository;
    private final GiftboxRepository giftboxRepository;

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
    
    // 사용 기한 D-37 push 알림 (기부자)
    @Async
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void imminentWallet() throws FirebaseMessagingException {
        List<Gift> giftList = giftRepository.findAllByNotAssignedAndStoredAndAutoDonation(LocalDateTime.now().plusDays(37));
        for (Gift gift : giftList) {
            // 기프티콘의 giverId로 FCM 전송
            fcmUtils.sendMessage(gift.getGiver().getId(), "wallet: D-37", "Your item" + gift.getProduct() + "is expiring soon! It will be automatically donated.");
        }
    }

    // 사용 기한 D-7 push 알림 (수혜자)
    @Async
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void immminentGift() throws FirebaseMessagingException {
        // 7일 뒤 만료되는 꾸러미 찾기
        List<Giftbox> giftboxList = giftboxRepository.findAllByIsAvailableAndDueDate(LocalDateTime.now().plusDays(7));
        for (Giftbox giftbox : giftboxList) {
            // 수혜자 찾기
            Receiver receiver = giftbox.getReceiver();

            // 조회된 수혜자에게 FCM 전송
            fcmUtils.sendMessage(receiver.getId(), "giftbox: D-7", "Your gift box is expiring soon! You can use it at" + giftbox.getStore() + ".");
        }
    }
}
