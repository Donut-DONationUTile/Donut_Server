package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.dto.receiver.response.ReceiverHomeResponseDto;

@Service
@RequiredArgsConstructor
public class ReceiverService {
    @Transactional
    public ReceiverHomeResponseDto receiverHome(){
        return ReceiverHomeResponseDto.builder().build();
    }
}
