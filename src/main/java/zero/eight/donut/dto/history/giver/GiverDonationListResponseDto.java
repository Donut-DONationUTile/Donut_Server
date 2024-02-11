package zero.eight.donut.dto.history.giver;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GiverDonationListResponseDto {
    private Integer period;
    private Long totalAmount;
    private Long unreceived;
    private Long received;
    private Long msg;
    private List<Donation> donationList;
}
