package zero.eight.donut.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zero.eight.donut.domain.Gift;
import zero.eight.donut.domain.Giver;
import zero.eight.donut.domain.Receiver;
import zero.eight.donut.domain.Report;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportedRequestDto {
    private Long giftId;
    public Report toEntity(Giver giver, Receiver receiver, Gift gift){
        return Report.builder()
                .giver(giver)
                .receiver(receiver)
                .gift(gift)
                .build();
    }
}
