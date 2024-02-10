package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.domain.Gift;
import zero.eight.donut.domain.Giftbox;
import zero.eight.donut.domain.Receiver;
import zero.eight.donut.dto.home.receiver.*;
import zero.eight.donut.exception.Error;
import zero.eight.donut.exception.NotFoundException;
import zero.eight.donut.repository.GiftRepository;
import zero.eight.donut.repository.GiftboxRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeReceiverService {
    private final GiftRepository giftRepository;
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

        Map<String, Integer> storeCountMap = new HashMap<>();
        giftboxList.stream().forEach(box -> {
            String store = box.getStore();
            storeCountMap.put(store, storeCountMap.getOrDefault(store, 0) + 1);
        });
      /**
       * TO DO : availability 처리하기
       ***/
        Boolean availability  = true;

        return ReceiverHomeResponseDto.builder()
                .availability(availability)
                .amount(amount)
                .cu(storeCountMap.getOrDefault("cu", 0))
                .gs25(storeCountMap.getOrDefault("gs25", 0))
                .sevenEleven(storeCountMap.getOrDefault("7eleven", 0))
                .boxList(boxInfoList)
                .build();
    }


    @Transactional
    public ReceiverGetBoxResponseDto receiverGetOneBox(Long boxId){
        Giftbox giftbox = giftboxRepository.findById(boxId)
                .orElseThrow(() -> new NotFoundException(Error.GIFTBOX_NOT_FOUND_EXCEPTION));

        List<Gift> giftList = giftRepository.findAllByGiftboxId(boxId);

        List<GiftInfo> giftInfoList = giftList.stream()
                .map(gift -> GiftInfo.builder()
                        .giftId(gift.getId())
                        .product(gift.getProduct())
                        .price(gift.getPrice())
                        .dueDate(gift.getDueDate())
                        .isUsed(gift.getStatus().toString())
                        .build())
                .collect(Collectors.toList());

        return ReceiverGetBoxResponseDto.builder()
                .store(giftbox.getStore())
                .amount(giftbox.getAmount())
                .dueDate(giftbox.getDueDate())
                .giftList(giftInfoList)
                .build();
    }
    @Transactional
    public ReceiverGetGiftResponseDto receiverGetOneGift(Long giftId){
        Gift gift = giftRepository.findById(giftId).orElseThrow(()-> new NotFoundException((Error.GIFT_NOT_FOUND_EXCEPTION)));
        return ReceiverGetGiftResponseDto.builder()
                .product(gift.getProduct())
                .price(gift.getPrice())
                .dueDate(gift.getDueDate())
                .imgUrl(gift.getImageUrl())
                .store(gift.getStore())
                .status(gift.getStatus())
                .boxId(gift.getGiftbox().getId())
                .build();
    }
}