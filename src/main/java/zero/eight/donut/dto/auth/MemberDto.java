package zero.eight.donut.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDto {
    String name; // 기부자: 이메일, 수혜자: 아이디
    Role role; // 멤버 역할(ROLE_GIVER/ROLE_RECEIVER)
}
