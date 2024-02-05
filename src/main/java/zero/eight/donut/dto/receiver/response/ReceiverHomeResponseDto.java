package zero.eight.donut.dto.receiver.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ReceiverHomeResponseDto {
    private Boolean availability;
    private Long amount;
    private List<String> boxList;

    @Builder
    public ReceiverHomeResponseDto(Boolean availability, Long amount, List<String> boxList){
        this.availability = availability;
        this.amount = amount;
        this.boxList =boxList;

    }
}
