package zero.eight.donut.dto.mypage;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StatsDto {
    private int unreceived; // 기부한 것 중 할당 안된 것
    private int received; // 기부한 것 중 할당 된 것
    private int msg; // 받은 메세지 개수
}
