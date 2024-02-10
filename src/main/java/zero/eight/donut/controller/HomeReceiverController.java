package zero.eight.donut.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zero.eight.donut.common.response.SuccessResponse;
import zero.eight.donut.domain.Receiver;
import zero.eight.donut.dto.home.receiver.ReceiverGetBoxResponseDto;
import zero.eight.donut.dto.home.receiver.ReceiverGetGiftResponseDto;
import zero.eight.donut.dto.home.receiver.ReceiverHomeResponseDto;
import zero.eight.donut.exception.Success;
import zero.eight.donut.service.AuthUtils;
import zero.eight.donut.service.HomeReceiverService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home/receiver")
public class HomeReceiverController {
    private final HomeReceiverService homeReceiverService;
    private final AuthUtils authUtils;

    @GetMapping("")
    public SuccessResponse<ReceiverHomeResponseDto> receiverHome(){
        return SuccessResponse.success(Success.HOME_RECEIVER_SUCCESS, homeReceiverService.receiverHome(authUtils.getReceiver()));
    }

    @GetMapping("/box/{boxId}")
    public SuccessResponse<ReceiverGetBoxResponseDto> receiverGetOneBox(@PathVariable("boxId") Long boxId){
        return SuccessResponse.success(Success.HOME_RECEIVER_BOX_SUCCESS, homeReceiverService.receiverGetOneBox(boxId));
    }

    @GetMapping("/gift/{giftId}")
    public SuccessResponse<ReceiverGetGiftResponseDto> receiverGetOneGuft(@PathVariable("giftId") Long giftId){
        return SuccessResponse.success(Success.HOME_RECEIVER_GIFT_SUCCESS, homeReceiverService.receiverGetOneGift(giftId));
    }
}
