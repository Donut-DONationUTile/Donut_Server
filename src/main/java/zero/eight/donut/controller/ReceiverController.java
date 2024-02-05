package zero.eight.donut.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zero.eight.donut.common.response.SuccessResponse;
import zero.eight.donut.dto.receiver.response.ReceiverHomeResponseDto;
import zero.eight.donut.exception.Success;
import zero.eight.donut.service.ReceiverService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReceiverController {
    private  final ReceiverService receiverService;

    @GetMapping("/home/receiver")
    public SuccessResponse<ReceiverHomeResponseDto> receiverHome(){
        return SuccessResponse.success(Success.HOME_RECEIVER_SUCCESS, receiverService.receiverHome());
    }
}
