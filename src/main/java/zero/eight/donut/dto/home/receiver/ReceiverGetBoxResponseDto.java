package zero.eight.donut.dto.home.receiver;

import lombok.Builder;
import lombok.Getter;
import zero.eight.donut.domain.enums.Store;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ReceiverGetBoxResponseDto {
    private Store store;
    private Integer amount;
    private LocalDateTime dueDate;
    private List<GiftInfo> giftList;

    @Builder
    public ReceiverGetBoxResponseDto(Store store, Integer amount, LocalDateTime dueDate, List<GiftInfo> giftList){
        this.store = store;
        this.amount = amount;
        this.dueDate = dueDate;
        this.giftList = giftList;
    }
}
