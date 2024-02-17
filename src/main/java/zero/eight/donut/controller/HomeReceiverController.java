package zero.eight.donut.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.service.HomeReceiverService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home/receiver")
public class HomeReceiverController {
    private final HomeReceiverService homeReceiverService;

    @GetMapping("")
    public ApiResponse<?> receiverHome(){
        return homeReceiverService.receiverHome();
    }

    @GetMapping("/box/{boxId}")
    public ApiResponse<?> receiverGetOneBox(@PathVariable("boxId") Long boxId){
        return homeReceiverService.receiverGetOneBox(boxId);
    }

    @GetMapping("/gift/{giftId}")
    public ApiResponse<?> receiverGetOneGuft(@PathVariable("giftId") Long giftId){
        return homeReceiverService.receiverGetOneGift(giftId);
    }
}
