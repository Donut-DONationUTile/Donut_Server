package zero.eight.donut.dto.report;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReportedResponseDto {
    private Long reportId;
    private Long giftId;
    private Boolean isLast;
    @Builder
    public ReportedResponseDto(Long reportId, Long giftId, Boolean isLast){
        this.reportId = reportId;
        this.giftId = giftId;
        this.isLast=isLast;
    }
}
