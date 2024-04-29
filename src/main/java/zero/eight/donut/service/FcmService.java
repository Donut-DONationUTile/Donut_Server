package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.config.jwt.AuthUtils;
import zero.eight.donut.domain.FcmToken;
import zero.eight.donut.domain.Giver;
import zero.eight.donut.domain.Receiver;
import zero.eight.donut.dto.auth.Role;
import zero.eight.donut.dto.fcm.FcmMemberDto;
import zero.eight.donut.dto.fcm.FcmTokenRequestDto;
import zero.eight.donut.exception.Error;
import zero.eight.donut.repository.FcmTokenRepository;
import zero.eight.donut.repository.GiverRepository;
import zero.eight.donut.repository.ReceiverRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FcmService {
    private final AuthUtils authUtils;
    private final GiverRepository giverRepository;
    private final ReceiverRepository receiverRepository;
    private final FcmTokenRepository fcmTokenRepository;

    public ApiResponse<?> registerFcmToken(FcmTokenRequestDto requestDto) throws Exception {
        final String token = requestDto.getToken();
        FcmMemberDto member = getMemberDto();

        fcmTokenRepository.findByMemberId(member.getId())
                .ifPresentOrElse(
                        it -> it.updateToken(token, member.getRole()),
                        () -> fcmTokenRepository.save(FcmToken.builder()
                                .token(token)
                                .memberId(member.getId())
                                .build())
                );
    }

    private FcmMemberDto getMemberDto() throws Exception {
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
}
