package zero.eight.donut.dto.donation;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GiftboxRequestDto {
    String store; // 사용처
    int price; // 금액
}
