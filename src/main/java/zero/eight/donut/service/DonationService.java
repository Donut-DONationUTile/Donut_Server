package zero.eight.donut.service;


import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.domain.*;
import zero.eight.donut.dto.GiftAssignDto;
import zero.eight.donut.dto.auth.Role;
import zero.eight.donut.dto.donation.DonateGiftRequestDto;
import zero.eight.donut.dto.donation.GiftValueDto;
import zero.eight.donut.dto.donation.GiftboxRequestDto;
import zero.eight.donut.exception.ApiException;
import zero.eight.donut.exception.Error;
import zero.eight.donut.exception.InternalServerErrorException;
import zero.eight.donut.exception.Success;
import zero.eight.donut.repository.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class DonationService {

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String BUCKET_NAME;
    private final Storage storage;
    private final AuthUtils authUtils;
    private final BenefitRepository benefitRepository;
    private final GiftRepository giftRepository;
    private final GiftboxRepository giftboxRepository;
    private final DonationRepository donationRepository;
    private final DonationInfoRepository donationInfoRepository;

    @Transactional
    public ApiResponse<?> assignGiftbox(GiftboxRequestDto giftboxRequestDto) {
        log.info("기프티콘 할당 함수 접근");

        // 수혜자 여부 검증
        if (!authUtils.getCurrentUserRole().equals(Role.ROLE_RECEIVER)) {
            // 수혜 대상이 아닐 시(인증 오류)
            log.info("수혜 대상이 아님: ROLE -> {}", authUtils.getCurrentUserRole());
            return ApiResponse.failure(Error.NOT_AUTHENTICATED_EXCEPTION);
        }
        log.info("수혜 대상 검증 완료");
        
        // 할당 가능 금액 여부 검증
        Receiver receiver = authUtils.getReceiver();
        Optional<Benefit> benefitOptional = benefitRepository.findByReceiver(receiver);
        if (benefitOptional.isEmpty()) {
            // 수혜 내역 부재 시(서버 오류)
            log.info("수혜 내역 부재(INTERNAL_SERVER_ERROR)");
            throw new InternalServerErrorException(Error.INTERNAL_SERVER_ERROR);
        }
        else if(!benefitOptional.get().getAvailability()) {
            // 가용 잔액 부족 시(잔액 부족 오류)
            log.info("가용 잔액 부족(INSUFFICIENT_BALANCE_EXCEPTION)");
            return ApiResponse.failure(Error.INSUFFICIENT_BALANCE_EXCEPTION);
        }
        else if (giftRepository.sumByStoreName(giftboxRequestDto.getStore()) < giftboxRequestDto.getPrice()) {
            // 기부 금액 부족 시(기부 부족 오류)
            log.info("기부 금액 부족(INSUFFICIENT_DONATION_EXCEPTION)");
            return ApiResponse.failure(Error.INSUFFICIENT_DONATION_EXCEPTION);
        }
        log.info("할당 금액 검증 완료");

        // 사용처에 따른 기프티콘 추출
        List<Gift> giftList = giftRepository.findByStore(giftboxRequestDto.getStore());
        log.info("사용처에 따른 기프티콘 추출 완료: store -> {}", giftboxRequestDto.getStore());

        // 추출된 기프티콘 리스트에서 기프티콘, 가격, 사용 기한으로 새로운 리스트 생성
        List<GiftValueDto> giftValueDtoList = new ArrayList<>(); // gift, price, date(epochSecond)
        for (int i = 0; i < giftList.size(); i++) {
            Instant instant = giftList.get(i).getDueDate().atZone(ZoneId.systemDefault()).toInstant();
            GiftValueDto giftValueDto = GiftValueDto.builder()
                    .gift(giftList.get(i))
                    .price(giftList.get(i).getPrice())
                    .epochMilli(instant.getEpochSecond())
                    .build();
            giftValueDtoList.add(giftValueDto);
        }
        log.info("새로운 기프티콘 정보 리스트 생성 완료");
        
        // 가능한 기프티콘 중 사용 기한이 짧은 기프티콘부터 선택
        GiftAssignDto assignDto = findClosestValue(giftValueDtoList, giftboxRequestDto.getPrice());
        log.info("꾸러미 구성 완료");
        log.info("구성된 꾸러미 가격 -> {}", assignDto.getAssignedValue());

        // 구성된 꾸러미가 할당 가능한 금액인지 검증
        // 수혜 금액을 조금이라도 초과하면 할당 불가
        if (assignDto.getAssignedValue() > benefitOptional.get().getSum()) {
            log.info("구성된 꾸러미가 수혜 금액 초과(INSUFFICIENT_BALANCE_EXCEPTION)");
            return ApiResponse.failure(Error.INSUFFICIENT_BALANCE_EXCEPTION);
        }
        log.info("구성된 꾸러미 가격 검증 완료");

        // 꾸러미 생성
        Giftbox giftbox = Giftbox.builder()
                .receiver(receiver)
                .isAvailable(true)
                .store(giftboxRequestDto.getStore())
                .build();
        log.info("꾸러미 객체 생성 완료");

        // 꾸러미에 기프티콘 할당
        setGiftbox(assignDto, giftbox);
        log.info("꾸러미에 기프티콘 할당 완료");

        return ApiResponse.success(Success.ASSIGN_BENEFIT_SUCCESS);
    }

    private void setGiftbox(GiftAssignDto giftAssignDto, Giftbox giftbox) {
        log.info("꾸러미-기프티콘 할당 함수 접근");

        // 기프티콘 할당 처리
        for (Gift g : giftAssignDto.getAssignedList()) {
            // 할당 여부 true 처리
            g.setIsAssigned();
            // giftbox 매핑 갱신
            g.updateGiftbox(giftbox);
            // 기프티콘 객체 갱신
            giftRepository.save(g);
        }
        log.info("setGiftbox: 기프티콘 할당 처리 완료");

        // 꾸러미 사용 기간 & 남은 금액 갱신
        giftbox.updateDueDateAndAmount(giftAssignDto.getAssignedList().get(0).getDueDate(), giftAssignDto.getAssignedValue());
        log.info("꾸러미 사용 기간 & 남은 금액 갱신 완료");
        
        // 꾸러미 객체 갱신
        giftboxRepository.save(giftbox);
        log.info("꾸러미 객체 갱신 완료");
        log.info("할당된 꾸러미 객체 id -> {}", giftboxRepository.findById(giftbox.getId()));
    }

    private GiftAssignDto findClosestValue(List<GiftValueDto> values, int price) {

        // values 리스트를 epochMilli 필드의 값으로 오름차순 정렬
        values = values.stream()
                .sorted(Comparator.comparingLong(GiftValueDto::getEpochMilli))
                .collect(Collectors.toList());

        Integer currentSum = Integer.valueOf(0);
        List<Gift> assignedList = new ArrayList<>();

        for (int i = 0; i < values.size(); i++) {
            currentSum += values.get(i).getPrice(); // 배열의 값에 따른 금액 합산
            assignedList.add(values.get(i).getGift()); // 사용된 gift 객체 저장

            if (currentSum >= price) {
                break;
            }
        }

        GiftAssignDto giftAssignDto = GiftAssignDto.builder()
                .assignedList(assignedList)
                .assignedValue(currentSum)
                .build();

        return giftAssignDto;
    }

    @Transactional
    public ApiResponse<?> donateGift(DonateGiftRequestDto requestDto) throws IOException {
        // 기부자 여부 검증
        if (!authUtils.getCurrentUserRole().equals(Role.ROLE_GIVER)) {
            return ApiResponse.failure(Error.NOT_AUTHENTICATED_EXCEPTION);
        }
        Giver giver = authUtils.getGiver();
        log.info("giver name -> {}", giver.getName());
        Giftbox defaultGiftbox = giftboxRepository.findById(0L)
                .orElseThrow(()-> new ApiException(Error.GIFTBOX_NOT_FOUND_EXCEPTION));
        log.info("successfully get default giftbox");

        //이미지명 uuid 변환
        String uuid = UUID.randomUUID().toString();
        //이미지 형식 추출
        String ext = requestDto.getGiftImage().getContentType();
        if(storage==null)
            log.info("fail to get storage ->{}", storage);

        // Google Cloud Storage 이미지 업로드
        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(BUCKET_NAME, uuid)
                        .setContentType(ext)
                        .build(),
                requestDto.getGiftImage().getInputStream()
        );
        log.info("successfully upload image to gcs");

        String imgUrl = "https://storage.googleapis.com/" + BUCKET_NAME + "/" + uuid;

        //Gift 추가
        Gift newGift = requestDto.toEntity(giver, defaultGiftbox, imgUrl);
        giftRepository.save(newGift);

        //기부자별 정보 Donation 업데이트
        Donation donation = donationRepository.findByGiver(giver);
        donation.updateSumCount(
                donation.getSum()+requestDto.getPrice().longValue(),
                donation.getCount()+1L);

        //기부 통계 업데이트
        LocalDate now = LocalDate.now();
        DonationInfo donationInfo = donationInfoRepository.findDonationInfoByMonthAndYear(now.getMonthValue(), now.getYear());
        donationInfo.updateSumCount(
                donationInfo.getSum()+requestDto.getPrice().longValue(),
                donationInfo.getCount()+1L);

        return ApiResponse.success(Success.SUCCESS);
    }
}
