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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final GiftRepository giftRepository;
    private final GiftboxRepository giftboxRepository;
    @Transactional
    public ApiResponse<?> createReport(Long giftId){
        //Gift 있는지 확인
        Optional<Gift> gift = giftRepository.findById(giftId);
        if(gift.isEmpty())
            return ApiResponse.failure(Error.GIFT_NOT_FOUND_EXCEPTION);

        //할당된 Giftbox 확인
        Giftbox giftbox = giftboxRepository.findByGiftId(giftId);
        if(giftbox == null)
            return ApiResponse.failure(Error.GIFTBOX_NOT_FOUND_EXCEPTION);

        gift.get().updateStatus("USED");
        Integer amount = giftbox.getAmount() - gift.get().getPrice();
        Boolean availability = amount > 0 ? true:false;

        giftbox.updateAmountAndIsAvailable(amount, availability);

        ReportResponseDto responseDto = ReportResponseDto.builder()
                .isLast(!availability)
                .build();
        return ApiResponse.success(Success.CREATE_REPORT_SUCCESS, responseDto);
    }
}
