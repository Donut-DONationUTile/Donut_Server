package zero.eight.donut.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.service.HistoryReceiverService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/history/receiver/info")
public class HistoryReceiverController {
    private final HistoryReceiverService historyReceiverService;
    @GetMapping("")
    public ApiResponse<?> receivedDonation(){
        return historyReceiverService.receivedDonation();
    }
}
