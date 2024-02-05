package zero.eight.donut.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zero.eight.donut.dto.giver.response.GiverHomeResponseDto;
import zero.eight.donut.service.GiverService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GiverController {
    private final GiverService giverService;

    @GetMapping("/home/giver")
    GiverHomeResponseDto giverHome(){
        return giverService.giverHome();
    }

}
