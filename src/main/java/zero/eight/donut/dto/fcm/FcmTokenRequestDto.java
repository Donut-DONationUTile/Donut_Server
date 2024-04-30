package zero.eight.donut.dto.fcm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class FcmTokenRequestDto {
    private final String token;
}
