package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.config.jwt.AuthUtils;
import zero.eight.donut.domain.Giver;
import zero.eight.donut.domain.Receiver;
import zero.eight.donut.dto.ReceiverInfoResponseDto;
import zero.eight.donut.dto.home.giver.GiverHomeResponseDto;
import zero.eight.donut.dto.mypage.GiverInfoResponseDto;
import zero.eight.donut.dto.mypage.StatsDto;
import zero.eight.donut.exception.Success;
import zero.eight.donut.repository.*;

import java.time.Duration;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class MypageService {

    private final AuthUtils authUtils;
    private final GiftRepository giftRepository;
    private final DonationRepository donationRepository;
    private final DonationInfoRepository donationInfoRepository;
    private final MessageRepository messageRepository;
    private final BenefitRepository benefitRepository;

    public ApiResponse<?> getGiverMypage() {
        // 기부 기간(연) 계산
        Giver giver = authUtils.getGiver();
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(giver.getCreatedAt(), now); // 현재 시간과 기부자 계정 생성 시간 비교
        long days = duration.toDays(); // Duration 객체의 총 일(day) 수를 가져옴
        int years = (int) (days / 365); // 평년을 기준으로 연수 계산

        // 총 기부 금액 계산
        double donation = 0d;
        Long donationInfo = donationRepository.getSumByGiverId(giver.getId());
        if (donationInfo != null) {
            donation = donationInfo.doubleValue();
        }
        
        // 기부-할당x 기프티콘 수 계산
        int unreceived = giftRepository.findNotAssignedByGiverIdAndIsAssigned(giver.getId());
        
        // 기부-할당o 기프티콘 수 계산
        int received = giftRepository.findIsAssignedByGiverIdAndIsAssigned(giver.getId());
        
        // 받은 메세지 수 계산
        int msg = messageRepository.countByGiverId(giver.getId());

        // DTO 생성 및 반환
        StatsDto statsDto = StatsDto.builder()
                .unreceived(unreceived)
                .received(received)
                .msg(msg)
                .build();

        GiverInfoResponseDto responseDto = GiverInfoResponseDto.builder()
                .years(years)
                .donation(donation)
                .stats(statsDto)
                .build();

        return ApiResponse.success(Success.MYPAGE_GIVER_SUCCESS, responseDto);
    }

    public ApiResponse<?> getReceiverMypage() {
        // 수혜 금액 계산
        Receiver receiver = authUtils.getReceiver();
        double total = 0d;
        Integer summed = benefitRepository.sumBenefitByReceiverId(receiver.getId());
        if (summed != null) {
            total = summed.doubleValue();
        }

        // DTO 생성 및 반환
        ReceiverInfoResponseDto responseDto = ReceiverInfoResponseDto.builder()
                .total(total)
                .build();

        return ApiResponse.success(Success.MYPAGE_RECEIVER_SUCCESS, responseDto);
    }
}
