package zero.eight.donut.dto.home.receiver;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ReceiverHomeResponseDto {
    private Boolean availability;
    private Long amount;
    private List<BoxInfo> boxList;

    @Builder
    public ReceiverHomeResponseDto(Boolean availability, Long amount, List<BoxInfo> boxList){
        this.availability = availability;
        this.amount = amount;
        this.boxList =boxList;

    }
}
