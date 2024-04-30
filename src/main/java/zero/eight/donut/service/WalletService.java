package zero.eight.donut.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.config.jwt.AuthUtils;
import zero.eight.donut.domain.Gift;
import zero.eight.donut.domain.Giftbox;
import zero.eight.donut.domain.Giver;
import zero.eight.donut.domain.enums.Store;
import zero.eight.donut.dto.auth.Role;
import zero.eight.donut.dto.home.receiver.GetGiftResponseDto;
import zero.eight.donut.dto.wallet.WalletGiftInfoResponseDto;
import zero.eight.donut.dto.wallet.WalletResponseDto;
import zero.eight.donut.dto.wallet.WalletUploadRequestDto;
import zero.eight.donut.exception.ApiException;
import zero.eight.donut.exception.Error;
import zero.eight.donut.exception.Success;
import zero.eight.donut.repository.GiftRepository;
import zero.eight.donut.repository.GiftboxRepository;
import zero.eight.donut.repository.ReceiverRepository;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class WalletService {

    // gcp 변수
    @Value("${spring.cloud.gcp.storage.bucket}")
    private String BUCKET_NAME;
    private final Storage storage;

    private final AuthUtils authUtils;
    private final ReceiverRepository receiverRepository;
    private final GiftRepository giftRepository;
    private final GiftboxRepository giftboxRepository;

    public ApiResponse<?> walletMain() {
        // 변수 선언
        LocalDateTime now = LocalDateTime.now(); // 조회 시각
        Giver giver = authUtils.getGiver(); // 기부자 정보
        int receiver = receiverRepository.countBy(); // 수혜자 수
        double amount = 0d; // 기프티콘 총액
        int cu = 0; // 보유 기프티콘 개수(CU)
        int gs25 = 0; // 보유 기프티콘 개수(GS25)
        int seveneleven = 0; // 보유 기프티콘 개수(7eleven)
        List<WalletGiftInfoResponseDto> impendingList = new ArrayList<>(); // 임박 기프티콘 리스트
        List<WalletGiftInfoResponseDto> giftList = new ArrayList<>(); // 일반 기프티콘 리스트
        
        // 월렛 기프티콘 리스트 조회
        List<Gift> targetList = giftRepository.findAllByGiverAndStatusAndDueDateAfterOrToday(giver.getId(), now);

        if (targetList != null || !targetList.isEmpty()) { // 보유 기프티콘이 있음
            // 사용처별 기프티콘 개수 계산
            Map<Store, Long> giftCountMap = countGiftsByStore(targetList);
            cu = Math.toIntExact(giftCountMap.getOrDefault(Store.CU, 0L));
            gs25 = Math.toIntExact(giftCountMap.getOrDefault(Store.GS25, 0L));
            seveneleven = Math.toIntExact(giftCountMap.getOrDefault(Store.SEVENELEVEN, 0L));
            
            for (Gift g : targetList) {
                // 기프티콘 총액 계산
                amount += g.getPrice();
                // 기프티콘 분류
                if (g.getDueDate().isBefore(LocalDateTime.now().plusDays(30))) { // 유효 기간 30일 이내
                    // 임박 리스트에 할당
                    impendingList.add(
                            WalletGiftInfoResponseDto.builder()
                                    .giftId(g.getId())
                                    .days(Math.toIntExact(Duration.between(now, g.getDueDate()).toDaysPart()))
                                    .store(g.getStore().getStore())
                                    .dueDate(g.getDueDate())
                                    .product(g.getProduct())
                                    .price(g.getPrice())
                                    .build()
                    );
                }
                else { // 유효 기간 30일 이후
                    // 일반 리스트에 할당
                    giftList.add(
                            WalletGiftInfoResponseDto.builder()
                                    .giftId(g.getId())
                                    .days(Math.toIntExact(Duration.between(now, g.getDueDate()).toDaysPart()))
                                    .store(g.getStore().getStore())
                                    .dueDate(g.getDueDate())
                                    .product(g.getProduct())
                                    .price(g.getPrice())
                                    .build()
                    );
                }
            }
        }

        // response data 생성
        WalletResponseDto responseDto = WalletResponseDto.builder()
                .receiver(receiver)
                .amount(amount)
                .cu(cu)
                .gs25(gs25)
                .seveneleven(seveneleven)
                .impendingList(impendingList)
                .giftList(giftList)
                .build();

        return ApiResponse.success(Success.GET_WALLET_SUCCESS, responseDto);
    }

    // 사용처(store) 필드의 값마다 gift 개수 세기
    public Map<Store, Long> countGiftsByStore(List<Gift> giftList) {
        // Gift 객체 리스트를 사용처(store) 필드의 값마다 그룹화하여 각 그룹의 개수를 계산하여 Map으로 반환
        return giftList.stream()
                .collect(Collectors.groupingBy(Gift::getStore, Collectors.counting()));
    }

    @Transactional
    public ApiResponse<?> walletDetail(Long giftId) {

        // Security Config로 책임 전가
//        // 기부자 여부 검증
//        if (!authUtils.getCurrentUserRole().equals(Role.ROLE_GIVER)) {
//            return ApiResponse.failure(Error.NOT_AUTHENTICATED_EXCEPTION);
//        }

        //Gift 있는지 확인
        Optional<Gift> giftOptional = giftRepository.findById(giftId);
        if(giftOptional.isEmpty())
            return ApiResponse.failure(Error.GIFT_NOT_FOUND_EXCEPTION);

        Gift gift = giftOptional.get();

        GetGiftResponseDto responseDto = GetGiftResponseDto.builder()
                .product(gift.getProduct())
                .price(gift.getPrice())
                .dueDate(gift.getDueDate())
                .imgUrl(gift.getImageUrl())
                .store(gift.getStore())
                .status(gift.getStatus())
                .boxId(gift.getGiftbox().getId())
                .build();

        // 조회 시 일단 사용처리
        gift.updateStatus("SELF_USED");

        return ApiResponse.success(Success.HOME_RECEIVER_GIFT_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> walletUpload(WalletUploadRequestDto requestDto) throws IOException {
        // 기부자 여부 검증
        if (!authUtils.getCurrentUserRole().equals(Role.ROLE_GIVER)) {
            return ApiResponse.failure(Error.NOT_AUTHENTICATED_EXCEPTION);
        }
        Giver giver = authUtils.getGiver();

        // set Default Giftbox
        Giftbox defaultGiftbox = giftboxRepository.findById(0L)
                .orElseThrow(()-> new ApiException(Error.GIFTBOX_NOT_FOUND_EXCEPTION));

        // Upload Image to Google Cloud Storage
        String imageUrl = uploadImageToGCS(requestDto);

        //CREATE Gift
        Gift newGift = requestDto.toEntity(giver, defaultGiftbox, imageUrl, requestDto.getStore().toString());
        giftRepository.save(newGift);

        return ApiResponse.success(Success.UPLOAD_GIFT_SUCCESS);
    }

    private String uploadImageToGCS(WalletUploadRequestDto requestDto) throws IOException {
        //이미지명 uuid 변환
        String uuid = UUID.randomUUID().toString();
        //이미지 형식 추출
        String ext = requestDto.getGiftImage().getContentType();

        // Google Cloud Storage 이미지 업로드
        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(BUCKET_NAME, uuid)
                        .setContentType(ext)
                        .build(),
                requestDto.getGiftImage().getInputStream()
        );
        log.info("successfully upload image to gcs");

        String imgUrl = "https://storage.googleapis.com/" + BUCKET_NAME + "/" + uuid;
        return imgUrl;
    }
}
