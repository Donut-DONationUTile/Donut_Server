package zero.eight.donut.dto.ranking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RankingByNumberResponseDto {
    int rank; // 등수
    String name; // 기부자 아이디
    Long number; // 기부 횟수
}
