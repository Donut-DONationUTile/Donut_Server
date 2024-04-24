package zero.eight.donut.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.service.WalletService;

@RequiredArgsConstructor
@RequestMapping("/api/wallet")
@RestController
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/main")
    public ApiResponse<?> walletMain() {
        return walletService.walletMain();
    }
}
