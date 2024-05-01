package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.config.jwt.AuthUtils;
import zero.eight.donut.domain.Benefit;
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
import java.time.LocalDateTime;
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
        Long receiver_id = receiver.getId();

        //사용 가능한 꾸러미만 조회
        List<Giftbox> giftboxList = Optional.ofNullable(giftboxRepository.findAllByReceiverIdAndIsAvailable(receiver_id))
                .orElse(Collections.emptyList());

      
        //사용처별 꾸러미 잔액 -> 쿼리 한번만 호출하도록 변경
        Map<Store, Integer> storeGiftBoxMap = giftboxRepository.getGiftboxSumsByStore(receiver_id);
        Integer cu = storeGiftBoxMap.getOrDefault(Store.CU, 0);
        Integer gs25 = storeGiftBoxMap.getOrDefault(Store.GS25, 0);
        Integer seveneleven = storeGiftBoxMap.getOrDefault(Store.SEVENELEVEN, 0);


        //꾸러미 정보 가져오기
        List<BoxInfo> boxInfoList = new ArrayList<>();
        Long amount = 0L;
        for (Giftbox boxInfo : giftboxList) {
            boxInfoList.add(BoxInfo.builder()
                    .boxId(boxInfo.getId())
                    .store(boxInfo.getStore())
                    .dueDate(boxInfo.getDueDate())
                    .amount(boxInfo.getAmount())
                    .build());
            amount += boxInfo.getAmount();
        }

        /***
         * Get Giftbox Eligibility
         * 1. The total of the current packages held must be under 1000 KRW.
         * 2. It will not exceed this month's benefit amount.
         * 3. The sum of donated but unassigned Gifticons must be over 1000 KRW.
         */
        Boolean availability  = true;
        LocalDate now = LocalDate.now();
        //2. check this month benefit amount
        Optional<Benefit> optionalBenefit = benefitRepository.findByReceiverIdAndThisMonth(receiver_id, now.getYear(), now.getMonthValue());
        Boolean checkbenefit = optionalBenefit.map(Benefit::getAvailability)
                .orElseGet(() -> {
                    createNewBenefit(receiver);
                    return Boolean.TRUE;
                });

        if(amount > 1000 //1.
                || !checkbenefit //2.
                || giftRepository.sumByNotAssigned() <1001) //3.
            availability = false;


        ReceiverHomeResponseDto responseDto = ReceiverHomeResponseDto.builder()
                .availability(availability)
                .amount(amount)
                .cu(cu)
                .gs25(gs25)
                .sevenEleven(seveneleven)
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

        // Security Config로 책임 전가
//        // 수혜자 여부 검증
//        if (!authUtils.getCurrentUserRole().equals(Role.ROLE_RECEIVER)) {
//            return ApiResponse.failure(Error.NOT_AUTHENTICATED_EXCEPTION);
//        }

        //Gift 있는지 확인
        Optional<Gift> giftOptional = giftRepository.findById(giftId);
        if(giftOptional.isEmpty())
            return ApiResponse.failure(Error.GIFT_NOT_FOUND_EXCEPTION);

        Gift gift = giftOptional.get();

        // 재사용을 위해 함수로 변경
        /*
        GetGiftResponseDto responseDto = GetGiftResponseDto.builder()
                .product(gift.getProduct())
                .price(gift.getPrice())
                .dueDate(gift.getDueDate())
                .imgUrl(gift.getImageUrl())
                .store(gift.getStore())
                .status(gift.getStatus())
                .boxId(gift.getGiftbox().getId())
                .build();
         */

        return ApiResponse.success(Success.HOME_RECEIVER_GIFT_SUCCESS, getGiftInfo(giftId, gift));
    }

    public void createNewBenefit(Receiver receiver){
        Benefit newBenefit = Benefit.builder()
                .receiver(receiver)
                .sum(0)
                .month(LocalDateTime.now().getMonthValue())
                .year(LocalDateTime.now().getMonthValue())
                .availability(true)
                .build();
        benefitRepository.save(newBenefit);
    }
    public GetGiftResponseDto getGiftInfo(Long giftId, Gift gift){

        return GetGiftResponseDto.builder()
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
