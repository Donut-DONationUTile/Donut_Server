package zero.eight.donut.config.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zero.eight.donut.config.jwt.AuthUtils;
import zero.eight.donut.domain.FcmToken;
import zero.eight.donut.domain.Giver;
import zero.eight.donut.domain.Receiver;
import zero.eight.donut.dto.auth.Role;
import zero.eight.donut.dto.fcm.FcmMemberDto;
import zero.eight.donut.exception.Error;
import zero.eight.donut.repository.FcmTokenRepository;
import zero.eight.donut.repository.GiverRepository;
import zero.eight.donut.repository.ReceiverRepository;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class FcmUtils {

    private final AuthUtils authUtils;
    private final GiverRepository giverRepository;
    private final ReceiverRepository receiverRepository;
    private final FcmTokenRepository fcmTokenRepository;

    private FirebaseMessaging firebaseMessaging;



    public FcmMemberDto getMemberDto() throws Exception {
        if (authUtils.getCurrentUserRole().equals(Role.ROLE_GIVER)) {
            Giver giver = giverRepository.findByEmail(authUtils.getCurrentUserEmail()).orElseThrow(
                    () -> new Exception(Error.USERNAME_NOT_FOUND_EXCEPTION.getMessage())
            );
            return FcmMemberDto.builder()
                    .id(giver.getId())
                    .role(Role.ROLE_GIVER)
                    .build();
        }
        else {
            Optional<Receiver> optionalReceiver = receiverRepository.findByName(authUtils.getCurrentUserEmail());
            if (optionalReceiver.isEmpty()) {
                throw new Exception(Error.USERNAME_NOT_FOUND_EXCEPTION.getMessage());
            }

            return FcmMemberDto.builder()
                    .id(optionalReceiver.get().getId())
                    .role(Role.ROLE_RECEIVER)
                    .build();
        }
    }

    // FCM 메세지 전송
    public String sendMessage(Long memberId, String title, String body) throws FirebaseMessagingException {
        // 알림 수신자의 FCM 토큰 조회
        String fcmToken = getFcmToken(memberId);
        // FCM 메세지 생성
        Message message = makeMessage(fcmToken, title, body);
        // FCM 발신
        return firebaseMessaging.send(message);
    }

    public String getFcmToken(Long memberId) {
        Optional<FcmToken> fcmToken = fcmTokenRepository.findByMemberId(memberId);

        if (fcmToken.isEmpty()) {
            try {
                throw new Exception(Error.FCM_TOKEN_NOT_FOUND_EXCEPTION.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return fcmToken.get().getToken();
    }

    public Message makeMessage(String targetToken, String title, String body) {
        // Notification 객체는 모바일 환경에서 제목과 본문을 표시
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        return Message.builder()
                .setNotification(notification)
                .setToken(targetToken)
                .build();
    }
}
