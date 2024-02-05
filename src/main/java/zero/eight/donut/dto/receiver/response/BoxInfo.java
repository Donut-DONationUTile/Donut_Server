package zero.eight.donut.dto.receiver.response;

import lombok.Builder;

import java.time.LocalDateTime;

public class BoxInfo {
    private Long boxId;
    private String store;
    private LocalDateTime deadline;
    private Integer amount;

    @Builder
    public BoxInfo(Long boxId, String store, LocalDateTime deadline, Integer amount){
        this.boxId = boxId;
        this.store = store;
        this.deadline = deadline;
        this.amount = amount;
    }
}
