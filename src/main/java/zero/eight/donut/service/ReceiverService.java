package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.domain.Giftbox;
import zero.eight.donut.domain.Receiver;
import zero.eight.donut.dto.receiver.response.BoxInfo;
import zero.eight.donut.dto.receiver.response.ReceiverGetBoxResponseDto;
import zero.eight.donut.dto.receiver.response.ReceiverHomeResponseDto;
import zero.eight.donut.repository.GiftboxRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceiverService {
    private final GiftboxRepository giftboxRepository;
    @Transactional
    public ReceiverHomeResponseDto receiverHome(Receiver receiver){
        List<Giftbox> giftboxList = giftboxRepository.findAllByReceiverId(receiver.getId());

        Long amount = giftboxList.stream().mapToLong(boxInfo -> boxInfo.getAmount()).sum();
        List<BoxInfo> boxInfoList = giftboxList.stream()
                .map(boxInfo -> BoxInfo.builder()
                        .boxId(boxInfo.getId())
                        .store(boxInfo.getStore())
                        .dueDate(boxInfo.getDueDate())
                        .amount(boxInfo.getAmount())
                        .build())
                .collect(Collectors.toList());

      /**
       * TO DO : availability 처리하기
       ***/
        Boolean availability  = true;


        return ReceiverHomeResponseDto.builder()
                .availability(availability)
                .amount(amount)
                .boxList(boxInfoList)
                .build();
    }


    @Transactional
    public ReceiverGetBoxResponseDto receiverGetOneBox(Receiver receiver, Long boxId){
        return ReceiverGetBoxResponseDto.builder().build();
    }

}
