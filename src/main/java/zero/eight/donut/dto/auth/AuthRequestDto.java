package zero.eight.donut.dto.auth;

import lombok.Getter;

@Getter
public class AuthRequestDto {
    String id; // 수혜자 아이디
    String password; // 수혜자 비밀번호
}
