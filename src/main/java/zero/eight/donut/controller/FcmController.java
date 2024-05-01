package zero.eight.donut.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.dto.fcm.FcmTokenRequestDto;
import zero.eight.donut.exception.Success;
import zero.eight.donut.service.DonationService;
import zero.eight.donut.service.FcmService;

@RequiredArgsConstructor
@RequestMapping("/api/fcm")
@RestController
public class FcmController {

    private final FcmService fcmService;
    private final DonationService donationService;

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

    @PostMapping("/test/30")
    public ApiResponse<?> test30() throws FirebaseMessagingException {
        return ApiResponse.success(Success.FCM_TEST_SUCCESS, donationService.autoDonate());
    }

    @GetMapping("/mock/37/{email}")
    public ApiResponse<?> mock37(@PathVariable(name = "email") String email, @RequestParam(name = "product") String product) throws FirebaseMessagingException {
        return ApiResponse.success(Success.FCM_TEST_SUCCESS, fcmService.mock37(email, product));
    }

    @GetMapping("/mock/30/{email}")
    public ApiResponse<?> mock30(@PathVariable(name = "email") String email, @RequestParam(name = "product") String product) throws FirebaseMessagingException {
        return ApiResponse.success(Success.FCM_TEST_SUCCESS, fcmService.mock30(email, product));
    }

    @GetMapping("/mock/7/{name}")
    public ApiResponse<?> mock7(@PathVariable(name = "name") String name, @RequestParam(name = "store") String store) throws FirebaseMessagingException {
        return ApiResponse.success(Success.FCM_TEST_SUCCESS, fcmService.mock7(name, store));
    }
}