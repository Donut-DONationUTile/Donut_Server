package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.config.jwt.AuthUtils;
import zero.eight.donut.domain.Benefit;
import zero.eight.donut.domain.Gift;
import zero.eight.donut.domain.Giftbox;
import zero.eight.donut.domain.Receiver;
import zero.eight.donut.dto.auth.Role;
import zero.eight.donut.dto.history.receiver.ReceivedGift;
import zero.eight.donut.dto.history.receiver.ReceiverDonationResponseDto;
import zero.eight.donut.exception.Error;
import zero.eight.donut.exception.Success;
import zero.eight.donut.repository.BenefitRepository;
import zero.eight.donut.repository.GiftRepository;
import zero.eight.donut.repository.GiftboxRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistoryReceiverService {
    //월별 최대 지원금액
    static Integer maxAccount = 100000;

    private final AuthUtils authUtils;
    private final GiftboxRepository giftboxRepository;
    private final GiftRepository giftRepository;
    private final BenefitRepository benefitRepository;

    public ApiResponse<?> receivedDonation(){
        //수혜자 여부 조회
        if (!authUtils.getCurrentUserRole().equals(Role.ROLE_RECEIVER)) {
            return ApiResponse.failure(Error.NOT_AUTHENTICATED_EXCEPTION);
        }
        Receiver receiver = authUtils.getReceiver();

        //이번 달의 수혜 정보
        Integer thisYear = LocalDateTime.now().getYear();
        Integer thisMonth = LocalDateTime.now().getMonthValue();

        Benefit benefit = benefitRepository.findByReceiverIdAndThisMonth(receiver.getId(), thisYear, thisMonth)
                .orElseGet(() -> createNewBenefit(receiver));

        //수혜 기프티콘 목록
        List<Giftbox> giftboxList = giftboxRepository.findAllByReceiverId(receiver.getId());
        List<ReceivedGift> giftList = new ArrayList<>();
        for (Giftbox giftbox : giftboxList) {
            List<Gift> allGiftList = giftRepository.findAllByGiftboxId(giftbox.getId());
            for(Gift gift: allGiftList){
                giftList.add(ReceivedGift.builder()
                                .giftId(gift.getId())
                                .product(gift.getProduct())
                                .price(gift.getPrice())
                                .dueDate(gift.getDueDate())
                                .isUsed(gift.getStatus())
                        .build());
            }
        }

        ReceiverDonationResponseDto responseDto = ReceiverDonationResponseDto.builder()
                .amount(maxAccount - benefit.getSum())
                .availability(benefit.getAvailability())
                .giftList(giftList)
                .build();

        return ApiResponse.success(Success.GET_HISTORY_RECEIVER_BENEFIT_SUCCESS, responseDto);
    }
    private Benefit createNewBenefit(Receiver receiver){
        Benefit newBenefit = Benefit.builder()
                .receiver(receiver)
                .sum(0)
                .month(LocalDateTime.now().getMonthValue())
                .year(LocalDateTime.now().getYear())
                .availability(true)
                .build();
        benefitRepository.save(newBenefit);
        return newBenefit;
    }
}
