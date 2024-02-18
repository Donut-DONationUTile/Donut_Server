package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.config.jwt.AuthUtils;
import zero.eight.donut.domain.Gift;
import zero.eight.donut.domain.Giftbox;
import zero.eight.donut.domain.Receiver;
import zero.eight.donut.domain.enums.Store;
import zero.eight.donut.dto.auth.Role;
import zero.eight.donut.dto.home.receiver.*;
import zero.eight.donut.exception.Error;
import zero.eight.donut.exception.Success;
import zero.eight.donut.repository.BenefitRepository;
import zero.eight.donut.repository.GiftRepository;
import zero.eight.donut.repository.GiftboxRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeReceiverService {
    private final AuthUtils authUtils;
    private final GiftRepository giftRepository;
    private final GiftboxRepository giftboxRepository;
    private final BenefitRepository benefitRepository;
    @Transactional
    public ApiResponse receiverHome(){
        // 수혜자 여부 검증
        if (!authUtils.getCurrentUserRole().equals(Role.ROLE_RECEIVER)) {
            return ApiResponse.failure(Error.NOT_AUTHENTICATED_EXCEPTION);
        }
        Receiver receiver = authUtils.getReceiver();

        //사용 가능한 꾸러미만 조회
        List<Giftbox> giftboxList = Optional.ofNullable(giftboxRepository.findAllByReceiverIdAndIsAvailable(receiver.getId()))
                .orElse(Collections.emptyList());

        //사용처별 꾸러미 개수 구하기
        Map<String, Integer> storeCountMap = new HashMap<>();
        giftboxList.stream().forEach(box -> {
            String store = box.getStore().toString();
            storeCountMap.put(store, storeCountMap.getOrDefault(store, 0) + 1);
        });

        //꾸러미 정보 가져오기
        List<BoxInfo> boxInfoList = giftboxList.stream()
                .map(boxInfo -> BoxInfo.builder()
                        .boxId(boxInfo.getId())
                        .store(boxInfo.getStore())
                        .dueDate(boxInfo.getDueDate())
                        .amount(boxInfo.getAmount())
                        .build())
                .collect(Collectors.toList());


        /***
         * 꾸러미 신청 가능 여부
         * 1. 현재 갖고 있는 꾸러미들의 총합이 1000원 이하일 것
         * 2. 이번 달 수혜 금액을 넘지 않을 것
         * 3. 기부됐지만 할당되지 않은 기프티콘들의 합이 1000원 이상일 것
         */
        Boolean availability  = true;
        LocalDate now = LocalDate.now();
        Long amount = giftboxList.stream().mapToLong(boxInfo -> boxInfo.getAmount()).sum();
        if(amount > 1000
                || !benefitRepository.findByReceiverIdAndThisMonth(receiver.getId(), now.getYear(), now.getMonthValue()).getAvailability()
                || giftRepository.sumByNotAssigned() <1001)
            availability = false;


        ReceiverHomeResponseDto responseDto = ReceiverHomeResponseDto.builder()
                .availability(availability)
                .amount(amount)
                .cu(storeCountMap.getOrDefault("CU", 0))
                .gs25(storeCountMap.getOrDefault("GS25", 0))
                .sevenEleven(storeCountMap.getOrDefault("7-ELEVEN", 0))
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
