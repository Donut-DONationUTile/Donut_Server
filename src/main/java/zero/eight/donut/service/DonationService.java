package zero.eight.donut.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.config.firebase.FcmUtils;
import zero.eight.donut.domain.Gift;
import zero.eight.donut.dto.donation.DonateGiftRequestDto;
import zero.eight.donut.dto.donation.GiftboxRequestDto;
import zero.eight.donut.repository.GiftRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DonationService {
    private final FcmUtils fcmUtils;
    private final SerialDonationService donationService;
    private  final GiftRepository giftRepository;

    @Async
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void autoDonate() throws FirebaseMessagingException {
        List<Gift> giftList = giftRepository.findAllByNotAssignedAndStatusAndDueDateBetween( "STORED", LocalDateTime.now(), LocalDateTime.now().minusDays(30));
        for (Gift gift : giftList) {
            gift.updateStatus("UNUSED");
            giftRepository.save(gift);
            fcmUtils.sendMessage(gift.getGiver().getId(), "wallet: D-30", "Your item" + gift.getProduct() + "is donated now!");
        }
    }

    public synchronized ApiResponse<?> assignGiftbox(GiftboxRequestDto giftboxRequestDto) {
        return donationService.assignGiftbox(giftboxRequestDto);
    }

    public ApiResponse<?> donateGift(DonateGiftRequestDto requestDto) throws IOException {
        return donationService.donateGift(requestDto);
    }

    public ApiResponse<?> walletDonation(Long giftId) {
        return donationService.donateWalletGift(giftId);
    }
}