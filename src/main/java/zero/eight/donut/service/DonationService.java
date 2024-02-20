package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.dto.donation.GiftboxRequestDto;

@Slf4j
@RequiredArgsConstructor
@Service
public class DonationService {

    private final SerialDonationService donationService;

    public synchronized ApiResponse<?> assignGiftbox(GiftboxRequestDto giftboxRequestDto) {
        return donationService.assignGiftbox(giftboxRequestDto);
    }
}