package zero.eight.donut.dto.history.giver;

import lombok.Builder;
import lombok.Getter;
import zero.eight.donut.domain.enums.Status;

import java.time.LocalDateTime;

@Getter
@Builder
public class Donation {
    private Long giftId;
    private String product;
    private Integer price;
    private LocalDateTime dueDate;
    private Status status;
    private Boolean isAssigned;
}
