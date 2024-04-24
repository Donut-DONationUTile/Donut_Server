package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import zero.eight.donut.common.response.ApiResponse;
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

    private final SerialDonationService donationService;
    private  final GiftRepository giftRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void autoDonate(){
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate;
        List<Gift> giftList = giftRepository.findAllByImminentAndNotAssignedAndStatus(startDate, endDate, "UNUSED");
    }

    public synchronized ApiResponse<?> assignGiftbox(GiftboxRequestDto giftboxRequestDto) {
        return donationService.assignGiftbox(giftboxRequestDto);
    }

    public ApiResponse<?> donateGift(DonateGiftRequestDto requestDto) throws IOException {
        return donationService.donateGift(requestDto);
    }
}