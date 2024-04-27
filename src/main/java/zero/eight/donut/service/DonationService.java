package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.config.jwt.AuthUtils;
import zero.eight.donut.domain.Gift;
import zero.eight.donut.domain.Giver;
import zero.eight.donut.domain.enums.Status;
import zero.eight.donut.dto.donation.DonateGiftRequestDto;
import zero.eight.donut.dto.donation.GiftboxRequestDto;
import zero.eight.donut.exception.Error;
import zero.eight.donut.exception.Success;
import zero.eight.donut.repository.GiftRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DonationService {

    private final AuthUtils authUtils;
    private final SerialDonationService donationService;
    private  final GiftRepository giftRepository;

    @Async
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void autoDonate(){
        List<Gift> giftList = giftRepository.findAllByNotAssignedAndStatusAndDueDateBetween( "STORED", LocalDateTime.now(), LocalDateTime.now().minusDays(30));
        for (Gift gift : giftList) {
            gift.updateStatus("UNUSED");
            giftRepository.save(gift);
        }
    }

    public synchronized ApiResponse<?> assignGiftbox(GiftboxRequestDto giftboxRequestDto) {
        return donationService.assignGiftbox(giftboxRequestDto);
    }

    public ApiResponse<?> donateGift(DonateGiftRequestDto requestDto) throws IOException {
        return donationService.donateGift(requestDto);
    }

    @Transactional
    public ApiResponse<?> walletDonation(Long giftId) {
        // 기프티콘 찾기
        Gift gift = giftRepository.findById(giftId).orElse(null);
        if (gift == null) {
            return ApiResponse.failure(Error.GIFT_NOT_FOUND_EXCEPTION);
        }

        // 기프티콘 소유자 == 사용자 확인
        Giver giver = authUtils.getGiver();
        if (!gift.getGiver().equals(giver)) {
            return ApiResponse.failure(Error.NOT_AUTHENTICATED_EXCEPTION);
        }

        // 기프티콘 status 변경하기(-> UNUSED)
        gift.updateStatus("UNUSED");
        giftRepository.save(gift);

        return ApiResponse.success(Success.DONATE_GIFT_SUCCESS);
    }
}