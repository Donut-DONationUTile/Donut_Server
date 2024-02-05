package zero.eight.donut.dto.receiver.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ReceiverGetBoxResponseDto {
    private String store;
    private Integer amount;
    private LocalDateTime dueDate;
    private List<GiftInfo> giftList;

    @Builder
    public ReceiverGetBoxResponseDto(String store, Integer amount, LocalDateTime dueDate, List<GiftInfo> giftList){
        this.store = store;
        this.amount = amount;
        this.dueDate = dueDate;
        this.giftList = giftList;
    }
}
