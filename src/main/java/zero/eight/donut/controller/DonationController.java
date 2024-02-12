package zero.eight.donut.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.dto.donation.GiftboxRequestDto;
import zero.eight.donut.service.DonationService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/donation")
public class DonationController {

    private final DonationService donationService;

    @PostMapping("/receiver/assign")
    public ApiResponse<?> assignGiftbox(GiftboxRequestDto giftboxRequestDto) {
        return donationService.assignGiftbox(giftboxRequestDto);
    }
}