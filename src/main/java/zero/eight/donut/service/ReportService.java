package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.domain.Gift;
import zero.eight.donut.domain.Giftbox;
import zero.eight.donut.dto.report.ReportResponseDto;
import zero.eight.donut.exception.Error;
import zero.eight.donut.exception.NotFoundException;
import zero.eight.donut.exception.Success;
import zero.eight.donut.repository.GiftRepository;
import zero.eight.donut.repository.GiftboxRepository;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final GiftRepository giftRepository;
    private final GiftboxRepository giftboxRepository;
    @Transactional
    public ApiResponse<?> createReport(Long giftId){
        Gift gift = giftRepository.findById(giftId)
                .orElseThrow(()-> new NotFoundException(Error.GIFT_NOT_FOUND_EXCEPTION));
        Giftbox giftbox = giftboxRepository.findByGiftId(giftId);

        gift.updateStatus("USED");
        Integer amount = giftbox.getAmount() - gift.getPrice();
        Boolean availability = amount > 0 ? true:false;

        giftbox.updateAmountAndIsAvailable(amount, availability);

        ReportResponseDto responseDto = ReportResponseDto.builder()
                .isLast(!availability)
                .build();
        return ApiResponse.success(Success.CREATE_REPORT_SUCCESS, responseDto);
    }
}
