package zero.eight.donut.dto;

import lombok.Builder;
import lombok.Getter;
import zero.eight.donut.domain.Gift;

import java.util.List;

@Getter
@Builder
public class GiftAssignDto {
    List<Gift> assignedList; // 선택된 기프티콘 id 리스트
    Integer assignedValue; // 할당 금액 총액
}
