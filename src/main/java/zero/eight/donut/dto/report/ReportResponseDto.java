package zero.eight.donut.dto.report;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReportResponseDto {
    private Boolean isLast;
    @Builder
    public ReportResponseDto(Boolean isLast){
        this.isLast=isLast;
    }
}
