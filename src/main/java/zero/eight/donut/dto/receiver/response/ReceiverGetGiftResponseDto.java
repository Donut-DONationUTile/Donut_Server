package zero.eight.donut.dto.receiver.response;

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
}
