package zero.eight.donut.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import zero.eight.donut.common.domain.BaseTimeEntity;
import zero.eight.donut.domain.enums.Status;

import java.time.LocalDateTime;

@Entity
@Builder
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

    @Column(length = 100)
    private String imageUrl;

    @Column(length = 50)
    private String store;

    @Column
    private LocalDateTime dueDate;

    //기부자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giver_id")
    private Giver giver;

}
