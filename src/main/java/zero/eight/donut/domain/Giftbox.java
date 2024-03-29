package zero.eight.donut.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zero.eight.donut.domain.enums.Store;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Giftbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "giftbox_id")
    private Long id;

    @Column
    private LocalDateTime assignedAt;

    @Enumerated(value = EnumType.STRING)
    @Column
    private Store store;

    @Column
    private Integer amount;

    @Column
    private Boolean isAvailable;

    @Column
    private LocalDateTime dueDate;

    //할당된 수혜자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Receiver receiver;

    //기프티콘
    @OneToMany(mappedBy = "giftbox", fetch = FetchType.LAZY)
    private List<Gift> giftList = new ArrayList<>();

    public void updateAmountAndIsAvailable(Integer amount, Boolean isAvailable){
        this.amount = amount;
        this.isAvailable =isAvailable;
    }

    public void updateDueDateAndAmount(LocalDateTime dueDate, Integer amount) {
        this.dueDate = dueDate;
        this.amount = amount;
    }
}
