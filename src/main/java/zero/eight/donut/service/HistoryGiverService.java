package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.domain.Gift;
import zero.eight.donut.domain.Giver;
import zero.eight.donut.dto.history.giver.Donation;
import zero.eight.donut.dto.history.giver.GiverDonationListResponseDto;
import zero.eight.donut.exception.Success;
import zero.eight.donut.repository.DonationRepository;
import zero.eight.donut.repository.GiftRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryGiverService {

    private final AuthUtils authUtils;
    private final DonationRepository donationRepository;
    private final GiftRepository giftRepository;

    @Transactional
    public ApiResponse<?> getDonationList(LocalDateTime donateDate){
        Giver giver = authUtils.getGiver();

        //기부한 기간 계산
        Integer period = Period.between(giver.getCreatedAt().toLocalDate(), LocalDate.now()).getYears();

        //기부 총액
        Long totalAmount = donationRepository.getSumByGiverId(giver.getId());

        //한달 간 기부한 gift 목록 조회
        YearMonth yearMonth = YearMonth.from(donateDate);
        List<Gift> giftList = giftRepository.findAllByGiverIdAndCreatedAtBetween(giver.getId(), donateDate, yearMonth.atEndOfMonth().atStartOfDay());

        Long unreceived = 0L, received = 0L, msg = 0L;
        List<Donation> donationList = new ArrayList<>();

        for (Gift gift : giftList) {
            //received, unreceived, msg 구하기
            if (!gift.getIsAssigned()) { unreceived++; }
            else { received++; }
            if (gift.getIsMsgReceived()) { msg++; }

            //gift 개별 정보 가져오기
            donationList.add(Donation.builder()
                    .giftId(gift.getId())
                    .product(gift.getProduct())
                    .price(gift.getPrice())
                    .dueDate(gift.getDueDate())
                    .status(gift.getStatus())
                    .isAssigned(gift.getIsAssigned())
                    .build());
        }


        GiverDonationListResponseDto responseDto = GiverDonationListResponseDto.builder()
                .period(period)
                .totalAmount(totalAmount)
                .unreceived(unreceived)
                .received(received)
                .msg(msg)
                .donationList(donationList)
                .build();
        return ApiResponse.success(Success.GET_HISTORY_GIVER_DONATIONLIST_SUCCESS, responseDto);
    }
}
