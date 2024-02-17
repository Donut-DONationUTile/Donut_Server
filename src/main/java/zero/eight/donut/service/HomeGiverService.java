package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.config.jwt.AuthUtils;
import zero.eight.donut.dto.auth.Role;
import zero.eight.donut.dto.home.giver.GiverHomeResponseDto;
import zero.eight.donut.exception.Error;
import zero.eight.donut.exception.Success;
import zero.eight.donut.repository.DonationInfoRepository;
import zero.eight.donut.repository.ReceiverRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HomeGiverService {
    //월별 최대 지원금액
    static Double maxAccount = 100000.0;
    private final AuthUtils authUtils;
    private final ReceiverRepository receiverRepository;
    private final DonationInfoRepository donationInfoRepository;

    @Transactional
    public ApiResponse giverHome(){
        // 기부자 여부 검증
        if (!authUtils.getCurrentUserRole().equals(Role.ROLE_GIVER)) {
            return ApiResponse.failure(Error.NOT_AUTHENTICATED_EXCEPTION);
        }

        LocalDateTime now = LocalDateTime.now();
        Integer receivers = receiverRepository.countBy();
        Double donated = 0.0;
        Long donationInfo = donationInfoRepository.findByMonthAndYear(now.getMonthValue(), now.getYear());
        if (donationInfo != null) {
            donated = donationInfo.doubleValue();
        }
        GiverHomeResponseDto responseDto =  GiverHomeResponseDto.builder()
                .receivers(receivers)
                .donated(donated)
                .need(receivers*maxAccount)
                .build();

        return ApiResponse.success(Success.HOME_GIVER_SUCCESS, responseDto);
    }
}
