package zero.eight.donut.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberDto {
    String email; // 이메일
    Role role; // 멤버 역할(ROLE_GIVER/ROLE_RECEIVER)
}
