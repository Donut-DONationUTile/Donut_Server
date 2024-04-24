package zero.eight.donut.dto.donation;

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
public class DonateGiftRequestDto {
    private MultipartFile giftImage;
    private String product;
    private Integer price;
    private LocalDateTime dueDate;
    private Store store;
    private Boolean isRestored;

    public Gift toEntity(Giver giver, Giftbox giftbox, String imageUrl, String store){
        return Gift.builder()
                .giver(giver)
                .product(product)
                .isAssigned(false)
                .isMsgReceived(false)
                .status(Status.UNUSED)
                .price(price)
                .imageUrl(imageUrl)
                .store( Store.valueOf((store)))
                .dueDate(dueDate)
                .giftbox(giftbox)
                .build();
    }
}
