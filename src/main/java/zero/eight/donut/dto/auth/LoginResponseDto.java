package zero.eight.donut.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponseDto {

    String accesstoken; // 엑세스 토큰
    String refreshtoken; // 리프레시 토큰
    String name; // 닉네임(이메일 앞단)
}
