package zero.eight.donut.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.service.ReportService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/report")
public class ReportController {
    private final ReportService reportService;
    @PostMapping("/use")
    public ApiResponse<?> createUsed(@RequestParam(name = "giftId") Long giftId ){
        return reportService.createUsed(giftId);
    }

}
