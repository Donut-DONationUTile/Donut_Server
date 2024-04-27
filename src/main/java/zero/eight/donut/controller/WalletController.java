package zero.eight.donut.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.dto.wallet.WalletUploadRequestDto;
import zero.eight.donut.service.WalletService;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/api/wallet")
@RestController
public class WalletController {

    private final WalletService walletService;

    @GetMapping
    public ApiResponse<?> walletMain() {
        return walletService.walletMain();
    }

    @GetMapping("/{giftId}")
    public ApiResponse<?> walletDetail(@PathVariable Long giftId) {
        return walletService.walletDetail(giftId);
    }

    @PostMapping
    public ApiResponse<?> walletUpload(@RequestBody WalletUploadRequestDto requestDto) throws IOException {
        return walletService.walletUpload(requestDto);
    }
}
