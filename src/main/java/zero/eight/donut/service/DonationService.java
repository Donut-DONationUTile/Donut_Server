package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.dto.donation.DonateGiftRequestDto;
import zero.eight.donut.dto.donation.GiftboxRequestDto;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class DonationService {

    private final SerialDonationService donationService;

    public synchronized ApiResponse<?> assignGiftbox(GiftboxRequestDto giftboxRequestDto) {
        return donationService.assignGiftbox(giftboxRequestDto);
    }

    public ApiResponse<?> donateGift(DonateGiftRequestDto requestDto) throws IOException {
        return donationService.donateGift(requestDto);
    }
}