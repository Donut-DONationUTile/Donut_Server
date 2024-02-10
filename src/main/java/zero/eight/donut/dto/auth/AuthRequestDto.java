package zero.eight.donut.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AuthRequestDto {
    String id; // 수혜자 아이디
    String password; // 수혜자 비밀번호
}
