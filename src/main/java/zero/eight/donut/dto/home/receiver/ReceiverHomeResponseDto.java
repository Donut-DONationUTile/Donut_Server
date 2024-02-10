package zero.eight.donut.dto.home.receiver;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ReceiverHomeResponseDto {
    private Boolean availability;
    private Long amount;
    private Integer cu;
    private Integer gs25;
    private Integer sevenEleven;
    private List<BoxInfo> boxList;

    @Builder
    public ReceiverHomeResponseDto(Boolean availability, Long amount, Integer cu, Integer gs25, Integer sevenEleven, List<BoxInfo> boxList){
        this.availability = availability;
        this.amount = amount;
        this.cu = cu;
        this.gs25 = gs25;
        this.sevenEleven = sevenEleven;
        this.boxList =boxList;

    }
}
