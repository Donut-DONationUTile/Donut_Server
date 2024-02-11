package zero.eight.donut.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zero.eight.donut.common.domain.BaseTimeEntity;
import zero.eight.donut.domain.enums.Status;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Gift extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gift_id")
    private Long id;

    @Column(length = 100)
    private String product;

    @Column
    private Boolean isAssigned;

    @Enumerated(value = EnumType.STRING)
    @Column
    private Status status;

    @Column
    private Integer price;

    @Column(length = 500)
    private String imageUrl;

    @Column(length = 50)
    private String store;

    @Column
    private LocalDateTime dueDate;

    //기부자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giver_id")
    private Giver giver;

    //기프티콘꾸러미
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giftbox_id")
    private Giftbox giftbox;

    //신고 -> 단방향으로 수정
//    @OneToOne(fetch = FetchType.LAZY)
//    private Report report;

    //메세지 -> 단방향으로 수정
//    @OneToOne(fetch = FetchType.LAZY)
//    private Message message;

    public void updateStatus(String status){
        this.status = Status.valueOf(status);
    }
}
