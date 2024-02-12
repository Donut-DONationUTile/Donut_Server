package zero.eight.donut.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.service.HomeGiverService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home/giver")
public class HomeGiverController {
    private final HomeGiverService homeGiverService;

    @GetMapping("")
    public ApiResponse<?> giverHome(){
        return homeGiverService.giverHome();
    }

}
