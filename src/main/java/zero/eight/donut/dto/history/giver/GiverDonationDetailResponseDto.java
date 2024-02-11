package zero.eight.donut.dto.history.giver;

import lombok.Builder;
import lombok.Getter;
import zero.eight.donut.domain.enums.Status;

import java.time.LocalDateTime;

@Getter
@Builder
public class GiverDonationDetailResponseDto {
    private String product;
    private Integer amount;
    private LocalDateTime dueDate;
    private String store;
    private String receiver;
    private Boolean isAssigned;
    private Status status;
    private String message;
}
