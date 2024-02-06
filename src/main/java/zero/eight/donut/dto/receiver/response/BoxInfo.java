package zero.eight.donut.dto.receiver.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoxInfo {
    private Long boxId;
    private String store;
    private LocalDateTime dueDate;
    private Integer amount;

    @Builder
    public BoxInfo(Long boxId, String store, LocalDateTime dueDate, Integer amount){
        this.boxId = boxId;
        this.store = store;
        this.dueDate = dueDate;
        this.amount = amount;
    }
}
