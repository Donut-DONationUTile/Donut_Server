package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.dto.giver.response.GiverHomeResponseDto;
import zero.eight.donut.repository.DonationInfoRespository;
import zero.eight.donut.repository.GiverRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GiverService {
    private final GiverRepository giverRepository;
    private final DonationInfoRespository donationInfoRespository;

    @Transactional
    public GiverHomeResponseDto giverHome(){
        LocalDateTime now = LocalDateTime.now();
        Integer receivers = giverRepository.countBy();
        Double donated = donationInfoRespository.findByMonthAndYear(now.getMonthValue(), now.getYear());
        return GiverHomeResponseDto.builder()
                .receivers(receivers)
                .donated(donated)
                .need(receivers*50.0)
                .build();
    }
}
