package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.config.jwt.AuthUtils;
import zero.eight.donut.domain.*;
import zero.eight.donut.domain.enums.Status;
import zero.eight.donut.dto.auth.Role;
import zero.eight.donut.dto.report.ReportedRequestDto;
import zero.eight.donut.dto.report.ReportedResponseDto;
import zero.eight.donut.dto.report.UsedResponseDto;
import zero.eight.donut.exception.Error;
import zero.eight.donut.exception.Success;
import zero.eight.donut.repository.GiftRepository;
import zero.eight.donut.repository.GiftboxRepository;
import zero.eight.donut.repository.ReportRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final AuthUtils authUtils;
    private final ReportRepository reportRepository;
    private final GiftRepository giftRepository;
    private final GiftboxRepository giftboxRepository;
    @Transactional
    public ApiResponse<?> createUsed(Long giftId){
        //Gift 있는지 확인
        Optional<Gift> gift = giftRepository.findById(giftId);
        if(gift.isEmpty())
            return ApiResponse.failure(Error.GIFT_NOT_FOUND_EXCEPTION);

        //할당된 Giftbox 확인
        Giftbox giftbox = giftboxRepository.findByGiftId(giftId);
        if(giftbox == null)
            return ApiResponse.failure(Error.GIFTBOX_NOT_FOUND_EXCEPTION);

        //이미 사용된 경우
        if(gift.get().getStatus() == Status.USED)
            return ApiResponse.failure(Error.ALREADY_USED_GIFT_EXCEPTION);

        gift.get().updateStatus("USED");
        Integer amount = giftbox.getAmount() - gift.get().getPrice();
        Boolean availability = amount > 0 ? true:false;

        giftbox.updateAmountAndIsAvailable(amount, availability);

        UsedResponseDto responseDto = UsedResponseDto.builder()
                .isLast(!availability)
                .build();
        return ApiResponse.success(Success.CREATE_REPORT_SUCCESS, responseDto);
    }
    @Transactional
    public ApiResponse<?> createReport(ReportedRequestDto requestDto){
        //수혜자 여부 조회
        if (!authUtils.getCurrentUserRole().equals(Role.ROLE_RECEIVER)) {
                return ApiResponse.failure(Error.NOT_AUTHENTICATED_EXCEPTION);
        }

        //Gift 있는지 확인
        Optional<Gift> gift = giftRepository.findById(requestDto.getGiftId());
        if(gift.isEmpty())
            return ApiResponse.failure(Error.GIFT_NOT_FOUND_EXCEPTION);

        //할당된 Giftbox 확인
        Giftbox giftbox = giftboxRepository.findByGiftId(requestDto.getGiftId());
        if(giftbox.getId() == 0L)
            return ApiResponse.failure(Error.GIFTBOX_NOT_FOUND_EXCEPTION);

        // Report gift
        gift.get().updateStatus("REPORTED");

        //Create Report
        Giver giver = gift.get().getGiver();
        Receiver receiver = authUtils.getReceiver();
        Report report = requestDto.toEntity(giver, receiver, gift.get());
        reportRepository.save(report);

        //Giftbox info updated
        Integer amount = giftbox.getAmount() - gift.get().getPrice();
        Boolean availability = amount > 0 ? true:false;
        giftbox.updateAmountAndIsAvailable(amount, availability);

        ReportedResponseDto responseDto = ReportedResponseDto.builder()
                .reportId(report.getId())
                .giftId(report.getGift().getId())
                .isLast(!availability)
                .build();
        return ApiResponse.success(Success.CREATE_REPORT_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> patchUnused(Long giftId) {
        //Gift 있는지 확인
        Optional<Gift> giftOptional = giftRepository.findById(giftId);
        if(giftOptional.isEmpty())
            return ApiResponse.failure(Error.GIFT_NOT_FOUND_EXCEPTION);

        Gift gift = giftOptional.get();

        gift.updateStatus("STORED");

        return ApiResponse.success(Success.PATCH_STATUS_SUCCESS);
    }
}
