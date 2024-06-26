package zero.eight.donut.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.dto.donation.DonateGiftRequestDto;
import zero.eight.donut.dto.donation.GiftboxRequestDto;
import zero.eight.donut.service.DonationService;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/donation")
public class DonationController {

    private final DonationService donationService;

    @PostMapping("/receiver/assign")
    public ApiResponse<?> assignGiftbox(@RequestBody GiftboxRequestDto giftboxRequestDto) {
        return donationService.assignGiftbox(giftboxRequestDto);
    }

    @PostMapping("/giver/donate")
    public ApiResponse<?> donateGift(DonateGiftRequestDto donateGiftRequestDto) throws IOException {
        return donationService.donateGift(donateGiftRequestDto);
    }

    @PatchMapping("/wallet/{giftId}")
    public ApiResponse<?> walletDonation(@PathVariable Long giftId) {
        return donationService.walletDonation(giftId);
    }
}
