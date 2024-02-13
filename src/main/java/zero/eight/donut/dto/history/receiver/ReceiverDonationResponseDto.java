package zero.eight.donut.dto.history.receiver;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReceiverDonationResponseDto {
    private Integer amount;
    private Boolean availability;
    private List<ReceivedGift> receivedGiftList;
}
