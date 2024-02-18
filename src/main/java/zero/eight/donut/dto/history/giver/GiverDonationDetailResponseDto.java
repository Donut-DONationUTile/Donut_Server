package zero.eight.donut.dto.history.giver;

import lombok.Builder;
import lombok.Getter;
import zero.eight.donut.domain.enums.Status;
import zero.eight.donut.domain.enums.Store;

import java.time.LocalDateTime;

@Getter
@Builder
public class GiverDonationDetailResponseDto {
    private String product;
    private Integer amount;
    private LocalDateTime dueDate;
    private Store store;
    private String receiver;
    private Boolean isAssigned;
    private Status status;
    private String message;
    private LocalDateTime donateDate;
}
