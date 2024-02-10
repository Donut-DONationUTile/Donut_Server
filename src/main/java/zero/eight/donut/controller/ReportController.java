package zero.eight.donut.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import zero.eight.donut.common.response.SuccessResponse;
import zero.eight.donut.dto.report.ReportRequestDto;
import zero.eight.donut.dto.report.ReportResponseDto;
import zero.eight.donut.exception.Success;
import zero.eight.donut.service.ReportService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/report")
public class ReportController {
    private final ReportService reportService;
    @PostMapping("/use")
    public SuccessResponse<ReportResponseDto> createReport(@RequestParam(name = "giftId") Long giftId ){
        return SuccessResponse.success(Success.CREATE_REPORT_SUCCESS, reportService.createReport(giftId));
    }

}
