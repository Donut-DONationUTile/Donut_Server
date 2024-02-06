package zero.eight.donut.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zero.eight.donut.common.response.SuccessResponse;
import zero.eight.donut.dto.home.giver.GiverHomeResponseDto;
import zero.eight.donut.exception.Success;
import zero.eight.donut.service.HomeGiverService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HomeGiverController {
    private final HomeGiverService homeGiverService;

    @GetMapping("/giver")
    public SuccessResponse<GiverHomeResponseDto> giverHome(){
        return SuccessResponse.success(Success.HOME_GIVER_SUCCESS, homeGiverService.giverHome());
    }

}
