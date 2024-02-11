package zero.eight.donut.dto.donation;

import lombok.Builder;
import lombok.Getter;
import zero.eight.donut.domain.Gift;

@Getter
@Builder
public class GiftValueDto {
    private Gift gift;
    private Integer price;
    private long epochMilli;
}
