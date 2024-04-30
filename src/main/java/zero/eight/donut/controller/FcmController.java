package zero.eight.donut.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.dto.fcm.FcmTokenRequestDto;
import zero.eight.donut.service.FcmService;

@RequiredArgsConstructor
@RequestMapping("/api/fcm")
@RestController
public class FcmController {

    private final FcmService fcmService;

    @PostMapping("/token")
    public ApiResponse<?> createFcmToken(@RequestBody FcmTokenRequestDto requestDto) throws Exception {
        return fcmService.registerFcmToken(requestDto);
    }
}
