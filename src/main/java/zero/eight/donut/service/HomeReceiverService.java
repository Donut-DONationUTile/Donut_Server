package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.domain.Gift;
import zero.eight.donut.domain.Giftbox;
import zero.eight.donut.domain.Receiver;
import zero.eight.donut.domain.enums.Store;
import zero.eight.donut.dto.auth.Role;
import zero.eight.donut.dto.home.receiver.*;
import zero.eight.donut.exception.Error;
import zero.eight.donut.exception.Success;
import zero.eight.donut.repository.GiftRepository;
import zero.eight.donut.repository.GiftboxRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeReceiverService {
    private final AuthUtils authUtils;
    private final GiftRepository giftRepository;
    private final GiftboxRepository giftboxRepository;
    @Transactional
    public ApiResponse receiverHome(){
        // 수혜자 여부 검증
        if (!authUtils.getCurrentUserRole().equals(Role.ROLE_RECEIVER)) {
            return ApiResponse.failure(Error.NOT_AUTHENTICATED_EXCEPTION);
        }
        Receiver receiver = authUtils.getReceiver();

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
            Store store = box.getStore();
            storeCountMap.put(store.toString(), storeCountMap.getOrDefault(store, 0) + 1);
        });
      /**
       * TO DO : availability 처리하기
       ***/
        Boolean availability  = true;

        ReceiverHomeResponseDto responseDto = ReceiverHomeResponseDto.builder()
                .availability(availability)
                .amount(amount)
                .cu(storeCountMap.getOrDefault("CU", 0))
                .gs25(storeCountMap.getOrDefault("GS25", 0))
                .sevenEleven(storeCountMap.getOrDefault("SEVENELEVEN", 0))
                .boxList(boxInfoList)
                .build();
        return ApiResponse.success(Success.HOME_RECEIVER_SUCCESS, responseDto);
    }


    @Transactional
    public ApiResponse receiverGetOneBox(Long boxId){
        // 수혜자 여부 검증
        if (!authUtils.getCurrentUserRole().equals(Role.ROLE_RECEIVER)) {
            return ApiResponse.failure(Error.NOT_AUTHENTICATED_EXCEPTION);
        }
        //Giftbox 있는지 확인
        Optional<Giftbox> giftbox = giftboxRepository.findById(boxId);
        if(giftbox.isEmpty())
            return ApiResponse.failure(Error.GIFTBOX_NOT_FOUND_EXCEPTION);

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

        ReceiverGetBoxResponseDto responseDto = ReceiverGetBoxResponseDto.builder()
                .store(giftbox.get().getStore())
                .amount(giftbox.get().getAmount())
                .dueDate(giftbox.get().getDueDate())
                .giftList(giftInfoList)
                .build();
        return ApiResponse.success(Success.HOME_RECEIVER_BOX_SUCCESS, responseDto);
    }
    @Transactional
    public ApiResponse receiverGetOneGift(Long giftId){
        // 수혜자 여부 검증
        if (!authUtils.getCurrentUserRole().equals(Role.ROLE_RECEIVER)) {
            return ApiResponse.failure(Error.NOT_AUTHENTICATED_EXCEPTION);
        }
        //Gift 있는지 확인
        Optional<Gift> giftOptional = giftRepository.findById(giftId);
        if(giftOptional.isEmpty())
            return ApiResponse.failure(Error.GIFT_NOT_FOUND_EXCEPTION);
        Gift gift = giftOptional.get();

        ReceiverGetGiftResponseDto responseDto = ReceiverGetGiftResponseDto.builder()
                .product(gift.getProduct())
                .price(gift.getPrice())
                .dueDate(gift.getDueDate())
                .imgUrl(gift.getImageUrl())
                .store(gift.getStore())
                .status(gift.getStatus())
                .boxId(gift.getGiftbox().getId())
                .build();
        return ApiResponse.success(Success.HOME_RECEIVER_GIFT_SUCCESS, responseDto);
    }
}
