package zero.eight.donut.dto.wallet;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import zero.eight.donut.domain.Gift;
import zero.eight.donut.domain.Giftbox;
import zero.eight.donut.domain.Giver;
import zero.eight.donut.domain.enums.Status;
import zero.eight.donut.domain.enums.Store;

import java.time.LocalDateTime;

@Getter
@Builder
public class WalletUploadRequestDto {
    private MultipartFile giftImage;
    private String product;
    private Integer price;
    private LocalDateTime dueDate;
    private Store store;
    private Boolean autoDonation;

    public Gift toEntity(Giver giver, Giftbox giftbox, String imageUrl, String store){
        return Gift.builder()
                .giver(giver)
                .product(product)
                .status(Status.STORED)
                .price(price)
                .imageUrl(imageUrl)
                .store( Store.valueOf((store)))
                .dueDate(dueDate)
                .giftbox(giftbox)
                .autoDonation(autoDonation)
                .build();
    }
}
