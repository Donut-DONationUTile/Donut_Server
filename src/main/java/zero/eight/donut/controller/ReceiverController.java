package zero.eight.donut.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zero.eight.donut.common.response.SuccessResponse;
import zero.eight.donut.config.annotation.LoginUser;
import zero.eight.donut.domain.Receiver;
import zero.eight.donut.dto.receiver.response.ReceiverGetBoxResponseDto;
import zero.eight.donut.dto.receiver.response.ReceiverGetGiftResponseDto;
import zero.eight.donut.dto.receiver.response.ReceiverHomeResponseDto;
import zero.eight.donut.exception.Success;
import zero.eight.donut.service.ReceiverService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReceiverController {
    private  final ReceiverService receiverService;

    @GetMapping("/home/receiver")
    public SuccessResponse<ReceiverHomeResponseDto> receiverHome(@LoginUser Receiver receiver){
        return SuccessResponse.success(Success.HOME_RECEIVER_SUCCESS, receiverService.receiverHome(receiver));
    }

    @GetMapping("/home/receiver/box/{boxId}")
    public SuccessResponse<ReceiverGetBoxResponseDto> receiverGetOneBox(@PathVariable("boxId") Long boxId){
        return SuccessResponse.success(Success.HOME_RECEIVER_BOX_SUCCESS, receiverService.receiverGetOneBox(boxId));
    }

    @GetMapping("/home/receiver/box/{boxId}")
    public SuccessResponse<ReceiverGetGiftResponseDto> receiverGetOneGuft(@PathVariable("giftId") Long giftId){
        return SuccessResponse.success(Success.HOME_RECEIVER_BOX_SUCCESS, receiverService.receiverGetOneGift(giftId));
    }
}
