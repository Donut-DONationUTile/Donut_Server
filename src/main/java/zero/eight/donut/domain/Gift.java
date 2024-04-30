package zero.eight.donut.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import zero.eight.donut.common.domain.BaseTimeEntity;
import zero.eight.donut.domain.enums.Status;
import zero.eight.donut.domain.enums.Store;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class Gift extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gift_id")
    private Long id; // 기프티콘 고유 ID

    @Column(length = 100)
    private String product; // 상품명

    @Column
    @ColumnDefault("false")
    private Boolean isAssigned; // 할당여부

    @Column
    @ColumnDefault("false")
    private Boolean isMsgReceived; // 신규 메세지 등록 여부

    @Enumerated(value = EnumType.STRING)
    @Column
    private Status status; // 상태(used/unused/reported/stored/selfUsed)

    @Column
    private Integer price; // 가격

    @Column(length = 500)
    private String imageUrl; // 이미지 URL

    @Enumerated(value = EnumType.STRING)
    @Column
    private Store store; // 사용처

    @Column
    private LocalDateTime dueDate; // 사용 기한

    //기부자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giver_id")
    private Giver giver; // 기부자 고유 ID

    //기프티콘꾸러미
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giftbox_id")
    private Giftbox giftbox; // 꾸러미 고유 ID

    @Column(name = "auto_donation")
    @ColumnDefault("false")
    private Boolean autoDonation; // 자동 기부 허용

    //신고 -> 단방향으로 수정
//    @OneToOne(fetch = FetchType.LAZY)
//    private Report report;

//    //메세지 -> 단방향
//    @OneToOne(fetch = FetchType.LAZY)
//    private Message message;

    public void updateStatus(String status){
        this.status = Status.valueOf(status);
    }

    public void setIsAssigned() {
        this.isAssigned = true;
    }

    public void updateGiftbox(Giftbox giftbox) {
        this.giftbox = giftbox;
    }

    public void updateImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public void updateIsMsgReceived() {
        this.isMsgReceived = true;
    }
}
