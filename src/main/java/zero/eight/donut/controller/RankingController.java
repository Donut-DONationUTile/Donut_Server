package zero.eight.donut.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.service.RankingService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ranking/giver/info")
public class RankingController {

    private final RankingService rankingService;

    @GetMapping("/price")
    public ApiResponse<?> getPriceRank() {
        return rankingService.getRankingByPrice();
    }

    @GetMapping("/number")
    public ApiResponse<?> getNumberRank() {
        return rankingService.getRankingByNumber();
    }
}