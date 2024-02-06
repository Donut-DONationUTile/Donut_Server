package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.dto.giver.response.GiverHomeResponseDto;
import zero.eight.donut.repository.DonationInfoRespository;
import zero.eight.donut.repository.GiverRepository;
import zero.eight.donut.repository.ReceiverRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GiverService {
    private final GiverRepository giverRepository;
    private final ReceiverRepository receiverRepository;
    private final DonationInfoRespository donationInfoRespository;

    @Transactional
    public GiverHomeResponseDto giverHome(){
        LocalDateTime now = LocalDateTime.now();
        Integer receivers = receiverRepository.countBy();
        Double donated = donationInfoRespository.findByMonthAndYear(now.getMonthValue(), now.getYear()).doubleValue();

        return GiverHomeResponseDto.builder()
                .receivers(receivers)
                .donated(donated)
                .need(receivers*50000.0)
                .build();
    }
}
