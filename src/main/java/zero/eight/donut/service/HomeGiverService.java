package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.dto.home.giver.GiverHomeResponseDto;
import zero.eight.donut.repository.DonationInfoRepository;
import zero.eight.donut.repository.GiverRepository;
import zero.eight.donut.repository.ReceiverRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HomeGiverService {
    private final GiverRepository giverRepository;
    private final ReceiverRepository receiverRepository;
    private final DonationInfoRepository donationInfoRepository;

    @Transactional
    public GiverHomeResponseDto giverHome(){
        LocalDateTime now = LocalDateTime.now();
        Integer receivers = receiverRepository.countBy();
        Double donated = donationInfoRepository.findByMonthAndYear(now.getMonthValue(), now.getYear()).doubleValue();

        return GiverHomeResponseDto.builder()
                .receivers(receivers)
                .donated(donated)
                .need(receivers*50000.0)
                .build();
    }
}
