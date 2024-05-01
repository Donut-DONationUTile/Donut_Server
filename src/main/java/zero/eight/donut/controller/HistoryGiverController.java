package zero.eight.donut.controller;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.service.AuthService;
import zero.eight.donut.service.HistoryGiverService;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/history/giver")
public class HistoryGiverController {
    private final HistoryGiverService historyGiverService;
    private final AuthService authService;

    @GetMapping("/info/{donateDate}")
    public ApiResponse<?> getDonationList(@PathVariable(name = "donateDate")LocalDateTime donateDate){
        return  historyGiverService.getDonationList(donateDate);
    }

    @GetMapping("/info/detail/{giftId}")
    public ApiResponse<?> getDonationDetail(@PathVariable(name = "giftId")Long giftId){
        return  historyGiverService.getDonationDetail(giftId);
    }

    @GetMapping("/giver/get")
    public ApiResponse<?> getGiverEntity() {
        return authService.getGiverEntity();
    }
}
