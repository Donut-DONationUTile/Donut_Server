package zero.eight.donut.dto.wallet;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class WalletGiftInfoDto {
    private long giftId; // 기프티콘 고유 ID
    private int days; // 디데이 남은 일수
    private String store; // 사용처
    private LocalDateTime dueDate; // 사용 기한
    private String product; // 상품명
    private int price; // 상품 금액
}
