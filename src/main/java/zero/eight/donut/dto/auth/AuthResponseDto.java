package zero.eight.donut.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponseDto {

    String accesstoken; // 엑세스 토큰
    String refreshtoken; // 리프레시 토큰
    String name; // 닉네임(이메일 앞단)
}
