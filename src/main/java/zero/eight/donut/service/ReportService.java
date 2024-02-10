package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.domain.Gift;
import zero.eight.donut.dto.report.ReportRequestDto;
import zero.eight.donut.dto.report.ReportResponseDto;
import zero.eight.donut.exception.Error;
import zero.eight.donut.exception.NotFoundException;
import zero.eight.donut.repository.GiftRepository;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final GiftRepository giftRepository;
    @Transactional
    public ReportResponseDto createReport(ReportRequestDto requestDto){
        Gift gift = giftRepository.findById(requestDto.getGiftId())
                .orElseThrow(()-> new NotFoundException(Error.GIFT_NOT_FOUND_EXCEPTION));

        gift.updateStatus("USED");
        return ReportResponseDto.builder()
                .isLast(true)
                .build();
    }
}
