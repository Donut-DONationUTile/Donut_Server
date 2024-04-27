package zero.eight.donut.dto.wallet;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WalletResponseDto {
    private int receiver; // 수혜자 수
    private double amount; // 기프티콘 총액
    private int cu; // 보유 기프티콘 개수(CU)
    private int gs25; // 보유 기프티콘 개수(GS25)
    private int seveneleven; // 보유 기프티콘 개수(7eleven)
    private List<WalletGiftInfoResponseDto> impendingList; // 임박 기프티콘 리스트
    private List<WalletGiftInfoResponseDto> giftList; // 일반 기프티콘 리스트
}
