package zero.eight.donut.dto.receiver.response;

import lombok.Builder;
import lombok.Getter;
import zero.eight.donut.domain.enums.Status;

import java.time.LocalDateTime;

@Getter
public class ReceiverGetGiftResponseDto {
    private String product;
    private Integer amount;
    private LocalDateTime dueDate;
    private String imgUrl;
    private String store;
    private Status status;
    private Long boxId;

    @Builder
    public ReceiverGetGiftResponseDto(String product, Integer amount, LocalDateTime dueDate, String imgUrl, String store, Status status, Long boxId){
        this.product = product;
        this.amount = amount;
        this.dueDate = dueDate;
        this.imgUrl = imgUrl;
        this.store = store;
        this.status = status;
        this.boxId = boxId;

    }
}
