package zero.eight.donut.dto.receiver.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GiftInfo {
    private Long giftId;
    private String product;
    private Integer price;
    private LocalDateTime dueDate;
    private String isUsed;

    @Builder
    public GiftInfo(Long giftId, String product, Integer price, LocalDateTime dueDate, String isUsed){
        this.giftId = giftId;
        this.product = product;
        this.price = price;
        this.dueDate = dueDate;
        this.isUsed = isUsed;
    }
}
