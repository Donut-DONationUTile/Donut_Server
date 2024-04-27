package zero.eight.donut.dto.mypage;

import lombok.Builder;

@Builder
public class GiverInfoResponseDto {
    private int years; // 기부 기간(연)
    private double donation; // 총 기부 금액
    private StatsDto stats; // 통계
}
