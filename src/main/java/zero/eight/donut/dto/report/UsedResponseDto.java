package zero.eight.donut.dto.report;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UsedResponseDto {
    private Boolean isLast;
    @Builder
    public UsedResponseDto(Boolean isLast){
        this.isLast=isLast;
    }
}
