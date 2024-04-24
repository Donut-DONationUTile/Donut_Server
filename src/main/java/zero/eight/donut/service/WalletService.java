package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.config.jwt.AuthUtils;
import zero.eight.donut.domain.Gift;
import zero.eight.donut.domain.Giver;
import zero.eight.donut.domain.enums.Store;
import zero.eight.donut.dto.wallet.WalletGiftInfoDto;
import zero.eight.donut.dto.wallet.WalletResponseDto;
import zero.eight.donut.exception.Success;
import zero.eight.donut.repository.GiftRepository;
import zero.eight.donut.repository.ReceiverRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WalletService {

    private final AuthUtils authUtils;
    private final ReceiverRepository receiverRepository;
    private final GiftRepository giftRepository;

    public ApiResponse<?> walletMain() { // 월렛 화면 조회
        // 조회 시각
        LocalDateTime now = LocalDateTime.now();
        // 기부자 정보
        Giver giver = authUtils.getGiver();
        // 수혜자 수
        int receiver = receiverRepository.countBy();
        // 기프티콘 총액
        double amount = 0d;
        // 월렛 기프티콘 리스트 조회
        List<Gift> targetList = giftRepository.findAllByGiverAndStatusAndDueDateAfterOrToday(giver.getId(), now);
        // 사용처별 기프티콘 개수
        Map<Store, Long> giftCountMap = countGiftsByStore(targetList);
        int cu = Math.toIntExact(giftCountMap.get(Store.CU));
        int gs25 = Math.toIntExact(giftCountMap.get(Store.GS25));
        int seveneleven = Math.toIntExact(giftCountMap.get(Store.SEVENELEVEN));

        // 기한별 기프티콘 리스트
        List<WalletGiftInfoDto> impendingList = new ArrayList<>();
        List<WalletGiftInfoDto> giftList = new ArrayList<>();

        for (Gift g : targetList) {
            // 기프티콘 총액 계산
            amount += g.getPrice();
            // 기프티콘 분류
            if (g.getDueDate().isBefore(LocalDateTime.now().plusDays(30))) { // 유효 기간 30일 이내
                impendingList.add(
                        WalletGiftInfoDto.builder()
                                .giftId(g.getId())
                                .days(Math.toIntExact(Duration.between(now, g.getDueDate()).toDaysPart()))
                                .store(g.getStore().getStore())
                                .dueDate(g.getDueDate())
                                .product(g.getProduct())
                                .price(g.getPrice())
                                .build()
                );
            }
            else { // 유효 기간 30일 이후
                giftList.add(
                        WalletGiftInfoDto.builder()
                                .giftId(g.getId())
                                .days(Math.toIntExact(Duration.between(now, g.getDueDate()).toDaysPart()))
                                .store(g.getStore().getStore())
                                .dueDate(g.getDueDate())
                                .product(g.getProduct())
                                .price(g.getPrice())
                                .build()
                );
            }
        }

        // response data 생성
        WalletResponseDto responseDto = WalletResponseDto.builder()
                .receiver(receiver)
                .amount(amount)
                .cu(cu)
                .gs25(gs25)
                .seveneleven(seveneleven)
                .impendingList(impendingList)
                .giftList(giftList)
                .build();

        return ApiResponse.success(Success.SUCCESS, responseDto);
    }

    // 사용처(store) 필드의 값마다 gift 개수 세기
    public Map<Store, Long> countGiftsByStore(List<Gift> giftList) {
        // Gift 객체 리스트를 사용처(store) 필드의 값마다 그룹화하여 각 그룹의 개수를 계산하여 Map으로 반환
        return giftList.stream()
                .collect(Collectors.groupingBy(Gift::getStore, Collectors.counting()));
    }
}
