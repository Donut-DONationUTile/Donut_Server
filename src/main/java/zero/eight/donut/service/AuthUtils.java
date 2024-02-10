package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import zero.eight.donut.domain.Giver;
import zero.eight.donut.domain.Receiver;
import zero.eight.donut.dto.auth.Role;
import zero.eight.donut.repository.GiverRepository;
import zero.eight.donut.repository.ReceiverRepository;

@RequiredArgsConstructor
@Component
public class AuthUtils {

    // 현재 요청된 사용자 정보가 필요한 경우, AuthUtils.clss의 함수를 호출하면 됨

    private final GiverRepository giverRepository;
    private final ReceiverRepository receiverRepository;

    public Giver getGiver() {
        return giverRepository.findByEmail(getCurrentUserEmail()).get();
    }

    public Receiver getReceiver() {
        return receiverRepository.findByName(getCurrentUserEmail()).get();
    }

    public Authentication getAuthentication() {
        // SecurityContext에서 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

    public Object getPrincipal() {
        // 현재 사용자의 principal 가져오기
        return getAuthentication().getPrincipal();
    }

    public String getCurrentUserEmail() {
        Object principalObject = getPrincipal();

        // principal이 UserDetails 인스턴스인지 확인
        if (principalObject instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principalObject;

            // UserDetails 인스턴스에서 email 획득
            return userDetails.getUsername(); // 이메일은 UserDetails의 username에 저장되어 있음
        }
        return null;
    }

    public Role getCurrentUserRole() {
        Object principalObject = getPrincipal();

        // principal이 UserDetails 인스턴스인지 확인
        if (principalObject instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principalObject;

            // UserDetails 인스턴스에서 Role 획득
            return Role.valueOf(userDetails.getAuthorities().toString());
        }

        return null;
    }
}
