package zero.eight.donut.config.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class Token {

    private String accessToken; // 액세스 토큰
    private String refreshToken; // 리프레시 토큰
    private String name; // 사용자 아이디

}
