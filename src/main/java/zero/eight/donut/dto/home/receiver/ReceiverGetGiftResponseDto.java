package zero.eight.donut.dto.home.receiver;

import lombok.Builder;
import lombok.Getter;
import zero.eight.donut.domain.enums.Status;
import zero.eight.donut.domain.enums.Store;

import java.time.LocalDateTime;

@Getter
public class ReceiverGetGiftResponseDto {
    private String product;
    private Integer price;
    private LocalDateTime dueDate;
    private String imgUrl;
    private Store store;
    private Status status;
    private Long boxId;

    @Builder
    public ReceiverGetGiftResponseDto(String product, Integer price, LocalDateTime dueDate, String imgUrl, Store store, Status status, Long boxId){
        this.product = product;
        this.price = price;
        this.dueDate = dueDate;
        this.imgUrl = imgUrl;
        this.store = store;
        this.status = status;
        this.boxId = boxId;

    }
}
