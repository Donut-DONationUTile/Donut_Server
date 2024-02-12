package zero.eight.donut.dto.donation;

import lombok.Builder;
import lombok.Getter;
import zero.eight.donut.domain.enums.Store;

@Getter
@Builder
public class GiftboxRequestDto {
    Store store; // 사용처
    int price; // 금액
}
