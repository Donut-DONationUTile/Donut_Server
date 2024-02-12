package zero.eight.donut.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "donation_id")
    private Long id; // 기부 정보 고유 ID

    @Column
    private Long sum; // 기부 금액

    @Column
    private Long count; // 기부 횟수

    @Column
    private Integer report; // 누적 경고 수

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giver_id")
    private Giver giver; // 기부자 고유 ID

}
