package zero.eight.donut.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.dto.fcm.FcmTokenRequestDto;
import zero.eight.donut.exception.Success;
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

    @PostMapping("/test/37")
    public ApiResponse<?> test37() throws FirebaseMessagingException {
        return ApiResponse.success(Success.FCM_TEST_SUCCESS, fcmService.imminentWallet());
    }

    @PostMapping("/test/7")
    public ApiResponse<?> test7() throws FirebaseMessagingException {
        return ApiResponse.success(Success.FCM_TEST_SUCCESS, fcmService.immminentGift());
    }
}