package zero.eight.donut.dto.donation;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
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
}
