package zero.eight.donut.dto.home.giver;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GiverHomeResponseDto {
    private Integer receivers;
    private Double donated;
    private Double need;

    @Builder
    public GiverHomeResponseDto(Integer receivers, Double donated, Double need){
        this.receivers = receivers;
        this.donated = donated;
        this.need = need;
    }
}
