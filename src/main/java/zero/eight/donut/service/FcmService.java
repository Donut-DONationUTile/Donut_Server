package zero.eight.donut.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.config.firebase.FcmUtils;
import zero.eight.donut.domain.*;
import zero.eight.donut.dto.fcm.FcmMemberDto;
import zero.eight.donut.dto.fcm.FcmTokenRequestDto;
import zero.eight.donut.exception.Success;
import zero.eight.donut.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FcmService {
    private final FcmUtils fcmUtils;
    private final FcmTokenRepository fcmTokenRepository;
    private final GiftRepository giftRepository;
    private final GiftboxRepository giftboxRepository;
    private final GiverRepository giverRepository;
    private final ReceiverRepository receiverRepository;

    public ApiResponse<?> registerFcmToken(FcmTokenRequestDto requestDto) throws Exception {
        final String token = requestDto.getToken();
        FcmMemberDto member = fcmUtils.getMemberDto();

        fcmTokenRepository.findByMemberId(member.getId())
                .ifPresentOrElse(
                        it -> fcmTokenRepository.save(it.updateToken(token)),
                        () -> fcmTokenRepository.save(FcmToken.builder()
                                .token(token)
                                .memberId(member.getId())
                                .role(member.getRole())
                                .build())
                );

        return ApiResponse.success(Success.CREATE_FCM_TOKEN_SUCCESS);
    }

    // 사용 기한 D-37 push 알림 (기부자)
    @Async
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public List<String> imminentWallet() throws FirebaseMessagingException {
        List<Gift> giftList = giftRepository.findAllByNotAssignedAndStoredAndAutoDonation(LocalDateTime.now().plusDays(37));
        List<String> fcmList = new ArrayList<>();

        for (Gift gift : giftList) {
            // 기프티콘의 giverId로 FCM 전송
            fcmUtils.sendMessage(gift.getGiver().getId(), "wallet: D-37", "Your item" + gift.getProduct() + "is expiring soon! It will be automatically donated.");
            fcmList.add("fcmReceiver: " + gift.getGiver().getName() + "(ROLE_GIVER), fcm title: wallet: D-37, fcm body: Your item" + gift.getProduct() + "is expiring soon! It will be automatically donated.");
        }

        return fcmList;
    }

    // 사용 기한 D-7 push 알림 (수혜자)
    @Async
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public List<String> immminentGift() throws FirebaseMessagingException {
        // 7일 뒤 만료되는 꾸러미 찾기
        List<Giftbox> giftboxList = giftboxRepository.findAllByIsAvailableAndDueDate(LocalDateTime.now().plusDays(7));
        List<String> fcmList = new ArrayList<>();

        for (Giftbox giftbox : giftboxList) {
            // 수혜자 찾기
            Receiver receiver = giftbox.getReceiver();

            // 조회된 수혜자에게 FCM 전송
            fcmUtils.sendMessage(receiver.getId(), "giftbox: D-7", "Your gift box is expiring soon! You can use it at" + giftbox.getStore() + ".");
            fcmList.add("fcmReceiver: " + giftbox.getReceiver().getName() + "(ROLE_RECEIVER), fcm title: giftbox: D-7, fcm body: Your gift box is expiring soon! You can use it at" + giftbox.getStore() + ".");
        }

        return fcmList;
    }

    public String mock37(String email, String product) throws FirebaseMessagingException {
        Giver giver = giverRepository.findByEmail(email).orElseThrow();
        fcmUtils.sendMessage(giver.getId(), "[DONUT] D-37", "Your item" + product + "is expiring soon! It will be automatically donated.");
        return "fcmReceiver: " + email + "(ROLE_GIVER), fcm title: wallet: D-37, fcm body: Your item" + product + "is expiring soon! It will be automatically donated.";
    }

    public String mock30(String email, String product) throws FirebaseMessagingException {
        Giver giver = giverRepository.findByEmail(email).orElseThrow();
        fcmUtils.sendMessage(giver.getId(), "[DONUT] D-30", "Your item" + product + "is donated now!");
        return "fcmReceiver: " + giver.getName() + "(ROLE_GIVER), fcm title: wallet: D-30, fcm body: Your item" + product + "is donated now!";
    }

    public String mock7(String name, String store) throws FirebaseMessagingException {
        Receiver receiver = receiverRepository.findByName(name).orElseThrow();
        fcmUtils.sendMessage(receiver.getId(), "[DONUT] D-7", "Your gift box is expiring soon! You can use it at" + store + ".");
        return "fcmReceiver: " + receiver.getName() + "(ROLE_RECEIVER), fcm title: giftbox: D-7, fcm body: Your gift box is expiring soon! You can use it at" + store + ".";
    }
}