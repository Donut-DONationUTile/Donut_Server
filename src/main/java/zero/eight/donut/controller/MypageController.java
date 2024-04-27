package zero.eight.donut.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.service.MypageService;

@RequiredArgsConstructor
@RequestMapping("/api/mypage")
@RestController
public class MypageController {
    private final MypageService mypageService;

    @GetMapping("/giver")
    public ApiResponse<?> giverInfo() {
        return mypageService.getGiverMypage();
    }

    @GetMapping("/receiver")
    public ApiResponse<?> receiverInfo() {
        return mypageService.getReceiverMypage();
    }
}
