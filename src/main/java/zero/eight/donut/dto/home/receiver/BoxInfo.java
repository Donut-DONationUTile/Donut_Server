package zero.eight.donut.dto.home.receiver;

import lombok.Builder;
import lombok.Getter;
import zero.eight.donut.domain.enums.Store;

import java.time.LocalDateTime;

@Getter
public class BoxInfo {
    private Long boxId;
    private Store store;
    private LocalDateTime dueDate;
    private Integer amount;

    @Builder
    public BoxInfo(Long boxId, Store store, LocalDateTime dueDate, Integer amount){
        this.boxId = boxId;
        this.store = store;
        this.dueDate = dueDate;
        this.amount = amount;
    }
}
